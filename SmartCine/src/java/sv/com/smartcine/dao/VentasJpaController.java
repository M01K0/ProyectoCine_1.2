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
import sv.com.smartcine.dao.exceptions.NonexistentEntityException;
import sv.com.smartcine.entidades.Ventas;

public class VentasJpaController implements Serializable {

    public VentasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ventas ventas) {
        if (ventas.getReservacionesList() == null) {
            ventas.setReservacionesList(new ArrayList<Reservaciones>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Reservaciones> attachedReservacionesList = new ArrayList<Reservaciones>();
            for (Reservaciones reservacionesListReservacionesToAttach : ventas.getReservacionesList()) {
                reservacionesListReservacionesToAttach = em.getReference(reservacionesListReservacionesToAttach.getClass(), reservacionesListReservacionesToAttach.getId());
                attachedReservacionesList.add(reservacionesListReservacionesToAttach);
            }
            ventas.setReservacionesList(attachedReservacionesList);
            em.persist(ventas);
            for (Reservaciones reservacionesListReservaciones : ventas.getReservacionesList()) {
                Ventas oldIdVentasOfReservacionesListReservaciones = reservacionesListReservaciones.getIdVentas();
                reservacionesListReservaciones.setIdVentas(ventas);
                reservacionesListReservaciones = em.merge(reservacionesListReservaciones);
                if (oldIdVentasOfReservacionesListReservaciones != null) {
                    oldIdVentasOfReservacionesListReservaciones.getReservacionesList().remove(reservacionesListReservaciones);
                    oldIdVentasOfReservacionesListReservaciones = em.merge(oldIdVentasOfReservacionesListReservaciones);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ventas ventas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ventas persistentVentas = em.find(Ventas.class, ventas.getId());
            List<Reservaciones> reservacionesListOld = persistentVentas.getReservacionesList();
            List<Reservaciones> reservacionesListNew = ventas.getReservacionesList();
            List<Reservaciones> attachedReservacionesListNew = new ArrayList<Reservaciones>();
            for (Reservaciones reservacionesListNewReservacionesToAttach : reservacionesListNew) {
                reservacionesListNewReservacionesToAttach = em.getReference(reservacionesListNewReservacionesToAttach.getClass(), reservacionesListNewReservacionesToAttach.getId());
                attachedReservacionesListNew.add(reservacionesListNewReservacionesToAttach);
            }
            reservacionesListNew = attachedReservacionesListNew;
            ventas.setReservacionesList(reservacionesListNew);
            ventas = em.merge(ventas);
            for (Reservaciones reservacionesListOldReservaciones : reservacionesListOld) {
                if (!reservacionesListNew.contains(reservacionesListOldReservaciones)) {
                    reservacionesListOldReservaciones.setIdVentas(null);
                    reservacionesListOldReservaciones = em.merge(reservacionesListOldReservaciones);
                }
            }
            for (Reservaciones reservacionesListNewReservaciones : reservacionesListNew) {
                if (!reservacionesListOld.contains(reservacionesListNewReservaciones)) {
                    Ventas oldIdVentasOfReservacionesListNewReservaciones = reservacionesListNewReservaciones.getIdVentas();
                    reservacionesListNewReservaciones.setIdVentas(ventas);
                    reservacionesListNewReservaciones = em.merge(reservacionesListNewReservaciones);
                    if (oldIdVentasOfReservacionesListNewReservaciones != null && !oldIdVentasOfReservacionesListNewReservaciones.equals(ventas)) {
                        oldIdVentasOfReservacionesListNewReservaciones.getReservacionesList().remove(reservacionesListNewReservaciones);
                        oldIdVentasOfReservacionesListNewReservaciones = em.merge(oldIdVentasOfReservacionesListNewReservaciones);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = ventas.getId();
                if (findVentas(id) == null) {
                    throw new NonexistentEntityException("The ventas with id " + id + " no longer exists.");
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
            Ventas ventas;
            try {
                ventas = em.getReference(Ventas.class, id);
                ventas.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ventas with id " + id + " no longer exists.", enfe);
            }
            List<Reservaciones> reservacionesList = ventas.getReservacionesList();
            for (Reservaciones reservacionesListReservaciones : reservacionesList) {
                reservacionesListReservaciones.setIdVentas(null);
                reservacionesListReservaciones = em.merge(reservacionesListReservaciones);
            }
            em.remove(ventas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ventas> findVentasEntities() {
        return findVentasEntities(true, -1, -1);
    }

    public List<Ventas> findVentasEntities(int maxResults, int firstResult) {
        return findVentasEntities(false, maxResults, firstResult);
    }

    private List<Ventas> findVentasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ventas.class));
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

    public Ventas findVentas(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ventas.class, id);
        } finally {
            em.close();
        }
    }

    public int getVentasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ventas> rt = cq.from(Ventas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
