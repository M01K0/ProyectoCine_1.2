/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.com.smartcine.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "funciones")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Funciones.findAll", query = "SELECT f FROM Funciones f")
    , @NamedQuery(name = "Funciones.findById", query = "SELECT f FROM Funciones f WHERE f.id = :id")
    , @NamedQuery(name = "Funciones.findByFechaHoraInicio", query = "SELECT f FROM Funciones f WHERE f.fechaHoraInicio = :fechaHoraInicio")
    , @NamedQuery(name = "Funciones.findByFechaHoraFin", query = "SELECT f FROM Funciones f WHERE f.fechaHoraFin = :fechaHoraFin")
    , @NamedQuery(name = "Funciones.findByFormato", query = "SELECT f FROM Funciones f WHERE f.formato = :formato")
    , @NamedQuery(name = "Funciones.findByPrecio", query = "SELECT f FROM Funciones f WHERE f.precio = :precio")})
public class Funciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "fecha_hora_inicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraInicio;
    @Column(name = "fecha_hora_fin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraFin;
    @Basic(optional = false)
    @Column(name = "formato")
    private String formato;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "precio")
    private BigDecimal precio;
    @JoinColumn(name = "id_pelicula", referencedColumnName = "id")
    @ManyToOne
    private Peliculas idPelicula;
    @JoinColumn(name = "id_sala", referencedColumnName = "id")
    @ManyToOne
    private Salas idSala;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idFuncion")
    private List<Reservaciones> reservacionesList;

    public Funciones() {
        idPelicula = new Peliculas();
        idSala = new Salas();
    }

    public Funciones(Long id) {
        this.id = id;
    }

    public Funciones(Long id, String formato, BigDecimal precio) {
        this.id = id;
        this.formato = formato;
        this.precio = precio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(Date fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public Date getFechaHoraFin() {
        return fechaHoraFin;
    }

    public void setFechaHoraFin(Date fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Peliculas getIdPelicula() {
        return idPelicula;
    }

    public void setIdPelicula(Peliculas idPelicula) {
        this.idPelicula = idPelicula;
    }

    public Salas getIdSala() {
        return idSala;
    }

    public void setIdSala(Salas idSala) {
        this.idSala = idSala;
    }

    @XmlTransient
    public List<Reservaciones> getReservacionesList() {
        return reservacionesList;
    }

    public void setReservacionesList(List<Reservaciones> reservacionesList) {
        this.reservacionesList = reservacionesList;
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
        if (!(object instanceof Funciones)) {
            return false;
        }
        Funciones other = (Funciones) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.com.smartcine.entidades.Funciones[ id=" + id + " ]";
    }

}
