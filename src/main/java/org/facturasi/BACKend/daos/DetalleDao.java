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
    private static void setUp(){
        PropertyConfigurator.configure("src/main/resources/application.properties");
        emf = Persistence.createEntityManagerFactory("facturasi");
        em = emf.createEntityManager();
        em.getTransaction().begin();
    }
    private static void close(){
        em.getTransaction().commit();
        em.close();
    }
    public static void guardarDetalle(Detalle detalle){
        setUp();
        em.persist(detalle);
        LOGGER.info("Se ha guardado un nuevo detalle del producto "+ detalle.getProducto().getNombre() + " a la factura número " + detalle.getFactura().getNumFactura());
        close();
    }
    public static void eliminarDetalle(Detalle detalle){
        setUp();

        detalle = em.merge(detalle);
        em.remove(detalle);
        LOGGER.info("Se ha eliminado el detalle con número " + detalle.getNumDetalle());
        close();
    }
    public static List<Detalle> listarDetalles(int numFactura){
        setUp();
        List<Detalle> detalles = em.createQuery("SELECT d FROM Detalle d WHERE id_factura = :numFactura ",Detalle.class).setParameter("numFactura",numFactura).getResultList();
        close();
        return detalles;
    }

}
