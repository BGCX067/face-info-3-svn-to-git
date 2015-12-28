var perfilReader = new Ext.data.JsonReader ({
        root: 'data',
        fields: [
            'nombre','apellido', 'mail', 'fechaNac', 'ciudad-txt', 'localidad-txt', 'sexo-txt', 'telefono', 'celular', 'carrera'
        ]
    })

var formularioPerfil = new Ext.form.FormPanel({
    reader: perfilReader ,
    border:false,
    labelWidth: 90,
    autoWidth:true,
    id:'formularioPerfil',
    name: 'Perfil',
    defaults: {
        width: 160
    },
     items:[    
         {fieldLabel:'Nombre',name:'nombre',xtype:'textfield',vtype: 'alpha'},
         {fieldLabel:'Apellido',name:'apellido',xtype:'textfield',vtype: 'alpha'},
         {fieldLabel:'Mail',name:'mail',vtype: 'email',xtype:'textfield'},
         {fieldLabel: 'Fecha de Nacimiento', allowBlank: false, xtype:'datefield', name:'fechaNac'},
         texto_ciudad,
         texto_localidad,
         {
    fieldLabel:'Sexo',
    xtype:'radiogroup',
    autoWidth: true,
    columns:2,
    items:[
    {
        boxLabel: 'Masculino',
        id: 'sexo-txt',
        checked: true,
        inputValue: 0
    },
    {
        boxLabel: 'Femenino',
        id: 'sexo-txt' ,
        inputValue: 1
    }
    ]
},
         {fieldLabel:'Telefono',name:'telefono',vtype: 'telefono',xtype:'textfield'},
         {fieldLabel:'Celular',name:'celular',vtype: 'telefono',xtype:'textfield'},
         {fieldLabel:'Carrera',name:'carrera',xtype:'textfield',vtype: 'alpha'}

     ] 
});


//