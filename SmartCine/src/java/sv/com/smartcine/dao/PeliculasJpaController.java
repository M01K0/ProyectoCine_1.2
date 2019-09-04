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
import sv.com.smartcine.entidades.Genero;
import sv.com.smartcine.entidades.Clasificaciones;
import sv.com.smartcine.entidades.Funciones;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import sv.com.smartcine.dao.exceptions.NonexistentEntityException;
import sv.com.smartcine.entidades.Peliculas;

/**
 *
 * @author william.valdezfgkss
 */
public class PeliculasJpaController implements Serializable {

    public PeliculasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Peliculas peliculas) {
        if (peliculas.getFuncionesList() == null) {
            peliculas.setFuncionesList(new ArrayList<Funciones>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Genero idGenero = peliculas.getIdGenero();
            if (idGenero != null) {
                idGenero = em.getReference(idGenero.getClass(), idGenero.getId());
                peliculas.setIdGenero(idGenero);
            }
            Clasificaciones idClasificacion = peliculas.getIdClasificacion();
            if (idClasificacion != null) {
                idClasificacion = em.getReference(idClasificacion.getClass(), idClasificacion.getId());
                peliculas.setIdClasificacion(idClasificacion);
            }
            List<Funciones> attachedFuncionesList = new ArrayList<Funciones>();
            for (Funciones funcionesListFuncionesToAttach : peliculas.getFuncionesList()) {
                funcionesListFuncionesToAttach = em.getReference(funcionesListFuncionesToAttach.getClass(), funcionesListFuncionesToAttach.getId());
                attachedFuncionesList.add(funcionesListFuncionesToAttach);
            }
            peliculas.setFuncionesList(attachedFuncionesList);
            em.persist(peliculas);
            if (idGenero != null) {
                idGenero.getPeliculasList().add(peliculas);
                idGenero = em.merge(idGenero);
            }
            if (idClasificacion != null) {
                idClasificacion.getPeliculasList().add(peliculas);
                idClasificacion = em.merge(idClasificacion);
            }
            for (Funciones funcionesListFunciones : peliculas.getFuncionesList()) {
                Peliculas oldIdPeliculaOfFuncionesListFunciones = funcionesListFunciones.getIdPelicula();
                funcionesListFunciones.setIdPelicula(peliculas);
                funcionesListFunciones = em.merge(funcionesListFunciones);
                if (oldIdPeliculaOfFuncionesListFunciones != null) {
                    oldIdPeliculaOfFuncionesListFunciones.getFuncionesList().remove(funcionesListFunciones);
                    oldIdPeliculaOfFuncionesListFunciones = em.merge(oldIdPeliculaOfFuncionesListFunciones);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Peliculas peliculas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Peliculas persistentPeliculas = em.find(Peliculas.class, peliculas.getId());
            Genero idGeneroOld = persistentPeliculas.getIdGenero();
            Genero idGeneroNew = peliculas.getIdGenero();
            Clasificaciones idClasificacionOld = persistentPeliculas.getIdClasificacion();
            Clasificaciones idClasificacionNew = peliculas.getIdClasificacion();
            List<Funciones> funcionesListOld = persistentPeliculas.getFuncionesList();
            List<Funciones> funcionesListNew = peliculas.getFuncionesList();
            if (idGeneroNew != null) {
                idGeneroNew = em.getReference(idGeneroNew.getClass(), idGeneroNew.getId());
                peliculas.setIdGenero(idGeneroNew);
            }
            if (idClasificacionNew != null) {
                idClasificacionNew = em.getReference(idClasificacionNew.getClass(), idClasificacionNew.getId());
                peliculas.setIdClasificacion(idClasificacionNew);
            }
            List<Funciones> attachedFuncionesListNew = new ArrayList<Funciones>();
            for (Funciones funcionesListNewFuncionesToAttach : funcionesListNew) {
                funcionesListNewFuncionesToAttach = em.getReference(funcionesListNewFuncionesToAttach.getClass(), funcionesListNewFuncionesToAttach.getId());
                attachedFuncionesListNew.add(funcionesListNewFuncionesToAttach);
            }
            funcionesListNew = attachedFuncionesListNew;
            peliculas.setFuncionesList(funcionesListNew);
            peliculas = em.merge(peliculas);
            if (idGeneroOld != null && !idGeneroOld.equals(idGeneroNew)) {
                idGeneroOld.getPeliculasList().remove(peliculas);
                idGeneroOld = em.merge(idGeneroOld);
            }
            if (idGeneroNew != null && !idGeneroNew.equals(idGeneroOld)) {
                idGeneroNew.getPeliculasList().add(peliculas);
                idGeneroNew = em.merge(idGeneroNew);
            }
            if (idClasificacionOld != null && !idClasificacionOld.equals(idClasificacionNew)) {
                idClasificacionOld.getPeliculasList().remove(peliculas);
                idClasificacionOld = em.merge(idClasificacionOld);
            }
            if (idClasificacionNew != null && !idClasificacionNew.equals(idClasificacionOld)) {
                idClasificacionNew.getPeliculasList().add(peliculas);
                idClasificacionNew = em.merge(idClasificacionNew);
            }
            for (Funciones funcionesListOldFunciones : funcionesListOld) {
                if (!funcionesListNew.contains(funcionesListOldFunciones)) {
                    funcionesListOldFunciones.setIdPelicula(null);
                    funcionesListOldFunciones = em.merge(funcionesListOldFunciones);
                }
            }
            for (Funciones funcionesListNewFunciones : funcionesListNew) {
                if (!funcionesListOld.contains(funcionesListNewFunciones)) {
                    Peliculas oldIdPeliculaOfFuncionesListNewFunciones = funcionesListNewFunciones.getIdPelicula();
                    funcionesListNewFunciones.setIdPelicula(peliculas);
                    funcionesListNewFunciones = em.merge(funcionesListNewFunciones);
                    if (oldIdPeliculaOfFuncionesListNewFunciones != null && !oldIdPeliculaOfFuncionesListNewFunciones.equals(peliculas)) {
                        oldIdPeliculaOfFuncionesListNewFunciones.getFuncionesList().remove(funcionesListNewFunciones);
                        oldIdPeliculaOfFuncionesListNewFunciones = em.merge(oldIdPeliculaOfFuncionesListNewFunciones);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = peliculas.getId();
                if (findPeliculas(id) == null) {
                    throw new NonexistentEntityException("The peliculas with id " + id + " no longer exists.");
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
            Peliculas peliculas;
            try {
                peliculas = em.getReference(Peliculas.class, id);
                peliculas.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The peliculas with id " + id + " no longer exists.", enfe);
            }
            Genero idGenero = peliculas.getIdGenero();
            if (idGenero != null) {
                idGenero.getPeliculasList().remove(peliculas);
                idGenero = em.merge(idGenero);
            }
            Clasificaciones idClasificacion = peliculas.getIdClasificacion();
            if (idClasificacion != null) {
                idClasificacion.getPeliculasList().remove(peliculas);
                idClasificacion = em.merge(idClasificacion);
            }
            List<Funciones> funcionesList = peliculas.getFuncionesList();
            for (Funciones funcionesListFunciones : funcionesList) {
                funcionesListFunciones.setIdPelicula(null);
                funcionesListFunciones = em.merge(funcionesListFunciones);
            }
            em.remove(peliculas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Peliculas> findPeliculasEntities() {
        return findPeliculasEntities(true, -1, -1);
    }

    public List<Peliculas> findPeliculasEntities(int maxResults, int firstResult) {
        return findPeliculasEntities(false, maxResults, firstResult);
    }

    private List<Peliculas> findPeliculasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Peliculas.class));
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

    public Peliculas findPeliculas(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Peliculas.class, id);
        } finally {
            em.close();
        }
    }

    public int getPeliculasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Peliculas> rt = cq.from(Peliculas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
