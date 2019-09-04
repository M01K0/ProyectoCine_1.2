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
import sv.com.smartcine.entidades.Usuarios;
import sv.com.smartcine.entidades.Funciones;
import sv.com.smartcine.entidades.Ventas;
import sv.com.smartcine.entidades.DetalleReservaciones;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import sv.com.smartcine.dao.exceptions.IllegalOrphanException;
import sv.com.smartcine.dao.exceptions.NonexistentEntityException;
import sv.com.smartcine.entidades.Reservaciones;

/**
 *
 * @author william.valdezfgkss
 */
public class ReservacionesJpaController implements Serializable {

    public ReservacionesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Reservaciones reservaciones) {
        if (reservaciones.getDetalleReservacionesList() == null) {
            reservaciones.setDetalleReservacionesList(new ArrayList<DetalleReservaciones>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios idUsuario = reservaciones.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getId());
                reservaciones.setIdUsuario(idUsuario);
            }
            Funciones idFuncion = reservaciones.getIdFuncion();
            if (idFuncion != null) {
                idFuncion = em.getReference(idFuncion.getClass(), idFuncion.getId());
                reservaciones.setIdFuncion(idFuncion);
            }
            Ventas idVentas = reservaciones.getIdVentas();
            if (idVentas != null) {
                idVentas = em.getReference(idVentas.getClass(), idVentas.getId());
                reservaciones.setIdVentas(idVentas);
            }
            List<DetalleReservaciones> attachedDetalleReservacionesList = new ArrayList<DetalleReservaciones>();
            for (DetalleReservaciones detalleReservacionesListDetalleReservacionesToAttach : reservaciones.getDetalleReservacionesList()) {
                detalleReservacionesListDetalleReservacionesToAttach = em.getReference(detalleReservacionesListDetalleReservacionesToAttach.getClass(), detalleReservacionesListDetalleReservacionesToAttach.getId());
                attachedDetalleReservacionesList.add(detalleReservacionesListDetalleReservacionesToAttach);
            }
            reservaciones.setDetalleReservacionesList(attachedDetalleReservacionesList);
            em.persist(reservaciones);
            if (idUsuario != null) {
                idUsuario.getReservacionesList().add(reservaciones);
                idUsuario = em.merge(idUsuario);
            }
            if (idFuncion != null) {
                idFuncion.getReservacionesList().add(reservaciones);
                idFuncion = em.merge(idFuncion);
            }
            if (idVentas != null) {
                idVentas.getReservacionesList().add(reservaciones);
                idVentas = em.merge(idVentas);
            }
            for (DetalleReservaciones detalleReservacionesListDetalleReservaciones : reservaciones.getDetalleReservacionesList()) {
                Reservaciones oldIdReservacionOfDetalleReservacionesListDetalleReservaciones = detalleReservacionesListDetalleReservaciones.getIdReservacion();
                detalleReservacionesListDetalleReservaciones.setIdReservacion(reservaciones);
                detalleReservacionesListDetalleReservaciones = em.merge(detalleReservacionesListDetalleReservaciones);
                if (oldIdReservacionOfDetalleReservacionesListDetalleReservaciones != null) {
                    oldIdReservacionOfDetalleReservacionesListDetalleReservaciones.getDetalleReservacionesList().remove(detalleReservacionesListDetalleReservaciones);
                    oldIdReservacionOfDetalleReservacionesListDetalleReservaciones = em.merge(oldIdReservacionOfDetalleReservacionesListDetalleReservaciones);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Reservaciones reservaciones) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Reservaciones persistentReservaciones = em.find(Reservaciones.class, reservaciones.getId());
            Usuarios idUsuarioOld = persistentReservaciones.getIdUsuario();
            Usuarios idUsuarioNew = reservaciones.getIdUsuario();
            Funciones idFuncionOld = persistentReservaciones.getIdFuncion();
            Funciones idFuncionNew = reservaciones.getIdFuncion();
            Ventas idVentasOld = persistentReservaciones.getIdVentas();
            Ventas idVentasNew = reservaciones.getIdVentas();
            List<DetalleReservaciones> detalleReservacionesListOld = persistentReservaciones.getDetalleReservacionesList();
            List<DetalleReservaciones> detalleReservacionesListNew = reservaciones.getDetalleReservacionesList();
            List<String> illegalOrphanMessages = null;
            for (DetalleReservaciones detalleReservacionesListOldDetalleReservaciones : detalleReservacionesListOld) {
                if (!detalleReservacionesListNew.contains(detalleReservacionesListOldDetalleReservaciones)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DetalleReservaciones " + detalleReservacionesListOldDetalleReservaciones + " since its idReservacion field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getId());
                reservaciones.setIdUsuario(idUsuarioNew);
            }
            if (idFuncionNew != null) {
                idFuncionNew = em.getReference(idFuncionNew.getClass(), idFuncionNew.getId());
                reservaciones.setIdFuncion(idFuncionNew);
            }
            if (idVentasNew != null) {
                idVentasNew = em.getReference(idVentasNew.getClass(), idVentasNew.getId());
                reservaciones.setIdVentas(idVentasNew);
            }
            List<DetalleReservaciones> attachedDetalleReservacionesListNew = new ArrayList<DetalleReservaciones>();
            for (DetalleReservaciones detalleReservacionesListNewDetalleReservacionesToAttach : detalleReservacionesListNew) {
                detalleReservacionesListNewDetalleReservacionesToAttach = em.getReference(detalleReservacionesListNewDetalleReservacionesToAttach.getClass(), detalleReservacionesListNewDetalleReservacionesToAttach.getId());
                attachedDetalleReservacionesListNew.add(detalleReservacionesListNewDetalleReservacionesToAttach);
            }
            detalleReservacionesListNew = attachedDetalleReservacionesListNew;
            reservaciones.setDetalleReservacionesList(detalleReservacionesListNew);
            reservaciones = em.merge(reservaciones);
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.getReservacionesList().remove(reservaciones);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                idUsuarioNew.getReservacionesList().add(reservaciones);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
            if (idFuncionOld != null && !idFuncionOld.equals(idFuncionNew)) {
                idFuncionOld.getReservacionesList().remove(reservaciones);
                idFuncionOld = em.merge(idFuncionOld);
            }
            if (idFuncionNew != null && !idFuncionNew.equals(idFuncionOld)) {
                idFuncionNew.getReservacionesList().add(reservaciones);
                idFuncionNew = em.merge(idFuncionNew);
            }
            if (idVentasOld != null && !idVentasOld.equals(idVentasNew)) {
                idVentasOld.getReservacionesList().remove(reservaciones);
                idVentasOld = em.merge(idVentasOld);
            }
            if (idVentasNew != null && !idVentasNew.equals(idVentasOld)) {
                idVentasNew.getReservacionesList().add(reservaciones);
                idVentasNew = em.merge(idVentasNew);
            }
            for (DetalleReservaciones detalleReservacionesListNewDetalleReservaciones : detalleReservacionesListNew) {
                if (!detalleReservacionesListOld.contains(detalleReservacionesListNewDetalleReservaciones)) {
                    Reservaciones oldIdReservacionOfDetalleReservacionesListNewDetalleReservaciones = detalleReservacionesListNewDetalleReservaciones.getIdReservacion();
                    detalleReservacionesListNewDetalleReservaciones.setIdReservacion(reservaciones);
                    detalleReservacionesListNewDetalleReservaciones = em.merge(detalleReservacionesListNewDetalleReservaciones);
                    if (oldIdReservacionOfDetalleReservacionesListNewDetalleReservaciones != null && !oldIdReservacionOfDetalleReservacionesListNewDetalleReservaciones.equals(reservaciones)) {
                        oldIdReservacionOfDetalleReservacionesListNewDetalleReservaciones.getDetalleReservacionesList().remove(detalleReservacionesListNewDetalleReservaciones);
                        oldIdReservacionOfDetalleReservacionesListNewDetalleReservaciones = em.merge(oldIdReservacionOfDetalleReservacionesListNewDetalleReservaciones);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = reservaciones.getId();
                if (findReservaciones(id) == null) {
                    throw new NonexistentEntityException("The reservaciones with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Reservaciones reservaciones;
            try {
                reservaciones = em.getReference(Reservaciones.class, id);
                reservaciones.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reservaciones with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<DetalleReservaciones> detalleReservacionesListOrphanCheck = reservaciones.getDetalleReservacionesList();
            for (DetalleReservaciones detalleReservacionesListOrphanCheckDetalleReservaciones : detalleReservacionesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Reservaciones (" + reservaciones + ") cannot be destroyed since the DetalleReservaciones " + detalleReservacionesListOrphanCheckDetalleReservaciones + " in its detalleReservacionesList field has a non-nullable idReservacion field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Usuarios idUsuario = reservaciones.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.getReservacionesList().remove(reservaciones);
                idUsuario = em.merge(idUsuario);
            }
            Funciones idFuncion = reservaciones.getIdFuncion();
            if (idFuncion != null) {
                idFuncion.getReservacionesList().remove(reservaciones);
                idFuncion = em.merge(idFuncion);
            }
            Ventas idVentas = reservaciones.getIdVentas();
            if (idVentas != null) {
                idVentas.getReservacionesList().remove(reservaciones);
                idVentas = em.merge(idVentas);
            }
            em.remove(reservaciones);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Reservaciones> findReservacionesEntities() {
        return findReservacionesEntities(true, -1, -1);
    }

    public List<Reservaciones> findReservacionesEntities(int maxResults, int firstResult) {
        return findReservacionesEntities(false, maxResults, firstResult);
    }

    private List<Reservaciones> findReservacionesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Reservaciones.class));
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

    public Reservaciones findReservaciones(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Reservaciones.class, id);
        } finally {
            em.close();
        }
    }

    public int getReservacionesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Reservaciones> rt = cq.from(Reservaciones.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
