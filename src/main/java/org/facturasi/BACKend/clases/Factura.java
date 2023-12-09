package org.facturasi.BACKend.clases;

import org.facturasi.BACKend.enumerados.ModoPago;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Factura implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num_factura")
    private int numFactura;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;
    @Column(name = "fecha_creacion")
    private Timestamp fechaCreacion;

    private ModoPago numPago;
    private boolean pagado;
    @OneToMany(mappedBy = "factura",fetch = FetchType.EAGER ,cascade = CascadeType.REMOVE)
    transient private List<Detalle> detalles;

    public Factura(){
    }
    public Factura( Cliente cliente, ModoPago numPago) {
        this.numFactura = 0;
        this.cliente = cliente;
        this.fechaCreacion = new Timestamp(System.currentTimeMillis());
        this.numPago = numPago;
        this.pagado = false;
        this.detalles = new ArrayList<>();
    }

    public int getNumFactura() {
        return numFactura;
    }

    public void setNumFactura(int numFactura) {
        this.numFactura = numFactura;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public ModoPago getNumPago() {
        return numPago;
    }

    public void setNumPago(ModoPago numPago) {
        this.numPago = numPago;
    }

    public boolean isPagado() {
        return pagado;
    }

    public void setPagado(boolean pagado) {
        this.pagado = pagado;
    }

    public List<Detalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<Detalle> detalles) {
        this.detalles = detalles;
    }
    public void addDetalles(Detalle detalle){
        getDetalles().add(detalle);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Factura factura = (Factura) o;
        return numFactura == factura.numFactura && pagado == factura.pagado && cliente.equals(factura.cliente) && fechaCreacion.equals(factura.fechaCreacion) && numPago == factura.numPago && detalles.equals(factura.detalles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numFactura, cliente, fechaCreacion, numPago, pagado, detalles);
    }
}
