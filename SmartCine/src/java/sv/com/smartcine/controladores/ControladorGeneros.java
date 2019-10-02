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
import sv.com.smartcine.dao.GeneroJpaController;
import sv.com.smartcine.entidades.Genero;

@ManagedBean(name = "genero")
@RequestScoped
public class ControladorGeneros {

    GeneroJpaController generoDAO;
    private Genero gener;

    public ControladorGeneros() {
        generoDAO = new GeneroJpaController(Persistence.createEntityManagerFactory("SmartCinePU"));
        gener = new Genero();
        
    }
    
    public List<Genero> listar(){
        return generoDAO.findGeneroEntities();
    }
    
    public String ingresar(){
        generoDAO.create(gener);
        return "listar?faces-redirect=true";
    }
    
    //
    public String editar(Genero g){
        Map<String, Object> objetos = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        objetos.put("gn", g);
        return "editar?faces-redirect=true"; 
    }
    
    public String actualizar(Genero g){
        try {
            generoDAO.edit(g);
            return "listar?faces-redirect=true";
        } catch (Exception e) {
            
            return null;
        }
    }
    
    public String ver(Genero g){
        gener = g;
        return "ver?faces-redirect=true";
    }
    
    public String destruir(Genero g){
        try {
            generoDAO.destroy(g.getId());
        return "listar?faces-redirect=true";
        } catch (Exception e) {
            return null;
        }
    }

   public Genero getGener() {
        return gener;
    }

    public void setGener(Genero gener) {
        this.gener = gener;
    }
    
}
