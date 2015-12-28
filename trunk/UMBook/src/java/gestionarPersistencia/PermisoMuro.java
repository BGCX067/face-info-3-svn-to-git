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
@Table(name = "permiso_muro")
@NamedQueries({
    @NamedQuery(name = "PermisoMuro.findAll", query = "SELECT p FROM PermisoMuro p"),
    @NamedQuery(name = "PermisoMuro.findByIdUsuario", query = "SELECT p FROM PermisoMuro p WHERE p.permisoMuroPK.idUsuario = :idUsuario"),
    @NamedQuery(name = "PermisoMuro.findByIdPermiso", query = "SELECT p FROM PermisoMuro p WHERE p.permisoMuroPK.idPermiso = :idPermiso"),
    @NamedQuery(name = "PermisoMuro.findByIdGrupo", query = "SELECT p FROM PermisoMuro p WHERE p.permisoMuroPK.idGrupo = :idGrupo")})
public class PermisoMuro implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PermisoMuroPK permisoMuroPK;
    @JoinColumn(name = "idGrupo", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Grupo grupo;
    @JoinColumn(name = "idPermiso", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Permiso permiso;
    @JoinColumn(name = "idUsuario", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Usuario usuario;

    public PermisoMuro() {
    }

    public PermisoMuro(PermisoMuroPK permisoMuroPK) {
        this.permisoMuroPK = permisoMuroPK;
    }

    public PermisoMuro(int idUsuario, int idPermiso, int idGrupo) {
        this.permisoMuroPK = new PermisoMuroPK(idUsuario, idPermiso, idGrupo);
    }

    public PermisoMuroPK getPermisoMuroPK() {
        return permisoMuroPK;
    }

    public void setPermisoMuroPK(PermisoMuroPK permisoMuroPK) {
        this.permisoMuroPK = permisoMuroPK;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (permisoMuroPK != null ? permisoMuroPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PermisoMuro)) {
            return false;
        }
        PermisoMuro other = (PermisoMuro) object;
        if ((this.permisoMuroPK == null && other.permisoMuroPK != null) || (this.permisoMuroPK != null && !this.permisoMuroPK.equals(other.permisoMuroPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.PermisoMuro[permisoMuroPK=" + permisoMuroPK + "]";
    }

}
