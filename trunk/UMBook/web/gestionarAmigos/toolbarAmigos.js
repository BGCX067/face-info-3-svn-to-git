//--STORE DE AMIGOS-----------------------------------------------------
var amigosStore = new Ext.data.JsonStore({
    url: '/ServicioAmigos',
    baseParams:{
        accion:'cargarAmigos',
        idGrupo:'todos'
    },
    root: 'amigos',
    totalProperty: 'total',
    fields: ['id','nombre','apellido', 'mail']
});


//--BARRA INFERIOR PAGINADORA----------------------------------------------------------------
var paginadorAmigos = new Ext.PagingToolbar({
    store: amigosStore,
    id:'paginadorAmigos',
    emptyMsg : 'No se encontraron resultados',
    pageSize:20,
    style: {
        color: 'white'
    },
    displayInfo:true,
    displayMsg:'Amigos {0} - {1} de {2}'

});

//--CHECKBOX AMIGOS---------------------------------------------------------------------
var checkAmigos = new Ext.grid.CheckboxSelectionModel({
    listeners:{
        'selectionchange':function(selModel){
            if (checkAmigos.hasSelection()){
                if(Ext.getCmp('btnEliminarAmigos'))
                    Ext.getCmp('btnEliminarAmigos').enable();
                if(Ext.getCmp('btnVisitarAmigo'))
                    Ext.getCmp('btnVisitarAmigo').enable();
                if(Ext.getCmp('btnInvitar'))
                    Ext.getCmp('btnInvitar').enable();
            } else{
                if(Ext.getCmp('btnEliminarAmigos'))
                    Ext.getCmp('btnEliminarAmigos').disable();
                if(Ext.getCmp('btnInvitar'))
                    Ext.getCmp('btnInvitar').disable();
                if(Ext.getCmp('btnVisitarAmigo'))
                    Ext.getCmp('btnVisitarAmigo').disable();
            }
            return true;
        }
    }
});

//--BARRA SUPERIOR DE BOTONES - VER AMIGOS--------------------------------------------------
var toolbarAmigos = {
    id: 'toolbarAmigos',
    items:[
    {
        id: 'btnBuscar',
        text: 'Buscar Amigos',
        icon: '../Imagenes/find.png',
        tooltip: 'Busqueda de nuevos amigos a invitar',
        handler : function(){
            winBuscarAmigos.show();
        },
        scope:this
    },
    {
        id: 'btnEliminarAmigos',
        text: 'Eliminar',
        icon: '../Imagenes/user_delete.png',
        tooltip: 'Eliminar amigos seleccionados',
        disabled:true,
        handler : borrarAmigos,
        scope:this
    },
    {
        id: 'btnVisitarAmigo',
        text: 'Visitar a este amigo',
        icon: '../Imagenes/puerta.png',
        tooltip: 'Visitar muro del amigo',
        disabled:true,
        handler : visitarAmigo,
        scope:this
    },
    {
        text: 'Borrar Grupo',
        icon: '../Imagenes/delete.png',
        tooltip: 'Borrar el grupo seleccionado',
        handler: borrarGrupo,
        scope: this
    }
    ]
};

