package org.facturasi.BACKend.daos;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.facturasi.BACKend.clases.ModoPago;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class ModoPagoDao {
    private static EntityManager em;
    private  static EntityManagerFactory emf;
    private static final Logger LOGGER = Logger.getLogger(ModoPagoDao.class);
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
    public void guardarModoPago(ModoPago modoPago){
        setUp();
        em.persist(modoPago);
        LOGGER.info("Se ha guardado un nuevo modo de pago llamado: " + modoPago.getNombre());
        close();
    }
    public void eliminarModoPago(ModoPago modoPago){
        setUp();
        modoPago = em.merge(modoPago);
        em.remove(modoPago);
        LOGGER.info("Se ha eliminado el modo de pago llamado: " + modoPago.getNombre());
        close();
    }
    public List<ModoPago> listarModosPagos(){
        setUp();
        List<ModoPago> modosPagos = em.createQuery("SELECT f FROM f",ModoPago.class).getResultList();
        close();
        return modosPagos;
    }
}
