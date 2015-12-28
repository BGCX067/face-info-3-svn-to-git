var texto_mail= new Ext.form.TextField({
    fieldLabel: '*Mail',
    allowBlank: false,
    emptyText: "usuario@dominio.com",
    vtype: 'email',
    id: 'mail-txt'
})

var texto_clave1= new Ext.form.TextField({
    fieldLabel: '*Clave',
    allowBlank: false,
    inputType: 'password',
    id: 'clave1-txt'
})

var texto_clave2= new Ext.form.TextField({
    fieldLabel: '*Repetir Clave',
    allowBlank: false,
    inputType: 'password',
    id: 'clave2-txt'
})

var texto_nombre= new Ext.form.TextField({
    fieldLabel: '*Nombre',
    allowBlank: false,
    vtype:'alpha',
    id: 'nombre-txt'
})

var texto_apellido= new Ext.form.TextField({
    fieldLabel: '*Apellido',
    allowBlank: false,
    vtype:'alpha',
    id: 'apellido-txt'
})

var texto_cumple= new Ext.form.DateField({
    fieldLabel: '*Fecha de Nacimiento',
    allowBlank: false,
    id: 'cumple-txt'
})

//TIPO ESPECIAL PARA DEFINIR TELEFONOS -----------------------
Ext.apply(Ext.form.VTypes,{
    telefono: function(value,field){
        return value.replace(/[ \-\(\)]/g,'');
    },
    telefonoText: 'Numero de teléfono',
    telefonoMask: /[ \d\-\(\)]/
});

var texto_telefono= new Ext.form.TextField({
    fieldLabel: 'Telefono',
    id: 'telefono-txt',
    vtype: 'telefono'
})

var texto_celular= new Ext.form.TextField({
    fieldLabel: 'Celular',
    id: 'celular-txt',
    vtype: 'telefono'
})

var texto_carrera= new Ext.form.TextField({
    fieldLabel: 'Carrera',
    id: 'carrera-txt'
})

var texto_sexo = new Ext.form.RadioGroup({
    fieldLabel:'Sexo',
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
});

//CREO DOS STORES------------------------------------
var ciudadStore = new Ext.data.JsonStore({
    url : '/ServicioUsuario',
    baseParams: {
        accion: "cargarCiudad"
    },
    root:'ciudades',
    fields: ['id','nombre']
})

var localidadStore = new Ext.data.JsonStore({
    url : '/ServicioUsuario',
    baseParams: {
        accion: "cargarLocalidad"
    },
    root:'localidades',
    fields: ['id','nombre']
})

//CARGO LOS DOS STORES CREADOS-------------------------
var texto_ciudad = new Ext.form.ComboBox({
    store: ciudadStore,
    id: 'ciudad-txt',
    forceSelection:true,
    allowBlank: false,
    valueField: 'id',
     hiddenName: 'idCiudad',
    editable:false,
    displayField: 'nombre',
    triggerAction: 'all',
    mode: 'remote',
    emptyText: 'Seleccione una ciudad',
    fieldLabel: 'Ciudad'
});

var texto_localidad = new Ext.form.ComboBox({
    store: localidadStore,
    disabled: true,
    allowBlank: false,
    forceSelection:true,
    id: 'localidad-txt',
    hiddenName: 'idLocalidad',
    valueField: 'id',
    editable:false,
    displayField: 'nombre',
    triggerAction: 'all',
    mode: 'local',
    emptyText: 'Seleccione una localidad',
    fieldLabel: 'Localidad'
});