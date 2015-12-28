Ext.QuickTips.init();

//--STORE PARA ALMACENAR ALBUMS--------------------------------------------------
var storeAlbum =  new Ext.data.JsonStore({
    url: '/ServicioFotos',
    baseParams:{
        accion:'cargarAlbums'
    },
    autoLoad: true,
    root: 'data',
    id:'id',
    fields:[
    'id', 'nombre','fecha',
    {
        name: 'shortName',
        mapping: 'nombre',
        convert: shortName
    }
    ]
});

//--PLANTILLA PARA MOSTRAR ALBUMS---------------------------------------------------
var vistaAlbums = {
    id: 'vistaAlbums',
    itemSelector: 'div.thumb-wrap',
    style:'overflow:auto',
    multiSelect: true,

    plugins: [new Ext.DataView.DragSelector(),
    new Ext.DataView.LabelEditor({
        dataIndex: 'name'
    })
    ],
    store: storeAlbum,
    listeners: {
        'dblclick'       : {
            fn:mostrarFotos,
            scope:this
        },
        'loadexception'  : {
            fn:onLoadException
        },
        'beforeselect'   : {
            fn:function(vistaAlbums){
                return vistaAlbums.store.getRange().length > 0;
            }
        }
    },
    tpl: new Ext.XTemplate(

        '<tpl for=".">',
        '<div class="thumb-wrap album" id="{id}">',
        '<div class="thumb"><img src="../Imagenes/carpeta.jpg" class="thumb-img"></div>',
        '<span>{shortName}</span></div>',
        '</tpl>'
        )
};

function mostrarFotos (){
    storeFotos.baseParams={
        accion:'buscarFotos',
        idAlbum:Ext.getCmp('vistaAlbums').getSelectedNodes()[0].id
    }
    storeFotos.load();

    Ext.getCmp('vistaAlbums').destroy();
    new Ext.DataView(vistaFotos);
    Ext.getCmp('images').add('vistaFotos');
    Ext.getCmp('images').doLayout();
}

var onLoadException = function(v,o){
    this.view.getEl().update('<div style="padding:10px;">Error loading images.</div>');
}

   
/**
 * Create a DragZone instance for our JsonView
 */
var ImageDragZone = function(vistaAlbums, config){
    this.view = vistaAlbums;
    ImageDragZone.superclass.constructor.call(this, vistaAlbums.getEl(), config);
};
Ext.extend(ImageDragZone, Ext.dd.DragZone, {
    // We don't want to register our image elements, so let's
    // override the default registry lookup to fetch the image
    // from the event instead
    getDragData : function(e){
        var target = e.getTarget('.thumb-wrap');
        if(target){
            var vistaAlbums = this.view;
            if(!vistaAlbums.isSelected(target)){
                vistaAlbums.onClick(e);
            }
            var selNodes = vistaAlbums.getSelectedNodes();
            var dragData = {
                nodes: selNodes
            };
            if(selNodes.length == 1){
                dragData.ddel = target;
                dragData.single = true;
            }else{
                var div = document.createElement('div'); // create the multi element drag "ghost"
                div.className = 'multi-proxy';
                for(var i = 0, len = selNodes.length; i < len; i++){
                    div.appendChild(selNodes[i].firstChild.firstChild.cloneNode(true)); // image nodes only
                    if((i+1) % 3 == 0){
                        div.appendChild(document.createElement('br'));
                    }
                }
                var count = document.createElement('div'); // selected image count
                count.innerHTML = i + ' images selected';
                div.appendChild(count);

                dragData.ddel = div;
                dragData.multi = true;
            }
            return dragData;
        }
        return false;
    },

    // this method is called by the TreeDropZone after a node drop
    // to get the new tree node (there are also other way, but this is easiest)
    getTreeNode : function(){
        var treeNodes = [];
        var nodeData = this.view.getRecords(this.dragData.nodes);
        for(var i = 0, len = nodeData.length; i < len; i++){
            var data = nodeData[i].data;
            treeNodes.push(new Ext.tree.TreeNode({
                text: data.name,
                icon: data.url,
                data: data,
                leaf:true,
                cls: 'image-node'
            }));
        }
        return treeNodes;
    },

    // the default action is to "highlight" after a bad drop
    // but since an image can't be highlighted, let's frame it
    afterRepair:function(){
        for(var i = 0, len = this.dragData.nodes.length; i < len; i++){
            Ext.fly(this.dragData.nodes[i]).frame('#8db2e3', 1);
        }
        this.dragging = false;
    },

    // override the default repairXY with one offset for the margins and padding
    getRepairXY : function(e){
        if(!this.dragData.multi){
            var xy = Ext.Element.fly(this.dragData.ddel).getXY();
            xy[0]+=3;
            xy[1]+=3;
            return xy;
        }
        return false;
    }
});
