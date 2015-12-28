package gestionarPersistencia;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "permiso")
@NamedQueries({
    @NamedQuery(name = "Permiso.findAll", query = "SELECT p FROM Permiso p"),
    @NamedQuery(name = "Permiso.findById", query = "SELECT p FROM Permiso p WHERE p.id = :id"),
    @NamedQuery(name = "Permiso.findByTipo", query = "SELECT p FROM Permiso p WHERE p.tipo = :tipo"),
    @NamedQuery(name = "Permiso.findByAcceso", query = "SELECT p FROM Permiso p WHERE p.acceso = :acceso")})
public class Permiso implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "tipo")
    private String tipo;
    @Basic(optional = false)
    @Column(name = "acceso")
    private String acceso;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "permiso")
    private Collection<PermisoMuro> permisoMuroCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "permiso")
    private Collection<PermisoAlbum> permisoAlbumCollection;

    public Permiso() {
    }

    public Permiso(Integer id) {
        this.id = id;
    }

    public Permiso(Integer id, String tipo, String acceso) {
        this.id = id;
        this.tipo = tipo;
        this.acceso = acceso;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getAcceso() {
        return acceso;
    }

    public void setAcceso(String acceso) {
        this.acceso = acceso;
    }

    public Collection<PermisoMuro> getPermisoMuroCollection() {
        return permisoMuroCollection;
    }

    public void setPermisoMuroCollection(Collection<PermisoMuro> permisoMuroCollection) {
        this.permisoMuroCollection = permisoMuroCollection;
    }

    public Collection<PermisoAlbum> getPermisoAlbumCollection() {
        return permisoAlbumCollection;
    }

    public void setPermisoAlbumCollection(Collection<PermisoAlbum> permisoAlbumCollection) {
        this.permisoAlbumCollection = permisoAlbumCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Permiso)) {
            return false;
        }
        Permiso other = (Permiso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Permiso[id=" + id + "]";
    }

}
