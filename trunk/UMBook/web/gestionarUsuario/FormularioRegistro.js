//FILTRO DE LOCALIDADES---------------------------------------------
texto_ciudad.on('select',function(cmb,record,index){
    texto_localidad.enable();
    localidadStore.load({
        params:{
            idCiudad:record.get('id')
        }
    });
    texto_localidad.clearValue();
});

Ext.QuickTips.init();

var formulario = new Ext.form.FormPanel({
    border:false,
    labelWidth: 90,
    autoWidth:true,
    id:'formRegistro',
    name: 'registro',
    defaults: {
        width: 160
    },
    items:[
    texto_nombre,
    texto_apellido,
    texto_mail,
    texto_clave1,
    texto_clave2,
    texto_ciudad,
    texto_localidad,
    texto_sexo,
    texto_cumple,
    texto_telefono,
    texto_celular,
    texto_carrera
    ],
    html: "<br>Los campos con * son obligatorios"
});


var winRegistrarse = new Ext.Window({
    id:'winRegistrarse',
    title: 'Registrarse',
    width:320,
    height:500,
    bodyStyle:'background-color:#fff;padding: 10px',
    autoScroll: true,
    closeAction:'hide',
    onHide:function(){Ext.getCmp('viewPort').el.unmask()},
    items:[formulario],
    buttons: [{
        text:'Enviar',
        handler: function(){
            if (Ext.getCmp('clave1-txt').getValue() === Ext.getCmp('clave2-txt').getValue()) //valido claves
            {
                formulario.getForm().submit({
                    url : './ServicioUsuario',
                    params: {
                        accion:"registrarse"
                    },
                    success: function(form,action){
                        winRegistrarse.hide();
                        Ext.Msg.alert('Registro','Se ha registrado correctamente. Ya puede loguearse.');
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
            else
            {
                Ext.Msg.alert('Error','Contraseñas diferentes!!!!');
            }
        }
    },{
        text:'Cancelar',
        handler: function(){
            winRegistrarse.hide();
            formulario.getForm().reset();
            
        }
    }]
});
