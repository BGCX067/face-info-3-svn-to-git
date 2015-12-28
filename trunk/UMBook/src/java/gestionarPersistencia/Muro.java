package gestionarPersistencia;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

import javax.persistence.Table;

@Entity
@Table(name = "muro")
//ESTO ANDA: "SELECT m FROM Muro m JOIN m.idComentario c WHERE m.usuario.id= :usuarioActual"
  // JOIN m.comentario.usuario.id a
@NamedQueries({
//@NamedQuery(name = "Muro.buscarTodosComentarios", query = "SELECT c,u.nombre FROM Muro m join c.idUsuario u join m.comentario c WHERE u.id=:usuarioActual"),

    @NamedQuery(name = "Muro.buscarTodosComentarios",
    query = "SELECT c.id, c.fecha, c.descripcion, u.nombre, u.apellido FROM Muro m JOIN m.comentario c JOIN c.usuario u WHERE m.usuario.id= :usuarioActual"),
    @NamedQuery(name = "Muro.findAll", query = "SELECT m FROM Muro m"),
    @NamedQuery(name = "Muro.findByIdComentario", query = "SELECT m FROM Muro m WHERE m.idComentario = :idComentario")})
public class Muro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idComentario")
    private Integer idComentario;
    @JoinColumn(name = "idComentario", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Comentario comentario;
    @JoinColumn(name = "idUsuario", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuario usuario;

    public Muro() {
    }

    public Muro(Integer idComentario) {
        this.idComentario = idComentario;
    }

    public Muro(Comentario comentario, Usuario usuario) {
        this.comentario = comentario;
        this.usuario = usuario;
    }

    public Integer getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(Integer idComentario) {
        this.idComentario = idComentario;
    }

    public Comentario getComentario() {
        return comentario;
    }

    public void setComentario(Comentario comentario) {
        this.comentario = comentario;
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
        hash += (idComentario != null ? idComentario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Muro)) {
            return false;
        }
        Muro other = (Muro) object;
        if ((this.idComentario == null && other.idComentario != null) || (this.idComentario != null && !this.idComentario.equals(other.idComentario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Muro[idComentario=" + idComentario + "]";
    }
}
