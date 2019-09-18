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
import sv.com.smartcine.dao.SalasJpaController;
import sv.com.smartcine.entidades.Salas;
import sv.com.smartcine.entidades.Sucursales;

/**
 *
 * @author william.valdezfgkss
 */
@ManagedBean(name = "salas")
@RequestScoped
public class ControladorSalas {
    SalasJpaController salaDAO;
    private Salas sala;
    private Sucursales sucu;

    public ControladorSalas() {
        salaDAO = new SalasJpaController(Persistence.createEntityManagerFactory("SmartCinePU"));
        sala = new Salas();
    }
    
    public List<Salas> listar(){
        return salaDAO.findSalasEntities();
    }
    
    public String ingresar(){
        salaDAO.create(sala);
        return "listar?faces-redirect=true";
    }
    
    //
    public String editar(Salas s){
        Map<String, Object> objetos = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        objetos.put("sal", s);
        return "editar?faces-redirect=true"; 
    }
    
    public String actualizar(Salas s){
        try {
            salaDAO.edit(s);
            return "listar?faces-redirect=true";
        } catch (Exception e) {
            return null;
        }
    }
    
    public String ver(Salas s){
        sala = s;
        return "ver?faces-redirect=true";
    }
    
    public String destruir(Salas s){
        try {
            salaDAO.destroy(s.getId());
        return "listar?faces-redirect=true";
        } catch (Exception e) {
            return null;
        }
    }
    
    public Salas getSala() {
        return sala;
    }

    public void setSala(Salas sala) {
        this.sala = sala;
    }
    
    public Sucursales getSucu() {
        return sucu;
    }

    public void setSucu(Sucursales sucu) {
        this.sucu = sucu;
    }

}
