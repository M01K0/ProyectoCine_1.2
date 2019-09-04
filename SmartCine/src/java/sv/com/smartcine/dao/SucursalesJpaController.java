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
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import sv.com.smartcine.dao.exceptions.NonexistentEntityException;
import sv.com.smartcine.entidades.Sucursales;

/**
 *
 * @author william.valdezfgkss
 */
public class SucursalesJpaController implements Serializable {

    public SucursalesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Sucursales sucursales) {
        if (sucursales.getSalasList() == null) {
            sucursales.setSalasList(new ArrayList<Salas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Salas> attachedSalasList = new ArrayList<Salas>();
            for (Salas salasListSalasToAttach : sucursales.getSalasList()) {
                salasListSalasToAttach = em.getReference(salasListSalasToAttach.getClass(), salasListSalasToAttach.getId());
                attachedSalasList.add(salasListSalasToAttach);
            }
            sucursales.setSalasList(attachedSalasList);
            em.persist(sucursales);
            for (Salas salasListSalas : sucursales.getSalasList()) {
                Sucursales oldIdSucursalOfSalasListSalas = salasListSalas.getIdSucursal();
                salasListSalas.setIdSucursal(sucursales);
                salasListSalas = em.merge(salasListSalas);
                if (oldIdSucursalOfSalasListSalas != null) {
                    oldIdSucursalOfSalasListSalas.getSalasList().remove(salasListSalas);
                    oldIdSucursalOfSalasListSalas = em.merge(oldIdSucursalOfSalasListSalas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Sucursales sucursales) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sucursales persistentSucursales = em.find(Sucursales.class, sucursales.getId());
            List<Salas> salasListOld = persistentSucursales.getSalasList();
            List<Salas> salasListNew = sucursales.getSalasList();
            List<Salas> attachedSalasListNew = new ArrayList<Salas>();
            for (Salas salasListNewSalasToAttach : salasListNew) {
                salasListNewSalasToAttach = em.getReference(salasListNewSalasToAttach.getClass(), salasListNewSalasToAttach.getId());
                attachedSalasListNew.add(salasListNewSalasToAttach);
            }
            salasListNew = attachedSalasListNew;
            sucursales.setSalasList(salasListNew);
            sucursales = em.merge(sucursales);
            for (Salas salasListOldSalas : salasListOld) {
                if (!salasListNew.contains(salasListOldSalas)) {
                    salasListOldSalas.setIdSucursal(null);
                    salasListOldSalas = em.merge(salasListOldSalas);
                }
            }
            for (Salas salasListNewSalas : salasListNew) {
                if (!salasListOld.contains(salasListNewSalas)) {
                    Sucursales oldIdSucursalOfSalasListNewSalas = salasListNewSalas.getIdSucursal();
                    salasListNewSalas.setIdSucursal(sucursales);
                    salasListNewSalas = em.merge(salasListNewSalas);
                    if (oldIdSucursalOfSalasListNewSalas != null && !oldIdSucursalOfSalasListNewSalas.equals(sucursales)) {
                        oldIdSucursalOfSalasListNewSalas.getSalasList().remove(salasListNewSalas);
                        oldIdSucursalOfSalasListNewSalas = em.merge(oldIdSucursalOfSalasListNewSalas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = sucursales.getId();
                if (findSucursales(id) == null) {
                    throw new NonexistentEntityException("The sucursales with id " + id + " no longer exists.");
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
            Sucursales sucursales;
            try {
                sucursales = em.getReference(Sucursales.class, id);
                sucursales.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sucursales with id " + id + " no longer exists.", enfe);
            }
            List<Salas> salasList = sucursales.getSalasList();
            for (Salas salasListSalas : salasList) {
                salasListSalas.setIdSucursal(null);
                salasListSalas = em.merge(salasListSalas);
            }
            em.remove(sucursales);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Sucursales> findSucursalesEntities() {
        return findSucursalesEntities(true, -1, -1);
    }

    public List<Sucursales> findSucursalesEntities(int maxResults, int firstResult) {
        return findSucursalesEntities(false, maxResults, firstResult);
    }

    private List<Sucursales> findSucursalesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Sucursales.class));
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

    public Sucursales findSucursales(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Sucursales.class, id);
        } finally {
            em.close();
        }
    }

    public int getSucursalesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Sucursales> rt = cq.from(Sucursales.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
