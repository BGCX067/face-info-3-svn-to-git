//--FORMULARIO apra subir una foto-----------------------------------------------
 
var formularioIngresarFotos =new Ext.form.FormPanel({
    id: 'formularioIngresarFotos',
    border:false,
    bodyStyle: 'padding: 10px 10px 0 10px;',
    fileUpload: true,                       //Habilitamos el envio de archivos.
    items: [
    {
        xtype: 'fileuploadfield',
        id: 'form-file',
        emptyText: 'Seleccione una foto',
        fieldLabel: 'Archivo',
        allowBlank: false,
        name: 'archivo',
        buttonText: '',
        buttonCfg: {
            iconCls: 'upload-icon'
        }
    }
    ,{
        xtype:'combo',
        id:'idAlbum',
        forceSelection:true,
        alowBlank: false,
        store:storeAlbum,
        valueField: 'id',
        autoSelect:true,
        editable:false,
        hiddenName: 'id',
        displayField: 'nombre',
        triggerAction: 'all',
        mode: 'remote',
        
        emptyText: 'Seleccione un Album',
        fieldLabel: 'Album'
    },
    {
        xtype:'textarea',
        fieldLabel: 'Escriba descripcion',
        autoWidth: true,
        name: 'descripcion'
    }
    ]
});

//--VENTANA PARA CARGAR FOTO-----------------------------------------------------------

var winSubirFoto = new Ext.Window({
    id:'winSubirFoto',
    title: 'Subir Foto',
    bodyStyle: 'padding:10px;background-color:#fff;',
    width:400,
    layout: 'form',
    autoScroll: true,
    closeAction: 'hide',
    items:[formularioIngresarFotos],
    buttons: [{
        text:'Subir',
        handler: function(){
            if(Ext.getCmp('idAlbum').getValue()>0){
                //VALIDO EXTENSION DE LA FOTO
                if(Ext.getCmp('form-file').value.lastIndexOf('.jpeg')==-1 && Ext.getCmp('form-file').value.lastIndexOf('.jpg')==-1 && Ext.getCmp('form-file').value.lastIndexOf('.png')==-1 && Ext.getCmp('form-file').value.lastIndexOf('.gif')==-1){
                    Ext.Msg.alert('Error','El formato de la foto es incorrecto!');
                }
                else if(formularioIngresarFotos.getForm().isValid()){
                    formularioIngresarFotos.getForm().submit({
                        url:'/SubirFotos',
                        params:{
                            idAlbum:Ext.getCmp('idAlbum').getValue()
                        },
                        waitMsg: 'Subiendo la foto...',
                        success: function(form,action){
                            Ext.Msg.alert('Exito','Sus fotos se cargaron exitosamente');
                            winSubirFoto.hide();
                        },
                        failure: function(form,action){

                            switch (action.failureType) {
                                case Ext.form.Action.CLIENT_INVALID:
                                    Ext.Msg.alert('Error', 'Algunos campos son incorrectos');
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
                    Ext.Msg.alert('Error', 'Ingrese foto');
                }
            }else
            {
                Ext.Msg.alert('Error', 'Seleccione un álbum');
            }
        }
    }
    ,{
        text:'Cancelar',
        handler: function(){
            formularioIngresarFotos.getForm().reset();
            winSubirFoto.hide();
        }
    }]
});