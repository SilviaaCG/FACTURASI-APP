package org.facturasi.BACKend.daos;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.facturasi.BACKend.clases.Producto;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class ProductoDao {
    private static EntityManager em;
    private  static EntityManagerFactory emf;
    private static final Logger LOGGER = Logger.getLogger(ProductoDao.class);
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
    public void guardarProducto(Producto producto){
        setUp();
        if (producto.getIdProducto() != 0 ) {
            producto = em.merge(producto);
        }
        em.persist(producto);
        LOGGER.info("Se ha guardado un nuevo producto con el nombre: " + producto.getNombre());
        close();
    }
    public void eliminarProducto(Producto producto){
        setUp();
        producto = em.merge(producto);
        em.remove(producto);
        LOGGER.info("Se ha eliminado la categoria con el nombre: " + producto.getNombre());
        close();
    }
    public List<Producto> listarProductos(){
        setUp();
        List<Producto> productos = em.createQuery("SELECT p FROM Producto p",Producto.class).getResultList();
        close();
        return productos;
    }
}
