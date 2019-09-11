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
import javax.servlet.http.HttpSession;
import sv.com.smartcine.dao.UsuariosJpaController;
import sv.com.smartcine.entidades.Usuarios;

/**
 *
 * @author william.valdezfgkss
 */
@ManagedBean(name = "usuario")
@RequestScoped
public class ControladorUsuario {
    UsuariosJpaController usuDAO;
    Usuarios usu;
    
    public ControladorUsuario() {
        usuDAO = new UsuariosJpaController(Persistence.createEntityManagerFactory("SmartCinePU"));
        usu = new Usuarios();
    }
    
    public List<Usuarios> listar(){
        return usuDAO.findUsuariosEntities();
    }
    
    public String ingresar(){
        usuDAO.create(usu);
        return "index?faces-redirect=true";
    }
    
    
    public String editar(Usuarios s){
        Map<String, Object> objetos = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        objetos.put("ss", s);
        return "editar?faces-redirect=true"; 
    }
    
    public String actualizar(Usuarios s){
        try {
            usuDAO.edit(s);
            return "index?faces-redirect=true";
        } catch (Exception e) {
            
            return null;
        }
    }
    
    public String ver(Usuarios s){
        usu = s;
        return "ver?faces-redirect=true";
    }
    
    public String destruir(Usuarios s){
        try {
            usuDAO.destroy(s.getId());
        return "index?faces-redirect=true";
        } catch (Exception e) {
            return null;
        }
    }
    
  
    public Usuarios getUsu() {
        return usu;
    }

    public void setUsu(Usuarios usu) {
        this.usu = usu;
    }
}
