package org.facturasi.BACKend.daos;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.facturasi.BACKend.clases.Factura;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class FacturaDao {
    private static EntityManager em;
    private  static EntityManagerFactory emf;
    private static final Logger LOGGER = Logger.getLogger(FacturaDao.class);
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
    public void guardarFactura(Factura factura){
        setUp();
        em.persist(factura);
        LOGGER.info("Se ha guardado una nueva factura del cliente " + factura.getCliente().getNombre() + ".\nNúmero de factura: " + factura.getNumFactura());
        close();
    }
    public void eliminarFactura(Factura factura){
        setUp();
        factura = em.merge(factura);
        em.remove(factura);
        LOGGER.info("Se ha eliminado la factura número " +factura.getNumFactura());
        close();
    }
    public List<Factura> listarFacturas(){
        setUp();
        List<Factura> facturas = em.createQuery("SELECT f FROM f",Factura.class).getResultList();
        close();
        return facturas;
    }
}
