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
import sv.com.smartcine.dao.SucursalesJpaController;
import sv.com.smartcine.entidades.Sucursales;

/**
 *
 * @author william.valdezfgkss
 */
@ManagedBean(name = "sucursales")
@RequestScoped
public class ControladorSucursales {
    SucursalesJpaController suDAO;
    private Sucursales sucu;

    public ControladorSucursales() {
        suDAO = new SucursalesJpaController(Persistence.createEntityManagerFactory("SmartCinePU"));
        sucu = new Sucursales();
    }
    
    public List<Sucursales> listar(){
        return suDAO.findSucursalesEntities();
    }
    
    public String ingresar(){
        suDAO.create(sucu);
        return "listar?faces-redirect=true";
    }
    
    //
    public String editar(Sucursales s){
        Map<String, Object> objetos = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        objetos.put("sc", s);
        return "editar?faces-redirect=true"; 
    }
    
    public String actualizar(Sucursales s){
        try {
            suDAO.edit(s);
            return "listar?faces-redirect=true";
        } catch (Exception e) {
            return null;
        }
    }
    
    public String ver(Sucursales s){
        sucu = s;
        return "ver?faces-redirect=true";
    }
    
    public String destruir(Sucursales s){
        try {
            suDAO.destroy(s.getId());
        return "listar?faces-redirect=true";
        } catch (Exception e) {
            return null;
        }
    }

    public Sucursales getSucu() {
        return sucu;
    }

    public void setSucu(Sucursales sucu) {
        this.sucu = sucu;
    }
    
}
