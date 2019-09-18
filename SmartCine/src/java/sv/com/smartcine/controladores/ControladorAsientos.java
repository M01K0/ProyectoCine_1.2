/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.com.smartcine.controladores;

import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Persistence;
import sv.com.smartcine.dao.AsientosJpaController;
import sv.com.smartcine.entidades.Asientos;
import sv.com.smartcine.entidades.Salas;


/**
 *
 * @author HP
 */
@ManagedBean(name = "asientos")
@RequestScoped
public class ControladorAsientos {

    AsientosJpaController asientoDAO;
    private Asientos asient;
    private Salas sal;

    
    public ControladorAsientos() {
        asientoDAO = new AsientosJpaController(Persistence.createEntityManagerFactory("SmartCinePU"));
        asient = new Asientos();
    }
    
    
    public List<Asientos> listar(){
        return asientoDAO.findAsientosEntities();
    }
    
    public String ingresar(){
        asientoDAO.create(asient);
        return "listar?faces-redirect=true";
    }
    
    
    public String editar(Asientos a){
        Map<String, Object> objetos = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        objetos.put("as", a);
        return "editar?faces-redirect=true"; 
    }
    
    public String actualizar(Asientos a){
        try {
            asientoDAO.edit(a);
            return "listar?faces-redirect=true";
        } catch (Exception e) {
            
            return null;
        }
    }
    
    public String ver(Asientos a){
        asient = a;
        return "ver?faces-redirect=true";
    }
    
    public String destruir(Asientos a){
        try {
            asientoDAO.destroy(a.getId());
        return "listar?faces-redirect=true";
        } catch (Exception e) {
            return null;
        }
    }
    
    public Asientos getAsient() {
        return asient;
    }

    public void setAsient(Asientos asient) {
        this.asient = asient;
    }
    
    public Salas getSal() {
        return sal;
    }

    public void setSal(Salas sal) {
        this.sal = sal;
    }

}
