package gestionarPersistencia;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PermisoAlbumPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "idAlbum")
    private int idAlbum;
    @Basic(optional = false)
    @Column(name = "idPermiso")
    private int idPermiso;
    @Basic(optional = false)
    @Column(name = "idGrupo")
    private int idGrupo;

    public PermisoAlbumPK() {
    }

    public PermisoAlbumPK(int idAlbum, int idPermiso, int idGrupo) {
        this.idAlbum = idAlbum;
        this.idPermiso = idPermiso;
        this.idGrupo = idGrupo;
    }

    public int getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(int idAlbum) {
        this.idAlbum = idAlbum;
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
        hash += (int) idAlbum;
        hash += (int) idPermiso;
        hash += (int) idGrupo;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PermisoAlbumPK)) {
            return false;
        }
        PermisoAlbumPK other = (PermisoAlbumPK) object;
        if (this.idAlbum != other.idAlbum) {
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
        return "entidades.PermisoAlbumPK[idAlbum=" + idAlbum + ", idPermiso=" + idPermiso + ", idGrupo=" + idGrupo + "]";
    }

}
