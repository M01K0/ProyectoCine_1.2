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
import sv.com.smartcine.entidades.Salas;
import sv.com.smartcine.entidades.DetalleReservaciones;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import sv.com.smartcine.dao.exceptions.IllegalOrphanException;
import sv.com.smartcine.dao.exceptions.NonexistentEntityException;
import sv.com.smartcine.entidades.Asientos;

public class AsientosJpaController implements Serializable {

    public AsientosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Asientos asientos) {
        if (asientos.getDetalleReservacionesList() == null) {
            asientos.setDetalleReservacionesList(new ArrayList<DetalleReservaciones>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Salas idSala = asientos.getIdSala();
            if (idSala != null) {
                idSala = em.getReference(idSala.getClass(), idSala.getId());
                asientos.setIdSala(idSala);
            }
            List<DetalleReservaciones> attachedDetalleReservacionesList = new ArrayList<DetalleReservaciones>();
            for (DetalleReservaciones detalleReservacionesListDetalleReservacionesToAttach : asientos.getDetalleReservacionesList()) {
                detalleReservacionesListDetalleReservacionesToAttach = em.getReference(detalleReservacionesListDetalleReservacionesToAttach.getClass(), detalleReservacionesListDetalleReservacionesToAttach.getId());
                attachedDetalleReservacionesList.add(detalleReservacionesListDetalleReservacionesToAttach);
            }
            asientos.setDetalleReservacionesList(attachedDetalleReservacionesList);
            em.persist(asientos);
            if (idSala != null) {
                idSala.getAsientosList().add(asientos);
                idSala = em.merge(idSala);
            }
            for (DetalleReservaciones detalleReservacionesListDetalleReservaciones : asientos.getDetalleReservacionesList()) {
                Asientos oldIdAsientoOfDetalleReservacionesListDetalleReservaciones = detalleReservacionesListDetalleReservaciones.getIdAsiento();
                detalleReservacionesListDetalleReservaciones.setIdAsiento(asientos);
                detalleReservacionesListDetalleReservaciones = em.merge(detalleReservacionesListDetalleReservaciones);
                if (oldIdAsientoOfDetalleReservacionesListDetalleReservaciones != null) {
                    oldIdAsientoOfDetalleReservacionesListDetalleReservaciones.getDetalleReservacionesList().remove(detalleReservacionesListDetalleReservaciones);
                    oldIdAsientoOfDetalleReservacionesListDetalleReservaciones = em.merge(oldIdAsientoOfDetalleReservacionesListDetalleReservaciones);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Asientos asientos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Asientos persistentAsientos = em.find(Asientos.class, asientos.getId());
            Salas idSalaOld = persistentAsientos.getIdSala();
            Salas idSalaNew = asientos.getIdSala();
            List<DetalleReservaciones> detalleReservacionesListOld = persistentAsientos.getDetalleReservacionesList();
            List<DetalleReservaciones> detalleReservacionesListNew = asientos.getDetalleReservacionesList();
            List<String> illegalOrphanMessages = null;
            for (DetalleReservaciones detalleReservacionesListOldDetalleReservaciones : detalleReservacionesListOld) {
                if (!detalleReservacionesListNew.contains(detalleReservacionesListOldDetalleReservaciones)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DetalleReservaciones " + detalleReservacionesListOldDetalleReservaciones + " since its idAsiento field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idSalaNew != null) {
                idSalaNew = em.getReference(idSalaNew.getClass(), idSalaNew.getId());
                asientos.setIdSala(idSalaNew);
            }
            List<DetalleReservaciones> attachedDetalleReservacionesListNew = new ArrayList<DetalleReservaciones>();
            for (DetalleReservaciones detalleReservacionesListNewDetalleReservacionesToAttach : detalleReservacionesListNew) {
                detalleReservacionesListNewDetalleReservacionesToAttach = em.getReference(detalleReservacionesListNewDetalleReservacionesToAttach.getClass(), detalleReservacionesListNewDetalleReservacionesToAttach.getId());
                attachedDetalleReservacionesListNew.add(detalleReservacionesListNewDetalleReservacionesToAttach);
            }
            detalleReservacionesListNew = attachedDetalleReservacionesListNew;
            asientos.setDetalleReservacionesList(detalleReservacionesListNew);
            asientos = em.merge(asientos);
            if (idSalaOld != null && !idSalaOld.equals(idSalaNew)) {
                idSalaOld.getAsientosList().remove(asientos);
                idSalaOld = em.merge(idSalaOld);
            }
            if (idSalaNew != null && !idSalaNew.equals(idSalaOld)) {
                idSalaNew.getAsientosList().add(asientos);
                idSalaNew = em.merge(idSalaNew);
            }
            for (DetalleReservaciones detalleReservacionesListNewDetalleReservaciones : detalleReservacionesListNew) {
                if (!detalleReservacionesListOld.contains(detalleReservacionesListNewDetalleReservaciones)) {
                    Asientos oldIdAsientoOfDetalleReservacionesListNewDetalleReservaciones = detalleReservacionesListNewDetalleReservaciones.getIdAsiento();
                    detalleReservacionesListNewDetalleReservaciones.setIdAsiento(asientos);
                    detalleReservacionesListNewDetalleReservaciones = em.merge(detalleReservacionesListNewDetalleReservaciones);
                    if (oldIdAsientoOfDetalleReservacionesListNewDetalleReservaciones != null && !oldIdAsientoOfDetalleReservacionesListNewDetalleReservaciones.equals(asientos)) {
                        oldIdAsientoOfDetalleReservacionesListNewDetalleReservaciones.getDetalleReservacionesList().remove(detalleReservacionesListNewDetalleReservaciones);
                        oldIdAsientoOfDetalleReservacionesListNewDetalleReservaciones = em.merge(oldIdAsientoOfDetalleReservacionesListNewDetalleReservaciones);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = asientos.getId();
                if (findAsientos(id) == null) {
                    throw new NonexistentEntityException("The asientos with id " + id + " no longer exists.");
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
            Asientos asientos;
            try {
                asientos = em.getReference(Asientos.class, id);
                asientos.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The asientos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<DetalleReservaciones> detalleReservacionesListOrphanCheck = asientos.getDetalleReservacionesList();
            for (DetalleReservaciones detalleReservacionesListOrphanCheckDetalleReservaciones : detalleReservacionesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Asientos (" + asientos + ") cannot be destroyed since the DetalleReservaciones " + detalleReservacionesListOrphanCheckDetalleReservaciones + " in its detalleReservacionesList field has a non-nullable idAsiento field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Salas idSala = asientos.getIdSala();
            if (idSala != null) {
                idSala.getAsientosList().remove(asientos);
                idSala = em.merge(idSala);
            }
            em.remove(asientos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Asientos> findAsientosEntities() {
        return findAsientosEntities(true, -1, -1);
    }

    public List<Asientos> findAsientosEntities(int maxResults, int firstResult) {
        return findAsientosEntities(false, maxResults, firstResult);
    }

    private List<Asientos> findAsientosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Asientos.class));
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

    public Asientos findAsientos(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Asientos.class, id);
        } finally {
            em.close();
        }
    }

    public int getAsientosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Asientos> rt = cq.from(Asientos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<Asientos> porIdSala(Long idSal){
        EntityManager em = getEntityManager();
        
        try {
            return em.createNamedQuery("Asientos.findByIdSala", Asientos.class).setParameter("idSala",idSal).getResultList();
        } finally {
            em.close();
        }
    }
    
}
