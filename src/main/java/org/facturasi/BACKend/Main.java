package org.facturasi.BACKend;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import org.facturasi.BACKend.clases.*;
import org.facturasi.BACKend.daos.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        //Inicializo los daos
        ModoPagoDao mpd = new ModoPagoDao();
        FacturaDao fd = new FacturaDao();
        ClienteDao cd = new ClienteDao();
        DetalleDao dd = new DetalleDao();
        ProductoDao pd = new ProductoDao();
        CategoriaDao catd = new CategoriaDao();



    //CLIENTE
    Cliente cliente = new Cliente();
    cliente.setNombre("Greis");
    cliente.setApellidos("asa");
    cliente.setDireccion("callegreis");
    cliente.setCorreo("greis@gmail.com");
    cliente.setTelefono(322441);
    cliente.setImage("https://cdn.pixabay.com/photo/2023/11/10/20/32/duck-8380065_1280.jpg");

    //Lista para las facturas de la Clase Cliente
    List<Factura> facturas = new ArrayList<>();
    //MODOS DE PAGO
    ModoPago efectivo = new ModoPago();
    efectivo.setNombre("efectivo");
    ModoPago tarjeta = new ModoPago();
    tarjeta.setNombre("tarjeta visa");
    mpd.guardarModoPago(efectivo);
    mpd.guardarModoPago(tarjeta);
    //Facturas
    Factura factura1 = new Factura();
    factura1.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
    factura1.setNumPago(efectivo);

    Factura factura2 = new Factura();
    factura2.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
    factura2.setNumPago(tarjeta);
    //AGREGANDO FACTURAS A CLIENTE
    facturas.add(factura1);
    facturas.add(factura2);
    cliente.setFacturas(facturas);
    //AGREGANDO CLIENTE A FACTURAS
        for (Factura factura : facturas) {
            factura.setCliente(cliente);
        }
        //CATEGORIAS
        Categoria categoria = new Categoria();
        categoria.setNombre("Tarjetas graficas");
        categoria.setDescripcion("GPU");
        Categoria categoria1 = new Categoria();
        categoria1.setNombre("Procesadores");
        categoria1.setDescripcion("CPU");

        //PRODUCTOS
        Producto producto1 = new Producto();
        producto1.setNombre("Tarjeta gráfica RTX3060");
        producto1.setStock(20);
        producto1.setPrecio(308.95);
        producto1.setCategoria(categoria);
        Producto producto2 = new Producto();
        producto2.setNombre("Procesador Intel9 10343");
        producto2.setStock(20);
        producto2.setPrecio(400.0);
        producto2.setCategoria(categoria1);

        //DETALLES
        int cantidad = 2;
        Detalle detalle1 = new Detalle();
        detalle1.setFactura(factura1);
        detalle1.setProducto(producto1);
        detalle1.setCantidad(cantidad);
        detalle1.setPrecioTotal(cantidad*producto1.getPrecio());
        Detalle detalle2 = new Detalle();
        detalle2.setFactura(factura1);
        detalle2.setProducto(producto2);
        detalle2.setCantidad(cantidad);
        detalle2.setPrecioTotal(cantidad*producto1.getPrecio());

        //AGREGO DETALLES A FACTURA
        List<Detalle> detalles = new ArrayList<>();
        detalles.add(detalle1);
        detalles.add(detalle2);
        factura1.setDetalles(detalles);

        catd.guardarCategoria(categoria);
        catd.guardarCategoria(categoria1);
        pd.guardarProducto(producto1);
        pd.guardarProducto(producto2);

        cd.guardarCliente(cliente);
        fd.guardarFactura(factura1);
        fd.guardarFactura(factura2);
        dd.guardarDetalle(detalle1);
        dd.guardarDetalle(detalle2);

        //cd.eliminarCliente(cliente);
        // Generar el PDF
        String file = "C:\\Users\\silvia\\Downloads\\sample_pdf.pdf";
        createPdf(file);
        /**
    List<Cliente> clientes = new ArrayList<>();
    clientes = cd.listarClientes();
    for (Cliente cliente: clientes){
        System.out.println(cliente.getIdCliente());
    }*/

    }

    private static void createPdf(String file) throws IOException {
        PdfWriter writer = new PdfWriter(file) ;
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc);
        PdfFont myFont = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);

        Paragraph p1 = new Paragraph();
        p1.add("Hello, This is Delftstack!");
        p1.setTextAlignment(TextAlignment.CENTER);
        p1.setFont(myFont);
        p1.setFontSize(28);
        doc.add(p1);

        Paragraph p2 = new Paragraph();
        p2.add("We help you understand the concepts.");
        p2.setFontSize(18);
        doc.add(p2);

        doc.close();
    }


}