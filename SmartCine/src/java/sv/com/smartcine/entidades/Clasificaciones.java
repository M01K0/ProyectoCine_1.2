/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.com.smartcine.entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "clasificaciones")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Clasificaciones.findAll", query = "SELECT c FROM Clasificaciones c")
    , @NamedQuery(name = "Clasificaciones.findById", query = "SELECT c FROM Clasificaciones c WHERE c.id = :id")
    , @NamedQuery(name = "Clasificaciones.findByNombre", query = "SELECT c FROM Clasificaciones c WHERE c.nombre = :nombre")
    , @NamedQuery(name = "Clasificaciones.findByEdadMinima", query = "SELECT c FROM Clasificaciones c WHERE c.edadMinima = :edadMinima")
    , @NamedQuery(name = "Clasificaciones.findByDescripcion", query = "SELECT c FROM Clasificaciones c WHERE c.descripcion = :descripcion")})
public class Clasificaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "edad_minima")
    private int edadMinima;
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(mappedBy = "idClasificacion")
    private List<Peliculas> peliculasList;

    public Clasificaciones() {
    }

    public Clasificaciones(Long id) {
        this.id = id;
    }

    public Clasificaciones(Long id, String nombre, int edadMinima) {
        this.id = id;
        this.nombre = nombre;
        this.edadMinima = edadMinima;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdadMinima() {
        return edadMinima;
    }

    public void setEdadMinima(int edadMinima) {
        this.edadMinima = edadMinima;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<Peliculas> getPeliculasList() {
        return peliculasList;
    }

    public void setPeliculasList(List<Peliculas> peliculasList) {
        this.peliculasList = peliculasList;
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
        if (!(object instanceof Clasificaciones)) {
            return false;
        }
        Clasificaciones other = (Clasificaciones) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.com.smartcine.entidades.Clasificaciones[ id=" + id + " ]";
    }
    
}
