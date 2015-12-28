//--BARRA SUPERIOR DE BOTONES - VER PERFIL--------------------------------------------------
var toolbarPerfil = new Ext.Toolbar({
    id: 'toolbarPerfil',
    items:[
    {
        id: 'btnModificarPerfil',
        text: 'Modificar Perfil',
        icon: '../Imagenes/papel.png',
        tooltip: 'Modificar Mi Perfil',
        scope: this,
        handler: function(){
            formularioPerfil.getForm().submit({
                url : '/ServicioUsuario',
                params: {
                    accion:"actualizarPerfil"
                },
                success: function(form,action){
                    Ext.Msg.alert('Perfil','Se ha actualizado correctamente');
                    Ext.getCmp('perfil').getUpdater().refresh();
                    Ext.getCmp('perfil').doLayout();
                //REFRESCAR!!!
                },
                failure: function(form,action){
                    switch (action.failureType) {
                        case Ext.form.Action.CLIENT_INVALID:
                            Ext.Msg.alert('Error', 'Algunos campos son incorrectos.');
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
    },
    {
        id: 'btnCambiarClave',
        text: 'Cambiar Clave',
        icon: '../Imagenes/key.png',
        tooltip: 'Cambiar Clave',
        handler : function(){
            Ext.getCmp('winCambiarClave').show();
        }
    },
    ]
});
