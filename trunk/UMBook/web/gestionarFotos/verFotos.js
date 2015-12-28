//--STORE PARA ALMACENAR FOTOS--------------------------------------------------
var storeFotos =  new Ext.data.JsonStore({
    url: '/ServicioFotos',
    baseParams:{
        accion:'buscarFotos'
    },
    root: 'data',
    id:'id',
    fields:[
    'id', 'descripcion', 'ubicacion','idAlbum','idUsuario','descripcionAlbum',
    {
        name: 'shortName',
        mapping: 'ubicacion',
        convert: shortName
    }
    ]
});

//--PLANTILLA PARA MOSTRAR FOTOS---------------------------------------------------------
var vistaFotos = {
    id: 'vistaFotos',
    itemSelector: 'div.thumb-wrap',
    style:'overflow:auto',
    multiSelect: true,
    emptyText:'<br><b style="padding-left: 20px">No hay fotos en este álbum.</b>',
    plugins: new Ext.DataView.DragSelector({
        dragSafe:true
    }),
    autoScroll: true,
    store: storeFotos,
    tpl: new Ext.XTemplate(
        '<tpl for=".">',
        '<tpl if="xindex == 1">',
        '<br><b style="padding-left: 20px">Álbum: {descripcionAlbum}</b><br>',
        '</tpl>',
        '<div class="thumb-wrap foto" id="{id}" ondblclick="mostrarFoto(id, title)" title="{descripcion}">',
        '<div class="thumb"><img src="/BajarFotos?idFoto={id}" class="thumb-img"></div>',
        '<span>{shortName}</span></div>',
        '</tpl>'
        )
           
};

function mostrarFoto(id,descripcion){
    var winVerFoto = new Ext.Window({
        id:'winVerFoto',
        title: descripcion,
        width:550,
        height:550,
        onHide:function(){
            Ext.getBody().unmask()
            },
        bodyStyle:'background-color:#fff;padding: 10px',
        closeAction: 'destroy',
        html: '<img width=100% height=100% src="/BajarFotos?idFoto='+id+'">'
    });

    winVerFoto.show();
    Ext.getBody().mask();
}


//--CORTA EL NOMBRE HASTA 22 CARACTERES-----------------------------------------
function shortName(name){
    if(name.length > 22){
        return name.substr(0, 22) + '...';
    }
    return name;
};
