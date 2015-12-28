package gestionarPersistencia;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PermisoMuroPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "idUsuario")
    private int idUsuario;
    @Basic(optional = false)
    @Column(name = "idPermiso")
    private int idPermiso;
    @Basic(optional = false)
    @Column(name = "idGrupo")
    private int idGrupo;

    public PermisoMuroPK() {
    }

    public PermisoMuroPK(int idUsuario, int idPermiso, int idGrupo) {
        this.idUsuario = idUsuario;
        this.idPermiso = idPermiso;
        this.idGrupo = idGrupo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdPermiso() {
        return idPermiso;
    }

    public void setIdPermiso(int idPermiso) {
        this.idPermiso = idPermiso;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idUsuario;
        hash += (int) idPermiso;
        hash += (int) idGrupo;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PermisoMuroPK)) {
            return false;
        }
        PermisoMuroPK other = (PermisoMuroPK) object;
        if (this.idUsuario != other.idUsuario) {
            return false;
        }
        if (this.idPermiso != other.idPermiso) {
            return false;
        }
        if (this.idGrupo != other.idGrupo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.PermisoMuroPK[idUsuario=" + idUsuario + ", idPermiso=" + idPermiso + ", idGrupo=" + idGrupo + "]";
    }

}
