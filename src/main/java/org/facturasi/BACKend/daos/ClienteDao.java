package org.facturasi.BACKend.daos;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.facturasi.BACKend.clases.Cliente;
import org.facturasi.BACKend.clases.Factura;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

public class ClienteDao {
    private static EntityManager em;
    private  static EntityManagerFactory emf;
    private static final Logger LOGGER = Logger.getLogger(ClienteDao.class);
    private FacturaDao fd = new FacturaDao();
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
        if (cliente.getIdCliente() != 0 ) {
            cliente = em.merge(cliente);
        }
        em.persist(cliente);
        LOGGER.info("Se ha guardado un nuevo cliente con el id " + cliente.getIdCliente());
        close();
    }
    public void eliminarCliente(Cliente cliente){
        setUp();
        new ArrayList<Factura>(fd.listarFacturas()).forEach(factura -> {
            if (factura.getCliente().getIdCliente() == cliente.getIdCliente()) {
                fd.eliminarFactura(factura);  // Actualizar la factura en la base de datos
            }
        });

        Cliente copiacliente = em.merge(cliente);
        em.remove(copiacliente);


        LOGGER.info("Se ha eliminado el cliente con el id " + cliente.getIdCliente());
        close();
    }
    public List<Cliente> listarClientes(){
        setUp();
        List<Cliente> clientes = em.createQuery("SELECT c FROM Cliente c",Cliente.class).getResultList();
        close();
        return clientes;
    }


}
