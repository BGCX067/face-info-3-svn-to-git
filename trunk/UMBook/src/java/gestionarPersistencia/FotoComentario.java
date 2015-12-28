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
@Table(name = "foto_comentario")
@NamedQueries({
    @NamedQuery(name = "FotoComentario.findAll", query = "SELECT f FROM FotoComentario f"),
    @NamedQuery(name = "FotoComentario.findByIdComentario", query = "SELECT f FROM FotoComentario f WHERE f.idComentario = :idComentario")})
public class FotoComentario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idComentario")
    private Integer idComentario;
    @JoinColumn(name = "idComentario", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Comentario comentario;
    @JoinColumn(name = "idFoto", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Foto foto;

    public FotoComentario() {
    }

    public FotoComentario(Integer idComentario) {
        this.idComentario = idComentario;
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

    public Foto getFoto() {
        return foto;
    }

    public void setFoto(Foto foto) {
        this.foto = foto;
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
        if (!(object instanceof FotoComentario)) {
            return false;
        }
        FotoComentario other = (FotoComentario) object;
        if ((this.idComentario == null && other.idComentario != null) || (this.idComentario != null && !this.idComentario.equals(other.idComentario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.FotoComentario[idComentario=" + idComentario + "]";
    }

}
