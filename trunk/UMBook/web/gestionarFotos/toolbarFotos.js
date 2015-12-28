//--BARRA SUPERIOR DE BOTONES - VER AMIGOS--------------------------------------------------
var toolbarFotos = new Ext.Toolbar({
    id: 'toolbarFotos',
    items:[
    {
        id: 'btnSubirFoto',
        text: 'Subir foto',
        icon: '../Imagenes/find.png',
        tooltip: 'Cargue su Foto',
        handler : function(){
            Ext.getCmp('winSubirFoto').show();
        },
        scope:this
    },
    {
        id: 'btnEliminarFotos',
        text: 'Eliminar',
        icon: '../Imagenes/delete.png',
        tooltip: 'Eliminar fotos/albumes seleccionados',
        //disabled:true,
        handler : borrarFotos,
        scope:this
    },
    ]
});


function borrarFotos(){
    var fotoSelection= Ext.query("*[class=thumb-wrap foto x-view-selected]");
    var albumSelection= Ext.query("*[class=thumb-wrap album x-view-selected]");
    if (!fotoSelection[0] && !albumSelection[0]){
        Ext.MessageBox.alert("Error","Debe seleccionar al menos un elemento!");
        return;
    }
    Ext.MessageBox.confirm('Confirmar', 'Est&aacute; seguro que desea eliminar los elementos seleccionados?', function(btn){
        if (btn == 'yes'){
            var mask = new Ext.LoadMask(Ext.getBody(),{
                msg:"Eliminando..."
            });
            mask.show();
            var selecciones = '';  
            var action = '';
            if (fotoSelection[0])
            {
                selecciones = fotoSelection;
                action = 'eliminarFoto';
            }
            else if (albumSelection[0]){
                selecciones = albumSelection;
                action = 'eliminarAlbum';
            }
            
            var ids = "";
            for(var i =0; selecciones[i];i++){
                ids += selecciones[i].id+'-';
            }
            
            Ext.Ajax.request({
                url: '/ServicioFotos',
                params: {
                    'accion':action,
                    'ids':ids
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
