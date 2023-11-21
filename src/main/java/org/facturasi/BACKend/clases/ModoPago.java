package org.facturasi.BACKend.clases;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name = "modo_pago")
public class ModoPago implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num_pago")
    private int numPago;
    private String nombre;
    private String detalles;

    public ModoPago() {

    }

    public int getNumPago() {
        return numPago;
    }

    public void setNumPago(int numPago) {
        this.numPago = numPago;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }
}
