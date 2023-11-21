package org.facturasi.BACKend.daos;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.facturasi.BACKend.clases.Detalle;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class DetalleDao {
    private static EntityManager em;
    private  static EntityManagerFactory emf;
    private static final Logger LOGGER = Logger.getLogger(DetalleDao.class);
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
    public void guardarDetalle(Detalle detalle){
        setUp();
        em.persist(detalle);
        LOGGER.info("Se ha guardado un nuevo detalle del producto "+ detalle.getProducto().getNombre() + " a la factura número " + detalle.getFactura().getNumFactura());
        close();
    }
    public void eliminarDetalle(Detalle detalle){
        setUp();
        detalle = em.merge(detalle);
        em.remove(detalle);
        LOGGER.info("Se ha eliminado el detalle con número " + detalle.getNumDetalle());
        close();
    }
    public List<Detalle> listarDetalles(){
        setUp();
        List<Detalle> detalles = em.createQuery("SELECT f FROM f",Detalle.class).getResultList();
        close();
        return detalles;
    }

}
