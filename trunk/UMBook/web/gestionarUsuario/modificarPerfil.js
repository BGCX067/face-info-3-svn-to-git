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

//FORMULARIO PARA MODIFICAR LA CLAVE----------------------------------------------
var formularioClave = new Ext.form.FormPanel({
    border:false,
    labelWidth: 90,
    autoWidth:true,
    id:'formularioClave',
    name: 'Clave',
    defaults: {
        width: 160
    },
    items:[
    new Ext.form.TextField({
        fieldLabel: 'Clave Actual',
        allowBlank: false,
        inputType: 'password',
        id: 'claveActual'
    }),
    new Ext.form.TextField({
        fieldLabel: 'Nueva Clave',
        allowBlank: false,
        inputType: 'password',
        id: 'claveNueva1'
    }),
    new Ext.form.TextField({
        fieldLabel: 'Repetir Nueva Clave',
        allowBlank: false,
        inputType: 'password',
        id: 'claveNueva2'
    })
    ]
});

//VENTANA PARA MODIFICAR CLAVE------------------------------------------------------------
var winCambiarClave = new Ext.Window({
    id:'winCambiarClave',
    title: 'Cambiar Clave',
    width:320,
    height:200,
    bodyStyle:'background-color:#fff;padding: 10px',
    autoScroll: true,
    closeAction:'hide',
    //onHide:function(){Ext.Msg.alert('Perfil','Se ha registrado correctamente. Ya puede loguearse.')},
    items:[ formularioClave],
    buttons: [{
        text:'Enviar',
        handler: function(){
            if (Ext.getCmp('claveNueva1').getValue() === Ext.getCmp('claveNueva2').getValue()) //valido claves
            {
                formularioClave.getForm().submit({
                    url : '/ServicioUsuario',
                    params: {
                        accion:"actualizarClave"
                    },
                    success: function(form,action){
                        winCambiarClave.hide();
                        Ext.Msg.alert('Clave','Su clave se ha actualizado correctamente.');
                    },
                    failure: function(form,action){
                        switch (action.failureType) {
                            case Ext.form.Action.CLIENT_INVALID:
                                Ext.Msg.alert('Error', 'Form fields may not be submitted with invalid values');
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
                Ext.Msg.alert('Error','Las nuevas contraseñas no cinciden!!');
            }
        }
    },{
        text:'Cancelar',
        handler: function(){
            formularioClave.getForm().reset();
            winCambiarClave.hide();
        }
    }]
});