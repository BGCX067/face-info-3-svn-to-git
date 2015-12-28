var cargarAlbums = new Ext.tree.TreeLoader({
    url: '/ServicioFotos',
    baseParams:{
        accion:'cargarArbolAlbums'
    }
});


//--ARBOL DE ALBUMS-------------------------------------------------------------
var arbolAlbum = new Ext.tree.TreePanel({
    loader: cargarAlbums,
   
    root: new Ext.tree.AsyncTreeNode({
        text: 'Albums',
        id: 'todos',
        expanded: true,
        listeners:{
            click:function(n){
                storeAlbum.load();
                if(Ext.getCmp('vistaFotos')){
           
                    Ext.getCmp('vistaFotos').destroy();
                    new Ext.DataView(vistaAlbums);
                    Ext.getCmp('images').add('vistaAlbums');
                    Ext.getCmp('images').doLayout();
                }
               
            }
        }

    }),
    listeners: {
        click: function(n) {
            if(n.id!='todos'){
                storeFotos.baseParams={
                    accion:'buscarFotos',
                    idAlbum:n.id
                }
                storeFotos.load();

                if(Ext.getCmp('vistaAlbums')){
                    Ext.getCmp('vistaAlbums').destroy();
                    new Ext.DataView(vistaFotos);
                    Ext.getCmp('images').add('vistaFotos');
                    Ext.getCmp('images').doLayout();
                }
            
            }
            Ext.getCmp('Cuerpo').setActiveTab('images')
        }
    }
    
});

