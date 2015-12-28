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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "grupo")
@NamedQueries({
   // Select usuario.* from usuario join grupo_usuario on (usuario.id=grupo_usuario.idUsuario) join grupo on grupo.id=grupo_usuario.idGrupo where grupo.idUsuario=24 and grupo.nombre='amigos'
    @NamedQuery(name = "Grupo.findAllAmigos", query = "SELECT u FROM Grupo g join g.usuarioCollection u where g.usuario.id=:usuarioActual and g.id=:idGrupo"),
    //@NamedQuery(name = "Grupo.findIdByNombreAndIdUsuaurio", query = "SELECT g.id FROM Grupo g WHERE g.nombre = :nombreGrupo AND g.usuario.id =:idUsuario"),
    @NamedQuery(name = "Grupo.findAllByIdUsuario", query = "SELECT g FROM Grupo g WHERE g.usuario.id = :idUsuario"),
    @NamedQuery(name = "Grupo.findAll", query = "SELECT g FROM Grupo g"),
    @NamedQuery(name = "Grupo.findById", query = "SELECT g FROM Grupo g WHERE g.id = :id"),
    @NamedQuery(name = "Grupo.findByNombre", query = "SELECT g FROM Grupo g WHERE g.nombre = :nombre"),
    @NamedQuery(name = "Grupo.findByDescripcion", query = "SELECT g FROM Grupo g WHERE g.descripcion = :descripcion")})
public class Grupo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinTable(name = "grupo_usuario", joinColumns = {
        @JoinColumn(name = "idGrupo", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "idUsuario", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Usuario> usuarioCollection;
    @JoinColumn(name = "idUsuario", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuario usuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "grupo")
    private Collection<PermisoMuro> permisoMuroCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "grupo")
    private Collection<PermisoAlbum> permisoAlbumCollection;

    public Grupo() {
    }

    public Grupo(Integer id) {
        this.id = id;
    }

    public Grupo(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Collection<Usuario> getUsuarioCollection() {
        return usuarioCollection;
    }

    public void setUsuarioCollection(Collection<Usuario> usuarioCollection) {
        this.usuarioCollection = usuarioCollection;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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
        if (!(object instanceof Grupo)) {
            return false;
        }
        Grupo other = (Grupo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Grupo[id=" + id + "]";
    }

}
