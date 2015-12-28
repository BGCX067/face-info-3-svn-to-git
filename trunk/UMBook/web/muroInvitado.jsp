if (${usuarioActual.id}!=${usuarioInvitado.id}){

    if(Ext.getCmp('toolbarAmigos'))
    {
        Ext.getCmp('toolbarAmigos').destroy();
    }
    if(Ext.getCmp('toolbarFotos'))
    {
        Ext.getCmp('toolbarFotos').destroy();
    }
    if(Ext.getCmp('toolbarTreePanel'))
    {
        Ext.getCmp('toolbarTreePanel').destroy();
    }
    if(Ext.getCmp('comentarioGrid'))
    {
        Ext.getCmp('comentarioGrid').getColumnModel().getColumnById('checkComentarios').destroy();
    }
    if(Ext.getCmp('toolbarPerfil'))
    {
        Ext.getCmp('toolbarPerfil').destroy();
    }
}

/*
LOGICA DE SESIONES:

Cuando un Usuario se Loguea, se crean 3 variables de sesiones:

- sesionIniciada: Por defecto es TRUE. Sirve para validar en todas las páginas.

- usuarioInvitado: Es el usuario que se acaba de Loguear, este usuario se MANTIENE IGUAL durante toda la sesión.

- usuarioActual: Se crea por defecto IGUAL a usuarioIvitado. 
Cuando el usuario visita el muro de otro usuario, esta variable toma el valor del Usuario que se está visitando.

--------------------------------------------------------------------------------------------------------------------

Si (usuarioActual != usuarioInvitado) significa que estamos visitando el muro de otra persona. 
Por lo tanto se eliminan todas las toolbar del usuarioInvitado y se cargan las toolbar del usuarioActual.
*/