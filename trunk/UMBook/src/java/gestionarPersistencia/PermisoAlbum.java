package gestionarPersistencia;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "permiso_album")
@NamedQueries({
    @NamedQuery(name = "PermisoAlbum.findAll", query = "SELECT p FROM PermisoAlbum p"),
    @NamedQuery(name = "PermisoAlbum.findByIdAlbum", query = "SELECT p FROM PermisoAlbum p WHERE p.permisoAlbumPK.idAlbum = :idAlbum"),
    @NamedQuery(name = "PermisoAlbum.findByIdPermiso", query = "SELECT p FROM PermisoAlbum p WHERE p.permisoAlbumPK.idPermiso = :idPermiso"),
    @NamedQuery(name = "PermisoAlbum.findByIdGrupo", query = "SELECT p FROM PermisoAlbum p WHERE p.permisoAlbumPK.idGrupo = :idGrupo")})
public class PermisoAlbum implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PermisoAlbumPK permisoAlbumPK;
    @JoinColumn(name = "idGrupo", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Grupo grupo;
    @JoinColumn(name = "idPermiso", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Permiso permiso;
    @JoinColumn(name = "idAlbum", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Album album;

    public PermisoAlbum() {
    }

    public PermisoAlbum(PermisoAlbumPK permisoAlbumPK) {
        this.permisoAlbumPK = permisoAlbumPK;
    }

    public PermisoAlbum(int idAlbum, int idPermiso, int idGrupo) {
        this.permisoAlbumPK = new PermisoAlbumPK(idAlbum, idPermiso, idGrupo);
    }

    public PermisoAlbumPK getPermisoAlbumPK() {
        return permisoAlbumPK;
    }

    public void setPermisoAlbumPK(PermisoAlbumPK permisoAlbumPK) {
        this.permisoAlbumPK = permisoAlbumPK;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public Permiso getPermiso() {
        return permiso;
    }

    public void setPermiso(Permiso permiso) {
        this.permiso = permiso;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (permisoAlbumPK != null ? permisoAlbumPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PermisoAlbum)) {
            return false;
        }
        PermisoAlbum other = (PermisoAlbum) object;
        if ((this.permisoAlbumPK == null && other.permisoAlbumPK != null) || (this.permisoAlbumPK != null && !this.permisoAlbumPK.equals(other.permisoAlbumPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.PermisoAlbum[permisoAlbumPK=" + permisoAlbumPK + "]";
    }

}
