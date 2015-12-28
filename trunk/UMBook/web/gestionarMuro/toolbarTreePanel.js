var paramFormCrear="";
var urlFormCrear="";

//--TOOLBAR DEL TREE PANEL GENERAL--------------------------------------------
var toolbarTreePanel = new Ext.Toolbar({
    id: 'toolbarTreePanel',
    items:[{
        text: 'Nuevo Album',
        iconCls: 'album-btn',

        handler: function(){
            paramFormCrear={
                accion:"crearAlbum"
            },
            urlFormCrear= '/ServicioFotos',
            tituloWin='Crear Album',
            formularioAlbumGrupo.getForm().reset();
            winCrearAlbumGrupo.show();
        }
    },
    {
        text: 'Nuevo Grupo',
        iconCls: 'grupo-btn',
        handler: function(){
            paramFormCrear={
                accion:"crearGrupo"
            },
            urlFormCrear= '/ServicioAmigos',
            tituloWin='Crear Grupo',
            formularioAlbumGrupo.getForm().reset();
            winCrearAlbumGrupo.show();

        }
    }    
    ]
});


//--FORMULARIO PARA CREAR ALBUM O GRUPO--------------------------------------------
var formularioAlbumGrupo = new Ext.FormPanel({
    border:false,
    items:[{
        xtype:'textfield',
        fieldLabel: 'Nombre',
        allowBlank:false,
        autoWidth: true,
        id: 'nombre'
    },{
        xtype:'textarea',
        fieldLabel: 'Escriba descripcion',
        autoWidth: true,
        id: 'descripcion'
    }
    ]
});

//--VENTANA PARA CREAR ALBUM O GRUPO--------------------------------------------
var winCrearAlbumGrupo = new Ext.Window({
    id:'winCrearAlbumGrupo',
    title: 'Crear',
    bodyStyle: 'padding:10px;background-color:#fff;',
    width:320,
    layout: 'form',
    autoScroll: true,
    closeAction: 'hide',
    items:[formularioAlbumGrupo],
    buttons: [{
        text:'Aceptar',
        handler: function(){
            formularioAlbumGrupo.getForm().submit({
                url:urlFormCrear, 
                params:paramFormCrear,
                success: function(form,action){
                    Ext.Msg.alert('Bien!!!','Creación exitosa');
                    winCrearAlbumGrupo.hide();
                    window.location.reload();
                //arbolGrupo.getLoader().load();
                //document.getElementById('PanelPerfil');
                
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
    }
    ,{
        text:'Cancelar',
        handler: function(){
            winCrearAlbumGrupo.hide();
        }
    }]
});