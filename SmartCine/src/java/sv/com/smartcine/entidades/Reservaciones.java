/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.com.smartcine.entidades;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author william.valdezfgkss
 */
@Entity
@Table(name = "reservaciones")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reservaciones.findAll", query = "SELECT r FROM Reservaciones r")
    , @NamedQuery(name = "Reservaciones.findById", query = "SELECT r FROM Reservaciones r WHERE r.id = :id")
    , @NamedQuery(name = "Reservaciones.findByCantidad", query = "SELECT r FROM Reservaciones r WHERE r.cantidad = :cantidad")
    , @NamedQuery(name = "Reservaciones.findByMetodoPago", query = "SELECT r FROM Reservaciones r WHERE r.metodoPago = :metodoPago")})
public class Reservaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "cantidad")
    private int cantidad;
    @Basic(optional = false)
    @Column(name = "metodo_pago")
    private String metodoPago;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idReservacion")
    private List<DetalleReservaciones> detalleReservacionesList;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios idUsuario;
    @JoinColumn(name = "id_funcion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Funciones idFuncion;
    @JoinColumn(name = "id_ventas", referencedColumnName = "id")
    @ManyToOne
    private Ventas idVentas;

    public Reservaciones() {
    }

    public Reservaciones(Long id) {
        this.id = id;
    }

    public Reservaciones(Long id, int cantidad, String metodoPago) {
        this.id = id;
        this.cantidad = cantidad;
        this.metodoPago = metodoPago;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    @XmlTransient
    public List<DetalleReservaciones> getDetalleReservacionesList() {
        return detalleReservacionesList;
    }

    public void setDetalleReservacionesList(List<DetalleReservaciones> detalleReservacionesList) {
        this.detalleReservacionesList = detalleReservacionesList;
    }

    public Usuarios getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuarios idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Funciones getIdFuncion() {
        return idFuncion;
    }

    public void setIdFuncion(Funciones idFuncion) {
        this.idFuncion = idFuncion;
    }

    public Ventas getIdVentas() {
        return idVentas;
    }

    public void setIdVentas(Ventas idVentas) {
        this.idVentas = idVentas;
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
        if (!(object instanceof Reservaciones)) {
            return false;
        }
        Reservaciones other = (Reservaciones) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.com.smartcine.entidades.Reservaciones[ id=" + id + " ]";
    }
    
}
