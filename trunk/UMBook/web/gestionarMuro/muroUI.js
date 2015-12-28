Ext.BLANK_IMAGE_URL = '../ext-3.3.0/resources/images/default/s.gif';
Ext.ns('umbook.muro');

umbook.muro.PanelGeneralMuro = {
    init: function() {
        this.MiMuro = new Ext.Viewport( {
            title: 'Muro',
            layout: 'border',
            id: 'muroUI',
            items : [
            {
                xtype: 'panel',
                title: 'Noticias',
                region: 'east',
                width: 200
            },
            {
                xtype: 'panel',     //PANEL SUPERIOR ---
                region: 'north',
                layout:'border',
                bodyStyle: 'background-color: white;',
                defaults:
                {
                    bodyStyle: 'background-color: white;'
                }
                ,
                items:[infoUsuario,logo,fotoPerfil],
                height: 70,
                id: 'Banner'

            },
            {
                xtype: 'panel',
                region: 'south',
                height: 50,
                id: 'Info'
            },
            {
                xtype: 'tabpanel',
                activeTab: 0,           //panel activo!!!
                region: 'center',
                id: 'Cuerpo',
                items: [
                {
                    xtype: 'panel',
                    title: 'Comentarios',
                    id: 'comentarioInicio',
                    layout:'border',
                    items:[{
                        region: 'center',
                        autoWidth: true,
                        items:[comentarioGrid]
                    },
                    {
                        region: 'south',
                        autoHeight:true,
                        items:[paginadorComentarios]
                    },

                    ]
                },
                {
                    xtype: 'panel',
                    id:'images',
                    title: 'Fotos',
                    tbar:toolbarFotos,
                    items:[new Ext.DataView(vistaAlbums)]
                },
                {
                    xtype: 'panel',
                    id:'amigos',
                    title: 'Amigos',
                    layout:'border',
                    items:[{
                        region: 'center',
                        autoWidth: true,
                        items:[amigosGrid]
                    },

                    {
                        region: 'south',
                        autoHeight:true,
                        items:[paginadorAmigos]
                    },
                    ]
                }, {
                    xtype: 'panel',
                    id:'perfil',
                    title: 'Perfil',
                    tbar:toolbarPerfil,
                    items:[formularioPerfil]
                },
                ]
            },
            {
                title: 'Perfil', //--- TREE PANEL-------------------
                region: 'west',
                width: 200,
                id: 'PanelPerfil',
                tbar:toolbarTreePanel,
                items:[arbolAlbum, arbolGrupo]
            }
            ]
       
        });
        Ext.getCmp('perfil').on('show',function(_this){
            Ext.getCmp('formularioPerfil').getForm().load(
            {
                url:'/ServicioUsuario',
                params: {
                        accion:"cargarPerfil"
                    }
            });
        });
        Ext.getCmp('comentarioInicio').on('show',function(_this){
            comentarioGrid.getStore().load();
        });
    }

}

Ext.onReady(umbook.muro.PanelGeneralMuro.init,umbook.muro.PanelGeneralMuro);
