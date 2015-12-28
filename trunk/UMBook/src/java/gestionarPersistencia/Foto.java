package gestionarPersistencia;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "foto")
@NamedQueries({
   @NamedQuery(name = "Foto.findAllByIdAlbum", query = "SELECT f FROM Foto f WHERE f.album.id = :idAlbum"),
    @NamedQuery(name = "Foto.findAll", query = "SELECT f FROM Foto f"),
    @NamedQuery(name = "Foto.findById", query = "SELECT f FROM Foto f WHERE f.id = :id"),
    @NamedQuery(name = "Foto.findByDescripcion", query = "SELECT f FROM Foto f WHERE f.descripcion = :descripcion"),
    @NamedQuery(name = "Foto.findByUbicacion", query = "SELECT f FROM Foto f WHERE f.ubicacion = :ubicacion")})
public class Foto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "ubicacion")
    private String ubicacion;
    @OneToMany(mappedBy = "foto")
    private Collection<Usuario> usuarioCollection;
    @JoinColumn(name = "idAlbum", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Album album;

    public Foto() {
    }

        public Foto(String ubicacion,String descripcion) {
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Collection<Usuario> getUsuarioCollection() {
        return usuarioCollection;
    }

    public void setUsuarioCollection(Collection<Usuario> usuarioCollection) {
        this.usuarioCollection = usuarioCollection;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Foto)) {
            return false;
        }
        Foto other = (Foto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Foto[id=" + id + "]";
    }

}
