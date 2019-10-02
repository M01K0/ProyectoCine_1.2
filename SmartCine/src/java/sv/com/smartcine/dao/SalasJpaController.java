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
import sv.com.smartcine.entidades.Sucursales;
import sv.com.smartcine.entidades.Funciones;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import sv.com.smartcine.dao.exceptions.NonexistentEntityException;
import sv.com.smartcine.entidades.Asientos;
import sv.com.smartcine.entidades.Salas;

public class SalasJpaController implements Serializable {

    public SalasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Salas salas) {
        if (salas.getFuncionesList() == null) {
            salas.setFuncionesList(new ArrayList<Funciones>());
        }
        if (salas.getAsientosList() == null) {
            salas.setAsientosList(new ArrayList<Asientos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sucursales idSucursal = salas.getIdSucursal();
            if (idSucursal != null) {
                idSucursal = em.getReference(idSucursal.getClass(), idSucursal.getId());
                salas.setIdSucursal(idSucursal);
            }
            List<Funciones> attachedFuncionesList = new ArrayList<Funciones>();
            for (Funciones funcionesListFuncionesToAttach : salas.getFuncionesList()) {
                funcionesListFuncionesToAttach = em.getReference(funcionesListFuncionesToAttach.getClass(), funcionesListFuncionesToAttach.getId());
                attachedFuncionesList.add(funcionesListFuncionesToAttach);
            }
            salas.setFuncionesList(attachedFuncionesList);
            List<Asientos> attachedAsientosList = new ArrayList<Asientos>();
            for (Asientos asientosListAsientosToAttach : salas.getAsientosList()) {
                asientosListAsientosToAttach = em.getReference(asientosListAsientosToAttach.getClass(), asientosListAsientosToAttach.getId());
                attachedAsientosList.add(asientosListAsientosToAttach);
            }
            salas.setAsientosList(attachedAsientosList);
            em.persist(salas);
            if (idSucursal != null) {
                idSucursal.getSalasList().add(salas);
                idSucursal = em.merge(idSucursal);
            }
            for (Funciones funcionesListFunciones : salas.getFuncionesList()) {
                Salas oldIdSalaOfFuncionesListFunciones = funcionesListFunciones.getIdSala();
                funcionesListFunciones.setIdSala(salas);
                funcionesListFunciones = em.merge(funcionesListFunciones);
                if (oldIdSalaOfFuncionesListFunciones != null) {
                    oldIdSalaOfFuncionesListFunciones.getFuncionesList().remove(funcionesListFunciones);
                    oldIdSalaOfFuncionesListFunciones = em.merge(oldIdSalaOfFuncionesListFunciones);
                }
            }
            for (Asientos asientosListAsientos : salas.getAsientosList()) {
                Salas oldIdSalaOfAsientosListAsientos = asientosListAsientos.getIdSala();
                asientosListAsientos.setIdSala(salas);
                asientosListAsientos = em.merge(asientosListAsientos);
                if (oldIdSalaOfAsientosListAsientos != null) {
                    oldIdSalaOfAsientosListAsientos.getAsientosList().remove(asientosListAsientos);
                    oldIdSalaOfAsientosListAsientos = em.merge(oldIdSalaOfAsientosListAsientos);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Salas salas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Salas persistentSalas = em.find(Salas.class, salas.getId());
            Sucursales idSucursalOld = persistentSalas.getIdSucursal();
            Sucursales idSucursalNew = salas.getIdSucursal();
            List<Funciones> funcionesListOld = persistentSalas.getFuncionesList();
            List<Funciones> funcionesListNew = salas.getFuncionesList();
            List<Asientos> asientosListOld = persistentSalas.getAsientosList();
            List<Asientos> asientosListNew = salas.getAsientosList();
            if (idSucursalNew != null) {
                idSucursalNew = em.getReference(idSucursalNew.getClass(), idSucursalNew.getId());
                salas.setIdSucursal(idSucursalNew);
            }
            List<Funciones> attachedFuncionesListNew = new ArrayList<Funciones>();
            for (Funciones funcionesListNewFuncionesToAttach : funcionesListNew) {
                funcionesListNewFuncionesToAttach = em.getReference(funcionesListNewFuncionesToAttach.getClass(), funcionesListNewFuncionesToAttach.getId());
                attachedFuncionesListNew.add(funcionesListNewFuncionesToAttach);
            }
            funcionesListNew = attachedFuncionesListNew;
            salas.setFuncionesList(funcionesListNew);
            List<Asientos> attachedAsientosListNew = new ArrayList<Asientos>();
            for (Asientos asientosListNewAsientosToAttach : asientosListNew) {
                asientosListNewAsientosToAttach = em.getReference(asientosListNewAsientosToAttach.getClass(), asientosListNewAsientosToAttach.getId());
                attachedAsientosListNew.add(asientosListNewAsientosToAttach);
            }
            asientosListNew = attachedAsientosListNew;
            salas.setAsientosList(asientosListNew);
            salas = em.merge(salas);
            if (idSucursalOld != null && !idSucursalOld.equals(idSucursalNew)) {
                idSucursalOld.getSalasList().remove(salas);
                idSucursalOld = em.merge(idSucursalOld);
            }
            if (idSucursalNew != null && !idSucursalNew.equals(idSucursalOld)) {
                idSucursalNew.getSalasList().add(salas);
                idSucursalNew = em.merge(idSucursalNew);
            }
            for (Funciones funcionesListOldFunciones : funcionesListOld) {
                if (!funcionesListNew.contains(funcionesListOldFunciones)) {
                    funcionesListOldFunciones.setIdSala(null);
                    funcionesListOldFunciones = em.merge(funcionesListOldFunciones);
                }
            }
            for (Funciones funcionesListNewFunciones : funcionesListNew) {
                if (!funcionesListOld.contains(funcionesListNewFunciones)) {
                    Salas oldIdSalaOfFuncionesListNewFunciones = funcionesListNewFunciones.getIdSala();
                    funcionesListNewFunciones.setIdSala(salas);
                    funcionesListNewFunciones = em.merge(funcionesListNewFunciones);
                    if (oldIdSalaOfFuncionesListNewFunciones != null && !oldIdSalaOfFuncionesListNewFunciones.equals(salas)) {
                        oldIdSalaOfFuncionesListNewFunciones.getFuncionesList().remove(funcionesListNewFunciones);
                        oldIdSalaOfFuncionesListNewFunciones = em.merge(oldIdSalaOfFuncionesListNewFunciones);
                    }
                }
            }
            for (Asientos asientosListOldAsientos : asientosListOld) {
                if (!asientosListNew.contains(asientosListOldAsientos)) {
                    asientosListOldAsientos.setIdSala(null);
                    asientosListOldAsientos = em.merge(asientosListOldAsientos);
                }
            }
            for (Asientos asientosListNewAsientos : asientosListNew) {
                if (!asientosListOld.contains(asientosListNewAsientos)) {
                    Salas oldIdSalaOfAsientosListNewAsientos = asientosListNewAsientos.getIdSala();
                    asientosListNewAsientos.setIdSala(salas);
                    asientosListNewAsientos = em.merge(asientosListNewAsientos);
                    if (oldIdSalaOfAsientosListNewAsientos != null && !oldIdSalaOfAsientosListNewAsientos.equals(salas)) {
                        oldIdSalaOfAsientosListNewAsientos.getAsientosList().remove(asientosListNewAsientos);
                        oldIdSalaOfAsientosListNewAsientos = em.merge(oldIdSalaOfAsientosListNewAsientos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = salas.getId();
                if (findSalas(id) == null) {
                    throw new NonexistentEntityException("The salas with id " + id + " no longer exists.");
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
            Salas salas;
            try {
                salas = em.getReference(Salas.class, id);
                salas.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The salas with id " + id + " no longer exists.", enfe);
            }
            Sucursales idSucursal = salas.getIdSucursal();
            if (idSucursal != null) {
                idSucursal.getSalasList().remove(salas);
                idSucursal = em.merge(idSucursal);
            }
            List<Funciones> funcionesList = salas.getFuncionesList();
            for (Funciones funcionesListFunciones : funcionesList) {
                funcionesListFunciones.setIdSala(null);
                funcionesListFunciones = em.merge(funcionesListFunciones);
            }
            List<Asientos> asientosList = salas.getAsientosList();
            for (Asientos asientosListAsientos : asientosList) {
                asientosListAsientos.setIdSala(null);
                asientosListAsientos = em.merge(asientosListAsientos);
            }
            em.remove(salas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Salas> findSalasEntities() {
        return findSalasEntities(true, -1, -1);
    }

    public List<Salas> findSalasEntities(int maxResults, int firstResult) {
        return findSalasEntities(false, maxResults, firstResult);
    }

    private List<Salas> findSalasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Salas.class));
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

    public Salas findSalas(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Salas.class, id);
        } finally {
            em.close();
        }
    }

    public int getSalasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Salas> rt = cq.from(Salas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
