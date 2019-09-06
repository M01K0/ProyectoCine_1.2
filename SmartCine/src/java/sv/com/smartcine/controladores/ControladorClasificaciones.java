/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.com.smartcine.controladores;

import java.util.List;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import sv.com.smartcine.dao.ClasificacionesJpaController;
import sv.com.smartcine.entidades.Clasificaciones;
import sv.com.smartcine.entidades.Genero;

/**
 *
 * @author kenia.mendozafgkss
 */
@ManagedBean(name = "clasificaciones")
@RequestScoped
public class ControladorClasificaciones {

    ClasificacionesJpaController clasificDAO;
    private Clasificaciones clasi;

    private Clasificaciones getClasi() {
        return clasi;
    }

    private void setClasi(Clasificaciones clasi) {
        this.clasi = clasi;
    }
    
    public ControladorClasificaciones() {
    }
    
    public List<Clasificaciones> listar(){
        return clasificDAO.findClasificacionesEntities();
    }
    
    public String ingresar(){
        clasificDAO.create(clasi);
        return "index?faces-redirect=true";
    }
    
    //
    public String editar(Clasificaciones c){
        Map<String, Object> objetos = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        objetos.put("cl", c);
        return "editar?faces-redirect=true"; 
    }
    
    public String actualizar(Clasificaciones c){
        try {
            clasificDAO.edit(c);
            return "index?faces-redirect=true";
        } catch (Exception e) {
            
            return null;
        }
    }
    
    public String ver(Clasificaciones c){
        clasi = c;
        return "ver?faces-redirect=true";
    }
    
    public String destruir(Clasificaciones c){
        try {
            clasificDAO.destroy(c.getId());
        return "index?faces-redirect=true";
        } catch (Exception e) {
            return null;
        }
    }
    
}
