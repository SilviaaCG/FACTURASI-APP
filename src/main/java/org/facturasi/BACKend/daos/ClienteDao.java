package org.facturasi.BACKend.daos;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.facturasi.BACKend.clases.Cliente;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class ClienteDao {
    private static EntityManager em;
    private  static EntityManagerFactory emf;
    private static final Logger LOGGER = Logger.getLogger(ClienteDao.class);
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
    public void guardarCliente(Cliente cliente){
        setUp();
        em.persist(cliente);
        LOGGER.info("Se ha guardado un nuevo cliente con el id " + cliente.getIdCliente());
        close();
    }
    public void eliminarCliente(Cliente cliente){
        setUp();
        cliente = em.merge(cliente);
        em.remove(cliente);
        LOGGER.info("Se ha eliminado el cliente con el id " + cliente.getIdCliente());
        close();
    }
    public List<Cliente> listarClientes(){
        setUp();
        List<Cliente> clientes = em.createQuery("SELECT f FROM f",Cliente.class).getResultList();
        close();
        return clientes;
    }


}
