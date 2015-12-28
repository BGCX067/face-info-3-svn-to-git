//---FORMULARIO PARA NUEVO COMENTARIO-------------------------------------------------
var formularioComentario = new Ext.FormPanel({
    border:false,
    name: 'Comentar',
    items:[{
        xtype:'textarea',
        fieldLabel: 'Escriba Comentario',
        allowBlank: false,
        autoWidth: true,
        maxLength: 250,
        id: 'comentario'
    }
    ]
});

//---VENTANA PARA NUEVO COMENTARIO-------------------------------------------------
var winComentar = new Ext.Window({
    id:'winCom',
    title: 'Comentar',
    bodyStyle: 'padding:10px;background-color:#fff;',
    width:320,
    layout: 'form',
    autoScroll: true,
    closeAction: 'hide',
    items:[formularioComentario],
    buttons: [{
        text:'Comentar',
        handler: function(){
            formularioComentario.getForm().submit({
                url : '/ServicioMuro',
                params: {
                    accion:"comentar"
                },
                success: function(form,action){
                    winComentar.hide();
                    Ext.getCmp('paginadorComentarios').moveFirst();
                    
                },
                failure: function(form,action){

                    switch (action.failureType) {
                        case Ext.form.Action.CLIENT_INVALID:
                            Ext.Msg.alert('Error', 'Hubo un error en el ingreso de los datos');
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
            winComentar.hide();
        }
    }]
});

//---GRILLA DE COMENTARIOS-------------------------------------------------
var comentarioGrid = new Ext.grid.GridPanel({
    autoHeight:true,
    autoWidth: true,
    stripeRows: true,
    autoScroll: true,
    id:'comentarioGrid',
    sm: checkComentarios,
    store:comentariosStore,
    tbar: toolbarComentarios,
    viewConfig:{
        //forceFit:true
    },
    columns : [
    checkComentarios,
    {
        xtype: 'gridcolumn',
        dataIndex: 'usuario',
        header: 'Usuario',
        //width:20,
        sortable: true
    },
    {
        xtype: 'datecolumn',
        format: 'd M Y',
        dataIndex: 'fecha',
        //width:15,
        align:'center',
        header: 'Fecha',
        sortable: true
    },
    {
        xtype: 'gridcolumn',
        id: 'colComentariosDescripcion',
        dataIndex: 'descripcion',
        header: 'Comentario',
        renderer: function(value, metadata, record){
            metadata.attr = 'style="white-space:normal"';
            return value;
        },
        sortable: true
    }
    ],
    autoExpandColumn: 'colComentariosDescripcion'

});
