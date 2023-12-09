package org.facturasi.BACKend.clases;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class Detalle implements Serializable {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "num_detalle")
private int numDetalle;
@ManyToOne(fetch = FetchType.EAGER)
@JoinColumn(name = "id_factura")
private Factura factura;
@ManyToOne(fetch = FetchType.EAGER)
@JoinColumn(name = "id_producto")
private Producto producto;
private int cantidad;
@Column(name = "precio_total")
private double precioTotal;

public Detalle(){
}

    public Detalle(Factura factura, Producto producto, int cantidad) {
        this.numDetalle = 0;
        this.factura = factura;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioTotal = producto.getPrecio()*cantidad;
    }

    public int getNumDetalle() {
        return numDetalle;
    }

    public void setNumDetalle(int numDetalle) {
        this.numDetalle = numDetalle;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Detalle detalle = (Detalle) o;
        return numDetalle == detalle.numDetalle && cantidad == detalle.cantidad && Double.compare(detalle.precioTotal, precioTotal) == 0 && factura.equals(detalle.factura) && producto.equals(detalle.producto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numDetalle, factura, producto, cantidad, precioTotal);
    }
}
