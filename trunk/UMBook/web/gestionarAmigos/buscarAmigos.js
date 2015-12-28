var texto_mail1= new Ext.form.TextField({
    fieldLabel: '*Mail',
    emptyText: "usuario@dominio.com",
    vtype: 'email',
    id: 'mail-txt1'
})

var texto_nombre1= new Ext.form.TextField({
    fieldLabel: '*Nombre',
    vtype:'alpha',
    id: 'nombre-txt1'
})

var texto_apellido1= new Ext.form.TextField({
    fieldLabel: '*Apellido',
    vtype:'alpha',
    id: 'apellido-txt1'
})

//FORMULARIO PARA BUSCAR AMIGO----------------------------------------

var formularioBuscarAmigos = new Ext.form.FormPanel({
    border:false,
    // labelWidth: 130,
    autoWidth:true,
    name: 'busqueda',
    defaults: {
        width: 160
    },
    items:[new Ext.form.Radio({
        boxLabel:'Buscar por nombre y/o apellido',
        name:'radito',
        autoWidth: true,
        checked:true,
        listeners:{
            check:function(){
                texto_nombre1.disable(),texto_apellido1.disable(),texto_mail1.enable()
            }
        }
    }),
    texto_nombre1,
    texto_apellido1,
    new Ext.form.Radio({
        boxLabel:'Buscar por mail',
        name:'radito',
        autoWidth: true,
        listeners:{
            check:function(){
                texto_mail1.disable(), texto_nombre1.enable(),texto_apellido1.enable()
            }
        }
    }),
    texto_mail1.disable()
    ]
});

//--VENTANA para el Formulario buscar amigos-----------------------------------------------------

var winBuscarAmigos = new Ext.Window({
    id:'winBuscarAmigos',
    title: 'Buscar Amigos',
    width:350,
    height:250,
    bodyStyle:'background-color:#fff;padding: 10px',
    autoScroll: true,
    closeAction: 'hide',
    items:[formularioBuscarAmigos],
    buttons: [{
        text:'Buscar',
        handler: function(){
            if (Ext.getCmp('nombre-txt1').getValue()==='' && Ext.getCmp('apellido-txt1').getValue()==='' && Ext.getCmp('mail-txt1').getValue()==='')
            {
                Ext.Msg.alert('Error','Algun campo debe ser completado!!!!');
            }
            else if (!formularioBuscarAmigos.getForm().isValid())
                {
                    Ext.Msg.alert('Error','Formato incorrecto de los datos');
                }
            else
            {
                //--MODIFICO EL STORE---------------------------------------------------------
                Ext.getCmp('grillaAmigos').getStore().baseParams={
                    accion:'buscarAmigos',
                    nombre:Ext.getCmp('nombre-txt1').getValue(),
                    apellido:Ext.getCmp('apellido-txt1').getValue(),
                    mail:Ext.getCmp('mail-txt1').getValue()
                },

                amigosStore.load();
                //--INTERCAMBIO LAS TOOLBAR---------------------------------------------------
                if(Ext.getCmp('toolbarAmigos')){
                    Ext.getCmp('toolbarAmigos').destroy();
                    new Ext.Toolbar(toolbarBuscarAmigos);
                    Ext.getCmp('grillaAmigos').add('toolbarBuscarAmigos');
                    Ext.getCmp('grillaAmigos').doLayout();
                }
                winBuscarAmigos.hide();
            }
        }
    },{
        text:'Cancelar',
        handler: function(){
            formularioBuscarAmigos.getForm().reset();
            winBuscarAmigos.hide();
        }
    }]
});


