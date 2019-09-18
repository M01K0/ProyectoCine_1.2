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

/**
 *
 * @author william.valdezfgkss
 */
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

    public List<Peliculas> listar() {
        return pelDAO.findPeliculasEntities();
    }

    public String ingresar() {
        pelDAO.create(pel);
        return "index?faces-redirect=true";
    }

    //
    public String editar(Peliculas p) {
        Map<String, Object> objetos = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        objetos.put("ps", p);
        return "editar?faces-redirect=true";
    }

    public String actualizar(Peliculas p) {
        try {
            pelDAO.edit(p);
            return "index?faces-redirect=true";
        } catch (Exception e) {

            return null;
        }
    }

    public String ver(Peliculas p) {
        pel = p;
        return "ver?faces-redirect=true";
    }

    public String destruir(Peliculas p) {
        try {
            pelDAO.destroy(p.getId());
            return "index?faces-redirect=true";
        } catch (Exception e) {
            return null;
        }
    }

    public String mostrar(Peliculas u) {
        Map<String, Object> objetos = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        objetos.put("pel", u);
        return "/faces/recursos/cartelera/pelicula?faces-redirect=true";
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
