/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.com.smartcine.dao;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import sv.com.smartcine.entidades.Reservaciones;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import sv.com.smartcine.dao.exceptions.IllegalOrphanException;
import sv.com.smartcine.dao.exceptions.NonexistentEntityException;
import sv.com.smartcine.entidades.Usuarios;

/**
 *
 * @author william.valdezfgkss
 */
public class UsuariosJpaController implements Serializable {

    public UsuariosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuarios usuarios) {
        if (usuarios.getReservacionesList() == null) {
            usuarios.setReservacionesList(new ArrayList<Reservaciones>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Reservaciones> attachedReservacionesList = new ArrayList<Reservaciones>();
            for (Reservaciones reservacionesListReservacionesToAttach : usuarios.getReservacionesList()) {
                reservacionesListReservacionesToAttach = em.getReference(reservacionesListReservacionesToAttach.getClass(), reservacionesListReservacionesToAttach.getId());
                attachedReservacionesList.add(reservacionesListReservacionesToAttach);
            }
            usuarios.setReservacionesList(attachedReservacionesList);
            em.persist(usuarios);
            for (Reservaciones reservacionesListReservaciones : usuarios.getReservacionesList()) {
                Usuarios oldIdUsuarioOfReservacionesListReservaciones = reservacionesListReservaciones.getIdUsuario();
                reservacionesListReservaciones.setIdUsuario(usuarios);
                reservacionesListReservaciones = em.merge(reservacionesListReservaciones);
                if (oldIdUsuarioOfReservacionesListReservaciones != null) {
                    oldIdUsuarioOfReservacionesListReservaciones.getReservacionesList().remove(reservacionesListReservaciones);
                    oldIdUsuarioOfReservacionesListReservaciones = em.merge(oldIdUsuarioOfReservacionesListReservaciones);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuarios usuarios) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios persistentUsuarios = em.find(Usuarios.class, usuarios.getId());
            List<Reservaciones> reservacionesListOld = persistentUsuarios.getReservacionesList();
            List<Reservaciones> reservacionesListNew = usuarios.getReservacionesList();
            List<String> illegalOrphanMessages = null;
            for (Reservaciones reservacionesListOldReservaciones : reservacionesListOld) {
                if (!reservacionesListNew.contains(reservacionesListOldReservaciones)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Reservaciones " + reservacionesListOldReservaciones + " since its idUsuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Reservaciones> attachedReservacionesListNew = new ArrayList<Reservaciones>();
            for (Reservaciones reservacionesListNewReservacionesToAttach : reservacionesListNew) {
                reservacionesListNewReservacionesToAttach = em.getReference(reservacionesListNewReservacionesToAttach.getClass(), reservacionesListNewReservacionesToAttach.getId());
                attachedReservacionesListNew.add(reservacionesListNewReservacionesToAttach);
            }
            reservacionesListNew = attachedReservacionesListNew;
            usuarios.setReservacionesList(reservacionesListNew);
            usuarios = em.merge(usuarios);
            for (Reservaciones reservacionesListNewReservaciones : reservacionesListNew) {
                if (!reservacionesListOld.contains(reservacionesListNewReservaciones)) {
                    Usuarios oldIdUsuarioOfReservacionesListNewReservaciones = reservacionesListNewReservaciones.getIdUsuario();
                    reservacionesListNewReservaciones.setIdUsuario(usuarios);
                    reservacionesListNewReservaciones = em.merge(reservacionesListNewReservaciones);
                    if (oldIdUsuarioOfReservacionesListNewReservaciones != null && !oldIdUsuarioOfReservacionesListNewReservaciones.equals(usuarios)) {
                        oldIdUsuarioOfReservacionesListNewReservaciones.getReservacionesList().remove(reservacionesListNewReservaciones);
                        oldIdUsuarioOfReservacionesListNewReservaciones = em.merge(oldIdUsuarioOfReservacionesListNewReservaciones);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = usuarios.getId();
                if (findUsuarios(id) == null) {
                    throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public Usuarios buscarUsuario(String usuario, String clave) {
        EntityManager em = getEntityManager();
        try {
            return (Usuarios) em.createNamedQuery("Usuarios.BuscarporUsuarioClave", Usuarios.class).setParameter("usuario", usuario).setParameter("clave", clave).getSingleResult();
        } finally {
            em.close();
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios usuarios;
            try {
                usuarios = em.getReference(Usuarios.class, id);
                usuarios.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Reservaciones> reservacionesListOrphanCheck = usuarios.getReservacionesList();
            for (Reservaciones reservacionesListOrphanCheckReservaciones : reservacionesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuarios (" + usuarios + ") cannot be destroyed since the Reservaciones " + reservacionesListOrphanCheckReservaciones + " in its reservacionesList field has a non-nullable idUsuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuarios);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuarios> findUsuariosEntities() {
        return findUsuariosEntities(true, -1, -1);
    }

    public List<Usuarios> findUsuariosEntities(int maxResults, int firstResult) {
        return findUsuariosEntities(false, maxResults, firstResult);
    }

    private List<Usuarios> findUsuariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuarios.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Usuarios findUsuarios(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuarios.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuariosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuarios> rt = cq.from(Usuarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
