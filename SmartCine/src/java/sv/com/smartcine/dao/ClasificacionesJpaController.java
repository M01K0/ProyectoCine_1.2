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
import sv.com.smartcine.entidades.Peliculas;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import sv.com.smartcine.dao.exceptions.NonexistentEntityException;
import sv.com.smartcine.entidades.Clasificaciones;

public class ClasificacionesJpaController implements Serializable {

    public ClasificacionesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Clasificaciones clasificaciones) {
        if (clasificaciones.getPeliculasList() == null) {
            clasificaciones.setPeliculasList(new ArrayList<Peliculas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Peliculas> attachedPeliculasList = new ArrayList<Peliculas>();
            for (Peliculas peliculasListPeliculasToAttach : clasificaciones.getPeliculasList()) {
                peliculasListPeliculasToAttach = em.getReference(peliculasListPeliculasToAttach.getClass(), peliculasListPeliculasToAttach.getId());
                attachedPeliculasList.add(peliculasListPeliculasToAttach);
            }
            clasificaciones.setPeliculasList(attachedPeliculasList);
            em.persist(clasificaciones);
            for (Peliculas peliculasListPeliculas : clasificaciones.getPeliculasList()) {
                Clasificaciones oldIdClasificacionOfPeliculasListPeliculas = peliculasListPeliculas.getIdClasificacion();
                peliculasListPeliculas.setIdClasificacion(clasificaciones);
                peliculasListPeliculas = em.merge(peliculasListPeliculas);
                if (oldIdClasificacionOfPeliculasListPeliculas != null) {
                    oldIdClasificacionOfPeliculasListPeliculas.getPeliculasList().remove(peliculasListPeliculas);
                    oldIdClasificacionOfPeliculasListPeliculas = em.merge(oldIdClasificacionOfPeliculasListPeliculas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Clasificaciones clasificaciones) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Clasificaciones persistentClasificaciones = em.find(Clasificaciones.class, clasificaciones.getId());
            List<Peliculas> peliculasListOld = persistentClasificaciones.getPeliculasList();
            List<Peliculas> peliculasListNew = clasificaciones.getPeliculasList();
            List<Peliculas> attachedPeliculasListNew = new ArrayList<Peliculas>();
            for (Peliculas peliculasListNewPeliculasToAttach : peliculasListNew) {
                peliculasListNewPeliculasToAttach = em.getReference(peliculasListNewPeliculasToAttach.getClass(), peliculasListNewPeliculasToAttach.getId());
                attachedPeliculasListNew.add(peliculasListNewPeliculasToAttach);
            }
            peliculasListNew = attachedPeliculasListNew;
            clasificaciones.setPeliculasList(peliculasListNew);
            clasificaciones = em.merge(clasificaciones);
            for (Peliculas peliculasListOldPeliculas : peliculasListOld) {
                if (!peliculasListNew.contains(peliculasListOldPeliculas)) {
                    peliculasListOldPeliculas.setIdClasificacion(null);
                    peliculasListOldPeliculas = em.merge(peliculasListOldPeliculas);
                }
            }
            for (Peliculas peliculasListNewPeliculas : peliculasListNew) {
                if (!peliculasListOld.contains(peliculasListNewPeliculas)) {
                    Clasificaciones oldIdClasificacionOfPeliculasListNewPeliculas = peliculasListNewPeliculas.getIdClasificacion();
                    peliculasListNewPeliculas.setIdClasificacion(clasificaciones);
                    peliculasListNewPeliculas = em.merge(peliculasListNewPeliculas);
                    if (oldIdClasificacionOfPeliculasListNewPeliculas != null && !oldIdClasificacionOfPeliculasListNewPeliculas.equals(clasificaciones)) {
                        oldIdClasificacionOfPeliculasListNewPeliculas.getPeliculasList().remove(peliculasListNewPeliculas);
                        oldIdClasificacionOfPeliculasListNewPeliculas = em.merge(oldIdClasificacionOfPeliculasListNewPeliculas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = clasificaciones.getId();
                if (findClasificaciones(id) == null) {
                    throw new NonexistentEntityException("The clasificaciones with id " + id + " no longer exists.");
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
            Clasificaciones clasificaciones;
            try {
                clasificaciones = em.getReference(Clasificaciones.class, id);
                clasificaciones.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clasificaciones with id " + id + " no longer exists.", enfe);
            }
            List<Peliculas> peliculasList = clasificaciones.getPeliculasList();
            for (Peliculas peliculasListPeliculas : peliculasList) {
                peliculasListPeliculas.setIdClasificacion(null);
                peliculasListPeliculas = em.merge(peliculasListPeliculas);
            }
            em.remove(clasificaciones);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Clasificaciones> findClasificacionesEntities() {
        return findClasificacionesEntities(true, -1, -1);
    }

    public List<Clasificaciones> findClasificacionesEntities(int maxResults, int firstResult) {
        return findClasificacionesEntities(false, maxResults, firstResult);
    }

    private List<Clasificaciones> findClasificacionesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Clasificaciones.class));
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

    public Clasificaciones findClasificaciones(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Clasificaciones.class, id);
        } finally {
            em.close();
        }
    }

    public int getClasificacionesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Clasificaciones> rt = cq.from(Clasificaciones.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
