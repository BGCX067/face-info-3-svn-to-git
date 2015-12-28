amigosStore.load({
    params:{
        start:0,
        limit:20
    }
});

//---GRILLA DE AMIGOS----------------------------------------------------------
var amigosGrid = new Ext.grid.GridPanel({
    autoHeight:true,
    autoWidth: true,
    stripeRows: true,
    autoScroll: true,
    id:'grillaAmigos',
    sm: checkAmigos,
    store:amigosStore,
    tbar: new Ext.Toolbar(toolbarAmigos),
    viewConfig:{
        forceFit:true
    },
    columns : [
    checkAmigos,
    {
        xtype: 'gridcolumn',
        dataIndex: 'nombre',
        header: 'Nombre',
        sortable: true
    },
    {
        xtype: 'gridcolumn',
        dataIndex: 'apellido',
        width: 450,
        header: 'Apellido',
        sortable: true
    }
    ,
    {
        xtype: 'gridcolumn',
        dataIndex: 'mail',
        width: 450,
        header: 'Mail',
        sortable: true
    }
    ]     
});