function borrarAmigos(){
    if (!amigosGrid.getSelectionModel().hasSelection()){
        Ext.MessageBox.alert("Error","Debe seleccionar al menos un amigo!");
        return;
    }
    var modelSelections = amigosGrid.getSelectionModel().getSelections();
    Ext.MessageBox.confirm('Confirmar', 'Est&aacute; seguro que desea eliminar el/los amigos seleccionados?', function(btn){
       
        if (btn == 'yes'){
            var mask = new Ext.LoadMask(Ext.getBody(),{
                msg:"Eliminando Amigos..."
            });
            mask.show();
            var selecciones = modelSelections;
            var ids = "";
            for(var i =0; selecciones[i];i++){
                ids += selecciones[i].id+'-';
            }
            Ext.Ajax.request({
                url: '/ServicioAmigos',
                params: {
                    'accion':'eliminarAmigo',
                    'ids':ids,
                    'idGrupo':Ext.getCmp('grillaAmigos').getStore().baseParams.idGrupo

                },
                success: function (object,response){
                    mask.hide();
                    Ext.getCmp('paginadorAmigos').moveFirst();
                },
                failure: function(form,action){

                    switch (action.failureType) {
                        case Ext.form.Action.CLIENT_INVALID:
                            Ext.Msg.alert('Error', 'No se pudo');
                            break;
                        case Ext.form.Action.CONNECT_FAILURE:
                            Ext.Msg.alert('Error', 'Ajax communication failed');
                            break;
                        case Ext.form.Action.SERVER_INVALID:
                            Ext.Msg.alert('Error', action.result.msg);
                            break;
                        default:
                            Ext.Msg.alert('Error',action.result.msg);
                    }
                }
            });
        }
    });
}
function visitarAmigo(){
    if (amigosGrid.getSelectionModel().getCount()!=1){
        Ext.MessageBox.alert("Error","Debe seleccionar solo un amigo para visitar!");
        return;
    }
    var modelSelections = amigosGrid.getSelectionModel().getSelections();
            
    Ext.Ajax.request({
        url: '/ServicioAmigos',
        params: {
            'accion':'visitarAmigo',
            'idVisitado':modelSelections[0].id
        },
        success: function (object,response){
            window.location.href='/ServicioMuro?accion=muro';
                   
        },
        failure: function(form,action){

            switch (action.failureType) {
                case Ext.form.Action.CLIENT_INVALID:
                    Ext.Msg.alert('Error', 'No se pudo');
                    break;
                case Ext.form.Action.CONNECT_FAILURE:
                    Ext.Msg.alert('Error', 'Ajax communication failed');
                    break;
                case Ext.form.Action.SERVER_INVALID:
                    Ext.Msg.alert('Error', action.result.msg);
                    break;
                default:
                    Ext.Msg.alert('Error',action.result.msg);
            }
        }
    });
}

function borrarGrupo(){
    
    var grupoSelection= Ext.query("*[class=x-tree-node-el x-tree-node-leaf x-unselectable grupo x-tree-selected]");
    if (!grupoSelection[0]){
        Ext.MessageBox.alert("Error","Debe seleccionar al menos un grupo!");
        return;
    }
    Ext.MessageBox.confirm('Confirmar', 'Est&aacute; seguro que desea eliminar el grupo seleccionado?', function(btn){
        if (btn == 'yes'){
            var mask = new Ext.LoadMask(Ext.getBody(),{
                msg:"Eliminando grupo..."
            });
            mask.show();
            var idGrupo=grupoSelection[0].getAttribute('ext:tree-node-id');
            Ext.Ajax.request({
                url: '/ServicioAmigos',
                params: {
                    'accion':'eliminarGrupo',
                    'id':idGrupo
                },
                success: function (object,response){
                    mask.hide();
                    window.location.reload();
                    Ext.Msg.alert('Correcto', response.result.msg);
                },
                failure: function(form,action){
                    switch (action.failureType) {
                        case Ext.form.Action.CLIENT_INVALID:
                            Ext.Msg.alert('Error', 'No se pudo');
                            break;
                        case Ext.form.Action.CONNECT_FAILURE:
                            Ext.Msg.alert('Error', 'Ajax communication failed');
                            break;
                        case Ext.form.Action.SERVER_INVALID:
                            Ext.Msg.alert('Error', action.result.msg);
                            break;
                        default:
                            Ext.Msg.alert('Error',action.result.msg);
                    }
                }
            });
        }
    });
}


//--BARRA SUPERIOR DE BOTONES - BUSCAR AMIGOS--------------------------------------------------------
var toolbarBuscarAmigos = {
    id: 'toolbarBuscarAmigos',
    items:[
    {
        id: 'btnBuscar',
        text: 'Buscar Amigos',
        icon: '../Imagenes/find.png',
        tooltip: 'Busqueda de nuevos amigos a invitar',
        handler : function(){
            winBuscarAmigos.show();
        },
        scope:this
    },
    {
        id: 'btnInvitar',
        text: 'Invitar Amigos',
        icon: '../Imagenes/user_add.png',
        tooltip: 'Invitar amigos seleccionados',
        handler : invitarAmigos,
        disabled:true,
        scope:this
    },
    {
        id: 'btnVolver',
        text: 'Volver A Amigos',
        icon: '../Imagenes/navigate_left2_16.png',
        tooltip: 'Volver a la grilla de amigos',
        handler : volverAmigos,
        scope:this
    },{
        id: 'btnVisitarAmigo',
        text: 'Visitar a este amigo',
        icon: '../Imagenes/puerta.png',
        tooltip: 'Visitar muro del amigo',
        disabled:true,
        handler : visitarAmigo,
        scope:this
    }]
};

