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
import sv.com.smartcine.entidades.Genero;

public class GeneroJpaController implements Serializable {

    public GeneroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Genero genero) {
        if (genero.getPeliculasList() == null) {
            genero.setPeliculasList(new ArrayList<Peliculas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Peliculas> attachedPeliculasList = new ArrayList<Peliculas>();
            for (Peliculas peliculasListPeliculasToAttach : genero.getPeliculasList()) {
                peliculasListPeliculasToAttach = em.getReference(peliculasListPeliculasToAttach.getClass(), peliculasListPeliculasToAttach.getId());
                attachedPeliculasList.add(peliculasListPeliculasToAttach);
            }
            genero.setPeliculasList(attachedPeliculasList);
            em.persist(genero);
            for (Peliculas peliculasListPeliculas : genero.getPeliculasList()) {
                Genero oldIdGeneroOfPeliculasListPeliculas = peliculasListPeliculas.getIdGenero();
                peliculasListPeliculas.setIdGenero(genero);
                peliculasListPeliculas = em.merge(peliculasListPeliculas);
                if (oldIdGeneroOfPeliculasListPeliculas != null) {
                    oldIdGeneroOfPeliculasListPeliculas.getPeliculasList().remove(peliculasListPeliculas);
                    oldIdGeneroOfPeliculasListPeliculas = em.merge(oldIdGeneroOfPeliculasListPeliculas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Genero genero) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Genero persistentGenero = em.find(Genero.class, genero.getId());
            List<Peliculas> peliculasListOld = persistentGenero.getPeliculasList();
            List<Peliculas> peliculasListNew = genero.getPeliculasList();
            List<Peliculas> attachedPeliculasListNew = new ArrayList<Peliculas>();
            for (Peliculas peliculasListNewPeliculasToAttach : peliculasListNew) {
                peliculasListNewPeliculasToAttach = em.getReference(peliculasListNewPeliculasToAttach.getClass(), peliculasListNewPeliculasToAttach.getId());
                attachedPeliculasListNew.add(peliculasListNewPeliculasToAttach);
            }
            peliculasListNew = attachedPeliculasListNew;
            genero.setPeliculasList(peliculasListNew);
            genero = em.merge(genero);
            for (Peliculas peliculasListOldPeliculas : peliculasListOld) {
                if (!peliculasListNew.contains(peliculasListOldPeliculas)) {
                    peliculasListOldPeliculas.setIdGenero(null);
                    peliculasListOldPeliculas = em.merge(peliculasListOldPeliculas);
                }
            }
            for (Peliculas peliculasListNewPeliculas : peliculasListNew) {
                if (!peliculasListOld.contains(peliculasListNewPeliculas)) {
                    Genero oldIdGeneroOfPeliculasListNewPeliculas = peliculasListNewPeliculas.getIdGenero();
                    peliculasListNewPeliculas.setIdGenero(genero);
                    peliculasListNewPeliculas = em.merge(peliculasListNewPeliculas);
                    if (oldIdGeneroOfPeliculasListNewPeliculas != null && !oldIdGeneroOfPeliculasListNewPeliculas.equals(genero)) {
                        oldIdGeneroOfPeliculasListNewPeliculas.getPeliculasList().remove(peliculasListNewPeliculas);
                        oldIdGeneroOfPeliculasListNewPeliculas = em.merge(oldIdGeneroOfPeliculasListNewPeliculas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = genero.getId();
                if (findGenero(id) == null) {
                    throw new NonexistentEntityException("The genero with id " + id + " no longer exists.");
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
            Genero genero;
            try {
                genero = em.getReference(Genero.class, id);
                genero.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The genero with id " + id + " no longer exists.", enfe);
            }
            List<Peliculas> peliculasList = genero.getPeliculasList();
            for (Peliculas peliculasListPeliculas : peliculasList) {
                peliculasListPeliculas.setIdGenero(null);
                peliculasListPeliculas = em.merge(peliculasListPeliculas);
            }
            em.remove(genero);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Genero> findGeneroEntities() {
        return findGeneroEntities(true, -1, -1);
    }

    public List<Genero> findGeneroEntities(int maxResults, int firstResult) {
        return findGeneroEntities(false, maxResults, firstResult);
    }

    private List<Genero> findGeneroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Genero.class));
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

    public Genero findGenero(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Genero.class, id);
        } finally {
            em.close();
        }
    }

    public int getGeneroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Genero> rt = cq.from(Genero.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
