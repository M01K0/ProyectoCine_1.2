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

@Entity
@Table(name = "asientos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Asientos.findAll", query = "SELECT a FROM Asientos a")
    , @NamedQuery(name = "Asientos.findById", query = "SELECT a FROM Asientos a WHERE a.id = :id")
    , @NamedQuery(name = "Asientos.findByCodigo", query = "SELECT a FROM Asientos a WHERE a.codigo = :codigo")
    , @NamedQuery(name = "Asientos.findByEstado", query = "SELECT a FROM Asientos a WHERE a.estado = :estado")
    , @NamedQuery(name = "Asientos.findByIdSala", query = "SELECT a FROM Asientos a WHERE a.idSala.id = :idSala")
    , @NamedQuery(name = "AsientosUpdate", query = "UPDATE Asientos a SET a.estado = 'Ocupado' WHERE a.id = :id")})

public class Asientos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "codigo")
    private String codigo;
    @Basic(optional = false)
    @Column(name = "estado")
    private String estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idAsiento")
    private List<DetalleReservaciones> detalleReservacionesList;
    @JoinColumn(name = "id_sala", referencedColumnName = "id")
    @ManyToOne
    private Salas idSala;

    public Asientos() {
        idSala = new Salas();
    }

    public Asientos(Long id) {
        this.id = id;
    }

    public Asientos(Long id, String estado) {
        this.id = id;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @XmlTransient
    public List<DetalleReservaciones> getDetalleReservacionesList() {
        return detalleReservacionesList;
    }

    public void setDetalleReservacionesList(List<DetalleReservaciones> detalleReservacionesList) {
        this.detalleReservacionesList = detalleReservacionesList;
    }

    public Salas getIdSala() {
        return idSala;
    }

    public void setIdSala(Salas idSala) {
        this.idSala = idSala;
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
        if (!(object instanceof Asientos)) {
            return false;
        }
        Asientos other = (Asientos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.com.smartcine.entidades.Asientos[ id=" + id + " ]";
    }
    
}
