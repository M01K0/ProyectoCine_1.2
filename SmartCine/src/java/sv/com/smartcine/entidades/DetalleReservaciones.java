/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.com.smartcine.entidades;

import java.io.Serializable;
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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author william.valdezfgkss
 */
@Entity
@Table(name = "detalle_reservaciones")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetalleReservaciones.findAll", query = "SELECT d FROM DetalleReservaciones d")
    , @NamedQuery(name = "DetalleReservaciones.findById", query = "SELECT d FROM DetalleReservaciones d WHERE d.id = :id")})
public class DetalleReservaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "id_asiento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Asientos idAsiento;
    @JoinColumn(name = "id_reservacion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Reservaciones idReservacion;

    public DetalleReservaciones() {
    }

    public DetalleReservaciones(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Asientos getIdAsiento() {
        return idAsiento;
    }

    public void setIdAsiento(Asientos idAsiento) {
        this.idAsiento = idAsiento;
    }

    public Reservaciones getIdReservacion() {
        return idReservacion;
    }

    public void setIdReservacion(Reservaciones idReservacion) {
        this.idReservacion = idReservacion;
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
        if (!(object instanceof DetalleReservaciones)) {
            return false;
        }
        DetalleReservaciones other = (DetalleReservaciones) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.com.smartcine.entidades.DetalleReservaciones[ id=" + id + " ]";
    }
    
}
