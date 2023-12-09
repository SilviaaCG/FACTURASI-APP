package org.facturasi.BACKend.daos;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.facturasi.BACKend.clases.Detalle;
import org.facturasi.BACKend.clases.Factura;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

public class FacturaDao {
    private static EntityManager em;
    private  static EntityManagerFactory emf;
    private static final Logger LOGGER = Logger.getLogger(FacturaDao.class);
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
    public static void guardarFactura(Factura factura){
        setUp();
        if (factura.getNumFactura() != 0){
            factura = em.merge(factura);
        }
        em.persist(factura);
        LOGGER.info("Se ha guardado una nueva factura del cliente " + factura.getCliente().getNombre() + ".\nNúmero de factura: " + factura.getNumFactura());
        close();
    }
    public void eliminarFactura(Factura factura){
        setUp();
        new ArrayList<Detalle>(DetalleDao.listarDetalles(factura.getNumFactura())).forEach(detalle -> {
                DetalleDao.eliminarDetalle(detalle);
                // Actualizar los detalles de la factura en la base de datos
        });
        factura = em.merge(factura);
        em.remove(factura);
        LOGGER.info("Se ha eliminado la factura número " +factura.getNumFactura());
        close();
    }
    public List<Factura> listarFacturas(){
        setUp();
        List<Factura> facturas = em.createQuery("SELECT f FROM Factura f",Factura.class).getResultList();
        close();

        return facturas;

    }
    public static Factura buscarFacturaPorId(int id){
        setUp();
        Factura factura = em.find(Factura.class,id);
        close();
        return factura;
    }
    public static Boolean pagar(Factura factura){
        if(!factura.isPagado()){
            factura.setPagado(true);
            setUp();
            guardarFactura(factura);
            close();
        }else{
            LOGGER.info("YA ESTA PAGADO!!");
        }
        return factura.isPagado();
    }
}
