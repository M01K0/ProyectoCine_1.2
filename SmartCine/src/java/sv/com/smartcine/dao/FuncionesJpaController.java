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
import sv.com.smartcine.entidades.Salas;
import sv.com.smartcine.entidades.Reservaciones;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import sv.com.smartcine.dao.exceptions.IllegalOrphanException;
import sv.com.smartcine.dao.exceptions.NonexistentEntityException;
import sv.com.smartcine.entidades.Funciones;

public class FuncionesJpaController implements Serializable {

    public FuncionesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Funciones funciones) {
        if (funciones.getReservacionesList() == null) {
            funciones.setReservacionesList(new ArrayList<Reservaciones>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Peliculas idPelicula = funciones.getIdPelicula();
            if (idPelicula != null) {
                idPelicula = em.getReference(idPelicula.getClass(), idPelicula.getId());
                funciones.setIdPelicula(idPelicula);
            }
            Salas idSala = funciones.getIdSala();
            if (idSala != null) {
                idSala = em.getReference(idSala.getClass(), idSala.getId());
                funciones.setIdSala(idSala);
            }
            List<Reservaciones> attachedReservacionesList = new ArrayList<Reservaciones>();
            for (Reservaciones reservacionesListReservacionesToAttach : funciones.getReservacionesList()) {
                reservacionesListReservacionesToAttach = em.getReference(reservacionesListReservacionesToAttach.getClass(), reservacionesListReservacionesToAttach.getId());
                attachedReservacionesList.add(reservacionesListReservacionesToAttach);
            }
            funciones.setReservacionesList(attachedReservacionesList);
            em.persist(funciones);
            if (idPelicula != null) {
                idPelicula.getFuncionesList().add(funciones);
                idPelicula = em.merge(idPelicula);
            }
            if (idSala != null) {
                idSala.getFuncionesList().add(funciones);
                idSala = em.merge(idSala);
            }
            for (Reservaciones reservacionesListReservaciones : funciones.getReservacionesList()) {
                Funciones oldIdFuncionOfReservacionesListReservaciones = reservacionesListReservaciones.getIdFuncion();
                reservacionesListReservaciones.setIdFuncion(funciones);
                reservacionesListReservaciones = em.merge(reservacionesListReservaciones);
                if (oldIdFuncionOfReservacionesListReservaciones != null) {
                    oldIdFuncionOfReservacionesListReservaciones.getReservacionesList().remove(reservacionesListReservaciones);
                    oldIdFuncionOfReservacionesListReservaciones = em.merge(oldIdFuncionOfReservacionesListReservaciones);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Funciones funciones) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Funciones persistentFunciones = em.find(Funciones.class, funciones.getId());
            Peliculas idPeliculaOld = persistentFunciones.getIdPelicula();
            Peliculas idPeliculaNew = funciones.getIdPelicula();
            Salas idSalaOld = persistentFunciones.getIdSala();
            Salas idSalaNew = funciones.getIdSala();
            List<Reservaciones> reservacionesListOld = persistentFunciones.getReservacionesList();
            List<Reservaciones> reservacionesListNew = funciones.getReservacionesList();
            List<String> illegalOrphanMessages = null;
            for (Reservaciones reservacionesListOldReservaciones : reservacionesListOld) {
                if (!reservacionesListNew.contains(reservacionesListOldReservaciones)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Reservaciones " + reservacionesListOldReservaciones + " since its idFuncion field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idPeliculaNew != null) {
                idPeliculaNew = em.getReference(idPeliculaNew.getClass(), idPeliculaNew.getId());
                funciones.setIdPelicula(idPeliculaNew);
            }
            if (idSalaNew != null) {
                idSalaNew = em.getReference(idSalaNew.getClass(), idSalaNew.getId());
                funciones.setIdSala(idSalaNew);
            }
            List<Reservaciones> attachedReservacionesListNew = new ArrayList<Reservaciones>();
            for (Reservaciones reservacionesListNewReservacionesToAttach : reservacionesListNew) {
                reservacionesListNewReservacionesToAttach = em.getReference(reservacionesListNewReservacionesToAttach.getClass(), reservacionesListNewReservacionesToAttach.getId());
                attachedReservacionesListNew.add(reservacionesListNewReservacionesToAttach);
            }
            reservacionesListNew = attachedReservacionesListNew;
            funciones.setReservacionesList(reservacionesListNew);
            funciones = em.merge(funciones);
            if (idPeliculaOld != null && !idPeliculaOld.equals(idPeliculaNew)) {
                idPeliculaOld.getFuncionesList().remove(funciones);
                idPeliculaOld = em.merge(idPeliculaOld);
            }
            if (idPeliculaNew != null && !idPeliculaNew.equals(idPeliculaOld)) {
                idPeliculaNew.getFuncionesList().add(funciones);
                idPeliculaNew = em.merge(idPeliculaNew);
            }
            if (idSalaOld != null && !idSalaOld.equals(idSalaNew)) {
                idSalaOld.getFuncionesList().remove(funciones);
                idSalaOld = em.merge(idSalaOld);
            }
            if (idSalaNew != null && !idSalaNew.equals(idSalaOld)) {
                idSalaNew.getFuncionesList().add(funciones);
                idSalaNew = em.merge(idSalaNew);
            }
            for (Reservaciones reservacionesListNewReservaciones : reservacionesListNew) {
                if (!reservacionesListOld.contains(reservacionesListNewReservaciones)) {
                    Funciones oldIdFuncionOfReservacionesListNewReservaciones = reservacionesListNewReservaciones.getIdFuncion();
                    reservacionesListNewReservaciones.setIdFuncion(funciones);
                    reservacionesListNewReservaciones = em.merge(reservacionesListNewReservaciones);
                    if (oldIdFuncionOfReservacionesListNewReservaciones != null && !oldIdFuncionOfReservacionesListNewReservaciones.equals(funciones)) {
                        oldIdFuncionOfReservacionesListNewReservaciones.getReservacionesList().remove(reservacionesListNewReservaciones);
                        oldIdFuncionOfReservacionesListNewReservaciones = em.merge(oldIdFuncionOfReservacionesListNewReservaciones);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = funciones.getId();
                if (findFunciones(id) == null) {
                    throw new NonexistentEntityException("The funciones with id " + id + " no longer exists.");
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
            Funciones funciones;
            try {
                funciones = em.getReference(Funciones.class, id);
                funciones.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The funciones with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Reservaciones> reservacionesListOrphanCheck = funciones.getReservacionesList();
            for (Reservaciones reservacionesListOrphanCheckReservaciones : reservacionesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Funciones (" + funciones + ") cannot be destroyed since the Reservaciones " + reservacionesListOrphanCheckReservaciones + " in its reservacionesList field has a non-nullable idFuncion field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Peliculas idPelicula = funciones.getIdPelicula();
            if (idPelicula != null) {
                idPelicula.getFuncionesList().remove(funciones);
                idPelicula = em.merge(idPelicula);
            }
            Salas idSala = funciones.getIdSala();
            if (idSala != null) {
                idSala.getFuncionesList().remove(funciones);
                idSala = em.merge(idSala);
            }
            em.remove(funciones);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Funciones> findFuncionesEntities() {
        return findFuncionesEntities(true, -1, -1);
    }

    public List<Funciones> findFuncionesEntities(int maxResults, int firstResult) {
        return findFuncionesEntities(false, maxResults, firstResult);
    }

    private List<Funciones> findFuncionesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Funciones.class));
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

    public Funciones findFunciones(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Funciones.class, id);
        } finally {
            em.close();
        }
    }

    public int getFuncionesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Funciones> rt = cq.from(Funciones.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
