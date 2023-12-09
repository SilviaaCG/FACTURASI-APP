package org.facturasi.BACKend.clases;

import org.facturasi.BACKend.enumerados.Categoria;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Producto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private int idProducto;
    private String imageURL;
    private String nombre;
    private double precio;
    private int stock;
    private Categoria categoria;
    @OneToMany(mappedBy = "producto",fetch = FetchType.EAGER ,cascade = CascadeType.REMOVE)
    private List<Detalle> detalles;

    public Producto(){

    }

    public Producto(String imageURL, String nombre, double precio, int stock, Categoria categoria) {
        this.idProducto = 0;
        this.imageURL = imageURL;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
        this.detalles = new ArrayList<>();
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getImageURL() {
        return imageURL;
    }
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return idProducto == producto.idProducto && Double.compare(producto.precio, precio) == 0 && stock == producto.stock && imageURL.equals(producto.imageURL) && nombre.equals(producto.nombre) && categoria == producto.categoria && detalles.equals(producto.detalles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProducto, imageURL, nombre, precio, stock, categoria, detalles);
    }
}
