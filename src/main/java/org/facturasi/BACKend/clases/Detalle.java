package org.facturasi.BACKend.clases;

import javax.persistence.*;
@Entity
public class Detalle {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "num_detalle")
private int numDetalle;
@ManyToOne
@JoinColumn(name = "id_factura")
private Factura factura;
@ManyToOne
@JoinColumn(name = "id_producto")
private Producto producto;
private int cantidad;
@Column(name = "precio_total")
private double precioTotal;

public Detalle(){
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
}
