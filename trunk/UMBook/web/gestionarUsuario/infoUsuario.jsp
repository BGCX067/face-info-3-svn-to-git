var logo = new Ext.Panel({
border:false,
region: 'west',
width: 350,
id: 'logo',
html: '<img width="50%" SRC="../Imagenes/logo.png" VSPACE=10 HSPACE=10>'
});

var fotoPerfil = new Ext.Panel({
border:false,
region: 'center',
id: 'fotoPerfil',
bodyStyle:'padding: 15px 10px 10px',
html: '<center><br><span style="font-size: 22px; font-weight: bold; color: #191763; font-family:cambria,tahoma,verdana;">Muro de ${usuarioActual.nombre} ${usuarioActual.apellido}</span></center>'
<!--<img align="center" width="50" height="50" SRC="./BajarFotos?idFoto=perfil" VSPACE=10">-->

});


var infoUsuario = new Ext.Panel({
border:false,
region: 'east',
id: 'infoUsuario',
buttons:
[
{text:'Salir de la sesi&oacute;n',
handler: function(){
window.location.href='/ServicioUsuario?accion=cerrarSesion';
}},
{text:'Volver a mi Muro',
handler: function(){
window.location.href='/ServicioUsuario?accion=volverMuro';
}}
]
,
html: '&nbsp<span style="font-size: 16px; font-family:cambria,tahoma,verdana; color: #191763; text-align: right"> <b>Usuario:</b> ${usuarioInvitado.nombre} ${usuarioInvitado.apellido}</span>'

});



