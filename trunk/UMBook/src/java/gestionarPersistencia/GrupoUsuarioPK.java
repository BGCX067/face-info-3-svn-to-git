/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionarPersistencia;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class GrupoUsuarioPK implements Serializable
{
    @Basic(optional = false)
    @Column(name = "idGrupo")
    private int idGrupo;
    @Basic(optional = false)
    @Column(name = "idUsuario")
    private int idUsuario;

  
    public GrupoUsuarioPK()
    {
    }

    public GrupoUsuarioPK(int idGrupo, int idUsuario)
    {
        this.idGrupo = idGrupo;
        this.idUsuario = idUsuario;
    }

    public int getIdGrupo()
    {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo)
    {
        this.idGrupo = idGrupo;
    }

    public int getIdUsuario()
    {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario)
    {
        this.idUsuario = idUsuario;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (int) idGrupo;
        hash += (int) idUsuario;
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GrupoUsuarioPK))
        {
            return false;
        }
        GrupoUsuarioPK other = (GrupoUsuarioPK) object;
        if (this.idGrupo != other.idGrupo)
            return false;
        if (this.idUsuario != other.idUsuario)
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "gestionarPersistencia.GrupoUsuarioPK[ idGrupo=" + idGrupo + ", idUsuario=" + idUsuario + " ]";
    }
}

    