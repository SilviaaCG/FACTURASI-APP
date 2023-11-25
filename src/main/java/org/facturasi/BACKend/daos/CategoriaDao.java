package org.facturasi.BACKend.daos;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.facturasi.BACKend.clases.Categoria;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class CategoriaDao {
    private static EntityManager em;
    private  static EntityManagerFactory emf;
    private static final Logger LOGGER = Logger.getLogger(CategoriaDao.class);
    private void setUp(){
        PropertyConfigurator.configure("src/main/resources/application.properties");
        emf = Persistence.createEntityManagerFactory("facturasi");
        em = emf.createEntityManager();
        em.getTransaction().begin();
    }
    private void close(){
        em.getTransaction().commit();
        em.close();
    }
    public void guardarCategoria(Categoria categoria){
        setUp();
        em.persist(categoria);
        LOGGER.info("Se ha guardado una nueva categoria con el nombre: " +categoria.getNombre() );
        close();
    }
    public void eliminarCategoria(Categoria categoria){
        setUp();
        categoria = em.merge(categoria);
        em.remove(categoria);
        LOGGER.info("Se ha eliminado la categoria con el nombre: " +categoria.getNombre());
        close();
    }
    public List<Categoria> listarCategoria(){
        setUp();
        List<Categoria> categorias = em.createQuery("SELECT c FROM Categoria c",Categoria.class).getResultList();
        close();
        return categorias;
    }
}
