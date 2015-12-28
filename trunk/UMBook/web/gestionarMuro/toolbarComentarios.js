Ext.QuickTips.init();

//--STORE DE COMENTARIOS-----------------------------------------------------
var comentariosStore = new Ext.data.JsonStore({
    url: '/ServicioMuro',
    baseParams:{
        accion:'cargarComentario'
    },
    root: 'comentarios',
    totalProperty: 'total',
    fields: ['id','descripcion','fecha','usuario']
});

comentariosStore.load();

//--BARRA INFERIOR PAGINADORA------------------------------------------------
var paginadorComentarios = new Ext.PagingToolbar({
    store: comentariosStore,
    id:'paginadorComentarios',
    emptyMsg :'No se encontraron resultados',
    pageSize:20,
    style: {
        color: 'white'
    },
    displayInfo:true,
    displayMsg:'Comentarios {0} - {1} de {2}'
});

//--CHECKBOX COMENTARIOS------------------------------------------------
var checkComentarios = new Ext.grid.CheckboxSelectionModel({
    id: 'checkComentarios',
    width: 25,
    listeners:{
        'selectionchange':function(selModel){
            if (checkComentarios.hasSelection()){
                Ext.getCmp('btnEliminarComentarios').enable();
            } else{
                Ext.getCmp('btnEliminarComentarios').disable();
            }
            return true;
        }
    }
});

//--BARRA SUPERIOR DE BOTONES------------------------------------------------
var toolbarComentarios = new Ext.Toolbar({
    id: 'toolbarComentarios',
    items:[{
        id: 'btnComentar',
        text: 'Comentar',
        icon: '../Imagenes/add.png',
        tooltip: 'Comentar seleccionado',
        handler : function(){
            winComentar.show();
        },
        scope:this
    },{
        id: 'btnEliminarComentarios',
        text: 'Eliminar',
        icon: '../Imagenes/delete.png',
        tooltip: 'Eliminar registros seleccionados',
        disabled:true,
        handler : borrarComentarios,
        scope:this
    }]
});

function borrarComentarios(){
    if (!comentarioGrid.getSelectionModel().hasSelection()){
        Ext.MessageBox.alert("Error","Debe seleccionar al menos un registro!");
        return;
    }
    var modelSelections = comentarioGrid.getSelectionModel().getSelections();
    Ext.MessageBox.confirm('Confirmar', 'Est&aacute; seguro que desea eliminar los comentarios seleccionados?', function(btn){
        if (btn == 'yes'){
            var mask = new Ext.LoadMask(Ext.getBody(),{
                msg:"Eliminando Comentarios..."
            });
            mask.show();
            var selecciones = modelSelections;
            var ids = "";
            for(var i =0; selecciones[i];i++){
                ids += selecciones[i].id+'-';
            }
            Ext.Ajax.request({
                url: '/ServicioMuro',
                params: {
                    'accion':'eliminarComentario',
                    'ids':ids
                },
                success: function (object,response){
                    mask.hide();
                    Ext.getCmp('paginadorComentarios').moveFirst();
                },
                failure: function(form,action){

                    switch (action.failureType) {
                        case Ext.form.Action.CLIENT_INVALID:
                            Ext.Msg.alert('Error', 'No se pudo eliminar');
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