function invitarAmigos(){
    if (!amigosGrid.getSelectionModel().hasSelection()){
        Ext.MessageBox.alert("Error","Debe seleccionar al menos un amigo!");
        return;
    }
    var modelSelectionsInvitar = amigosGrid.getSelectionModel().getSelections();

    new Ext.Window({
        id:'winInvitarAmigos',
        title: 'Elegir Grupo',
        width:310,
        bodyStyle:'background-color:#fff;padding: 10px',
        closeAction: 'destroy',
        items:[new Ext.form.FormPanel({
            id:'formularioInvitar',
            border:false,
            items:[
            {
                xtype:'combo',
                id:'comboGrupo',
                store: new Ext.data.JsonStore({

                    url: '/ServicioAmigos',
                    baseParams:{
                        accion:'cargarArbolGrupos',
                        invitar:'true'
                    },
                    root: 'grupos',
                    totalProperty: 'total',
                    fields: ['id','text']
                }),
                forceSelection:true,
                valueField: 'id',
                editable:false,
                displayField: 'text',
                triggerAction: 'all',
                mode: 'remote',
                alowBlank: false,
                emptyText: 'Seleccione un Grupo',
                fieldLabel: 'Grupo'
            }]
        })],
        buttons: [{
            text:'Invitar',
            handler: function(){
                var mask = new Ext.LoadMask(Ext.getBody(),{
                    msg:"Invitando Amigos..."
                });
                
                var selecciones = modelSelectionsInvitar;
                var ids = "";
                for(var i =0; selecciones[i];i++){
                    ids += selecciones[i].id+'-';
                }
                if(Ext.getCmp('comboGrupo').getValue()>0){
                    mask.show();
                
                    Ext.getCmp('formularioInvitar').getForm().submit({

                        url: '/ServicioAmigos',
                        params: {
                            'accion':'invitarAmigo',
                            'ids':ids,
                            'idGrupo':Ext.getCmp('comboGrupo').getValue()
                        },
                        success: function (object,response){
                            mask.hide();
                            Ext.getCmp('winInvitarAmigos').close();
                            volverAmigos();
                            
                        },
                        failure: function(form,action){
                            mask.hide();
                            Ext.getCmp('winInvitarAmigos').close();
                            switch (action.failureType) {
                                case Ext.form.Action.CLIENT_INVALID:
                                    Ext.Msg.alert('Error', 'No se pudo');
                                    break;
                                case Ext.form.Action.CONNECT_FAILURE:
                                    Ext.Msg.alert('Error', 'Ajax communication failed');
                                    break;
                                case Ext.form.Action.SERVER_INVALID:
                                    Ext.Msg.alert('Error', action.result.msg);
                                    break;
                                default:
                                    Ext.Msg.alert('Error',action.result.msg);

                         
                            }
                        }
                    });
                }
                else
                {
                    Ext.Msg.alert('Error', 'Seleccione Grupo!!');
                }
            }
        }]
    }).show();


}
function volverAmigos(){
    Ext.getCmp('grillaAmigos').getStore().baseParams={
        accion:'cargarAmigos',
        idGrupo:'todos'
    },
    amigosStore.load();
    //--INTERCAMBIO LAS TOOLBAR---------------------------------------------------
    Ext.getCmp('toolbarBuscarAmigos').destroy();
    new Ext.Toolbar(toolbarAmigos);
    Ext.getCmp('grillaAmigos').add('toolbarAmigos');
    Ext.getCmp('grillaAmigos').doLayout();
}