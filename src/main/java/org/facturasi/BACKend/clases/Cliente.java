package org.facturasi.BACKend.clases;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Cliente implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private int idCliente;
    String image;
    private String nombre;
private  String apellidos;
private String direccion;
private  String correo;
private int telefono;
@OneToMany(mappedBy = "cliente", fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
transient private List<Factura> facturas;

public Cliente() {

}

    public Cliente( String image, String nombre, String apellidos, String direccion, String correo, int telefono) {
        this.idCliente = 0;
        this.image = image;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.correo = correo;
        this.telefono = telefono;
        this.facturas = new ArrayList<>();
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public List<Factura> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<Factura> facturas) {
        this.facturas = facturas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return idCliente == cliente.idCliente && telefono == cliente.telefono && image.equals(cliente.image) && nombre.equals(cliente.nombre) && apellidos.equals(cliente.apellidos) && direccion.equals(cliente.direccion) && correo.equals(cliente.correo) && facturas.equals(cliente.facturas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCliente, image, nombre, apellidos, direccion, correo, telefono, facturas);
    }
}
