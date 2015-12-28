Ext.BLANK_IMAGE_URL = '../ext-3.3.0/resources/images/default/s.gif';

MyPanelUi = {
    init: function (){
        Ext.QuickTips.init();
        this.formulario= new Ext.FormPanel({
            title: 'LOGIN',
            padding: 30,
            buttonAlign: 'center',
            id: 'formulario-login',
            autoWidth: true,
            autoHeight: true,
            border: false,
            items: [
            {
                xtype:'textfield',
                fieldLabel: 'Mail',
                allowBlank: false,
                value:"a@a.com",
                autoWidth: true,
                emptyText: "usuario@dominio.com",
                vtype: 'email',
                id: 'usuario-txt'
            },

            {
                xtype:'textfield',
                fieldLabel: 'Clave',
                value:"a",
                allowBlank: false,
                autoWidth: true,
                inputType: 'password',
                id: 'clave-txt'
            }
            ],
            buttons:[{
                id:'aceptar',
                text:'Entrar!!!',
                handler:this.sendData,
                scope:this
            },
            {
                id:'registrar',
                text:'Registrarse',
                handler:function(){
                    winRegistrarse.show();
                    this.viewPort.el.mask();
                },
                scope:this
            }]
            ,
            formBind: true

        });

        //--INTERFAZ GENERAL------------------------------------------------------------
        this.viewPort = new Ext.Viewport({
            title: 'UMBOOK',
            layout: 'border',
            id: 'viewPort',
            defaults: {
                unstyled: true,
                layout: 'fit'
            },
            items: [{
                region: 'south',    
                height: 100
            },{
                region:'west',
                width: 400
            },
            {
                region:'east',
                width: 400
            },
            {
                xtype: 'panel',
                region:'north',
                html: '<center><IMG SRC="../Imagenes/umbook.png" VSPACE=80">',
                height: 300
            },
            {
                region:'center',
                autoHeight: true,
                items: this.formulario
            }
            ]
        });

    },
    sendData:function(){
        if(!this.formulario.getForm().isValid()){
            Ext.Msg.alert('Alerta','El formato de los datos no es válido');
        }
        else
        {
            this.formulario.getForm().submit({
                url : '/ServicioUsuario',
                params: {
                    accion:"login"
                },
                failure: function (form, action) {
                    var object = Ext.util.JSON.decode(action.response.responseText);
                    
                    Ext.Msg.alert('Error',object.msg);
                },
                success: function (form,request)
                {
                    window.location.href='/ServicioMuro?accion=muro';
                }
            })
        }
    }

}

Ext.onReady(MyPanelUi.init,MyPanelUi);
