/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.com.smartcine.dao;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import sv.com.smartcine.dao.exceptions.NonexistentEntityException;
import sv.com.smartcine.entidades.Asientos;
import sv.com.smartcine.entidades.DetalleReservaciones;
import sv.com.smartcine.entidades.Reservaciones;

public class DetalleReservacionesJpaController implements Serializable {

    public DetalleReservacionesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DetalleReservaciones detalleReservaciones) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Asientos idAsiento = detalleReservaciones.getIdAsiento();
            if (idAsiento != null) {
                idAsiento = em.getReference(idAsiento.getClass(), idAsiento.getId());
                detalleReservaciones.setIdAsiento(idAsiento);
            }
            Reservaciones idReservacion = detalleReservaciones.getIdReservacion();
            if (idReservacion != null) {
                idReservacion = em.getReference(idReservacion.getClass(), idReservacion.getId());
                detalleReservaciones.setIdReservacion(idReservacion);
            }
            em.persist(detalleReservaciones);
            if (idAsiento != null) {
                idAsiento.getDetalleReservacionesList().add(detalleReservaciones);
                idAsiento = em.merge(idAsiento);
            }
            if (idReservacion != null) {
                idReservacion.getDetalleReservacionesList().add(detalleReservaciones);
                idReservacion = em.merge(idReservacion);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DetalleReservaciones detalleReservaciones) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DetalleReservaciones persistentDetalleReservaciones = em.find(DetalleReservaciones.class, detalleReservaciones.getId());
            Asientos idAsientoOld = persistentDetalleReservaciones.getIdAsiento();
            Asientos idAsientoNew = detalleReservaciones.getIdAsiento();
            Reservaciones idReservacionOld = persistentDetalleReservaciones.getIdReservacion();
            Reservaciones idReservacionNew = detalleReservaciones.getIdReservacion();
            if (idAsientoNew != null) {
                idAsientoNew = em.getReference(idAsientoNew.getClass(), idAsientoNew.getId());
                detalleReservaciones.setIdAsiento(idAsientoNew);
            }
            if (idReservacionNew != null) {
                idReservacionNew = em.getReference(idReservacionNew.getClass(), idReservacionNew.getId());
                detalleReservaciones.setIdReservacion(idReservacionNew);
            }
            detalleReservaciones = em.merge(detalleReservaciones);
            if (idAsientoOld != null && !idAsientoOld.equals(idAsientoNew)) {
                idAsientoOld.getDetalleReservacionesList().remove(detalleReservaciones);
                idAsientoOld = em.merge(idAsientoOld);
            }
            if (idAsientoNew != null && !idAsientoNew.equals(idAsientoOld)) {
                idAsientoNew.getDetalleReservacionesList().add(detalleReservaciones);
                idAsientoNew = em.merge(idAsientoNew);
            }
            if (idReservacionOld != null && !idReservacionOld.equals(idReservacionNew)) {
                idReservacionOld.getDetalleReservacionesList().remove(detalleReservaciones);
                idReservacionOld = em.merge(idReservacionOld);
            }
            if (idReservacionNew != null && !idReservacionNew.equals(idReservacionOld)) {
                idReservacionNew.getDetalleReservacionesList().add(detalleReservaciones);
                idReservacionNew = em.merge(idReservacionNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = detalleReservaciones.getId();
                if (findDetalleReservaciones(id) == null) {
                    throw new NonexistentEntityException("The detalleReservaciones with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DetalleReservaciones detalleReservaciones;
            try {
                detalleReservaciones = em.getReference(DetalleReservaciones.class, id);
                detalleReservaciones.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detalleReservaciones with id " + id + " no longer exists.", enfe);
            }
            Asientos idAsiento = detalleReservaciones.getIdAsiento();
            if (idAsiento != null) {
                idAsiento.getDetalleReservacionesList().remove(detalleReservaciones);
                idAsiento = em.merge(idAsiento);
            }
            Reservaciones idReservacion = detalleReservaciones.getIdReservacion();
            if (idReservacion != null) {
                idReservacion.getDetalleReservacionesList().remove(detalleReservaciones);
                idReservacion = em.merge(idReservacion);
            }
            em.remove(detalleReservaciones);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DetalleReservaciones> findDetalleReservacionesEntities() {
        return findDetalleReservacionesEntities(true, -1, -1);
    }

    public List<DetalleReservaciones> findDetalleReservacionesEntities(int maxResults, int firstResult) {
        return findDetalleReservacionesEntities(false, maxResults, firstResult);
    }

    private List<DetalleReservaciones> findDetalleReservacionesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DetalleReservaciones.class));
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

    public DetalleReservaciones findDetalleReservaciones(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DetalleReservaciones.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetalleReservacionesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DetalleReservaciones> rt = cq.from(DetalleReservaciones.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
