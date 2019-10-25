/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.com.smartcine.controladores;

import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Persistence;
import sv.com.smartcine.dao.PeliculasJpaController;
import sv.com.smartcine.entidades.Clasificaciones;
import sv.com.smartcine.entidades.Genero;
import sv.com.smartcine.entidades.Peliculas;

@ManagedBean(name = "peliculas")
@RequestScoped
public class CotroladorPeliculas {

    PeliculasJpaController pelDAO;
    private Peliculas pel;
    private int id;
    private Genero gener;
    private Clasificaciones clasif;

    public CotroladorPeliculas() {
        pelDAO = new PeliculasJpaController(Persistence.createEntityManagerFactory("SmartCinePU"));
        pel = new Peliculas();
    }

    // Metodo para listar Peliculas
    public List<Peliculas> listar() {
        return pelDAO.findPeliculasEntities();
    }

    // Metodo para ingresar Peliculas
    public String ingresar() {
        pelDAO.create(pel);
        return "listar?faces-redirect=true";
    }

    // Metodo para mapear Peliculas
    public String editar(Peliculas p) {
        Map<String, Object> objetos = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        objetos.put("ps", p);
        return "editar?faces-redirect=true";
    }

    // Metodo para editar Peliculas
    public String actualizar(Peliculas p) {
        try {
            pelDAO.edit(p);
            return "listar?faces-redirect=true";
        } catch (Exception e) {

            return null;
        }
    }

    // Metodo para eliminar Peliculas
    public String destruir(Peliculas p) {
        try {
            pelDAO.destroy(p.getId());
            return "listar?faces-redirect=true";
        } catch (Exception e) {
            return null;
        }
    }
    
    // Metodo para mapear Peliculas
    public String mostrarPel(Peliculas p) {
        Map<String, Object> objetos = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        objetos.put("pel", p);
        
        return "/faces/recursos/reserva/InfoReserva?faces-redirect=true";
    }
    
    // Metodo para mapear Peliculas
    public String mostrarPely(Peliculas p) {
        Map<String, Object> objetos = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        objetos.put("pely", p);
        
        return "/faces/recursos/reserva/reservacion?faces-redirect=true";
    }

    public Peliculas listPe() {
        id = getId();
        return pelDAO.porid(id);
    }

    public Peliculas getPel() {
        return pel;
    }

    public void setPel(Peliculas pel) {
        this.pel = pel;
    }

    public Genero getGener() {
        return gener;
    }

    public void setGener(Genero gener) {
        this.gener = gener;
    }

    public Clasificaciones getClasif() {
        return clasif;
    }

    public void setClasif(Clasificaciones clasif) {
        this.clasif = clasif;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    

}
