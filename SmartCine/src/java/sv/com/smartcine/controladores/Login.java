/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.com.smartcine.controladores;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.persistence.Persistence;
import javax.servlet.http.HttpSession;
import sv.com.smartcine.dao.UsuariosJpaController;
import sv.com.smartcine.entidades.Usuarios;

/**
 *
 * @author nehe.sandovalfgkss
 */
@ManagedBean(name = "login")
@SessionScoped
public class Login implements Serializable {
    UsuariosJpaController usuDAO;
    Usuarios usu;
    
    public Login() {
        usuDAO = new UsuariosJpaController(Persistence.createEntityManagerFactory("SmartCinePU"));
        usu = new Usuarios();
    }
    public String validar(){
        usu = usuDAO.buscarUsuario(usu.getUsuario(), usu.getClave());
        String val = "";
        if (usu != null) {
            HttpSession session = SesionUtil.getSession();
            session.setAttribute("user", usu);
            switch(usu.getRol()){
                case "Administrador": 
                    val = "/faces/admin?faces-redirect=true";
                     break;
                case "Estandar":
                    val = "/faces/index?faces-redirect=true";
                    break;
                
            }
            return val;
        } else {
            System.err.println("Usuario nulo");
            return "index?faces-redirect=true";
        }
    }

    public Usuarios getUsu() {
        return usu;
    }

    public void setUsu(Usuarios usu) {
        this.usu = usu;
    }
    
    public boolean existSession(){
        return (SesionUtil.getUserId() != null);
    }
    
    public String cerrar() {
        HttpSession session = SesionUtil.getSession();
        session.invalidate();
        return "index.xhtml";
    }
    
}
