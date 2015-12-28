var cargarGrupos = new Ext.tree.TreeLoader({
    url: '/ServicioAmigos',
    baseParams:{
        accion:'cargarArbolGrupos'
    }
});


//--ARBOL DE GRUPOS-------------------------------------------------------------
var arbolGrupo = new Ext.tree.TreePanel({
    loader: cargarGrupos,
    root: new Ext.tree.AsyncTreeNode({
        expanded: true,
        text: 'Grupos',
        id: 'todos'
    }),
    listeners: {
        click: function(n) {
            Ext.getCmp('grillaAmigos').getStore().baseParams={
                accion: 'cargarAmigos',
                idGrupo: n.id
            };
            if(Ext.getCmp('toolbarBuscarAmigos')){
                volverAmigos();
            }
            Ext.getCmp('Cuerpo').setActiveTab('amigos')
            Ext.getCmp('grillaAmigos').getStore().load();
        }
    }
});