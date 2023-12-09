package org.facturasi.FRONTend.views;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.facturasi.BACKend.clases.Detalle;
import org.facturasi.BACKend.clases.Factura;
import org.facturasi.BACKend.clases.Producto;
import org.facturasi.BACKend.daos.DetalleDao;
import org.facturasi.BACKend.daos.FacturaDao;
import org.facturasi.BACKend.daos.ProductoDao;
import org.glassfish.jaxb.core.v2.TODO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@PageTitle("FacturaFichaView")
@Route(value = "FacturaFichaView", layout = MainLayout.class)
public class FacturaFichaView extends VerticalLayout implements HasUrlParameter<Integer> {

    Factura factura;
    H1 titulo;
    HorizontalLayout fichaFactura;
    Detalle detalle;
    int max = 0;

    //Optional<Alquiler> alquiler;
    //Listas
    List<Detalle> detallesDeFactura;
    List<Producto> productos;
    Grid<Detalle> tablaDetalles;


    //FORMULARIO DETALLE

    ComboBox<Producto> comboBoxProductos;
    //private Select<Producto> productoSelect;
    private IntegerField cantidad;

    @Override
    public void setParameter(BeforeEvent beforeEvent, Integer id) {
        Factura factura = FacturaDao.buscarFacturaPorId(id);
        titulo = new H1();
        titulo.setText(String.valueOf(factura.getNumFactura()));
        try {
            //detallesDeFactura = DetalleDao.listarDetalles(factura.getNumFactura());

            detallesDeFactura = DetalleDao.listarDetalles(factura.getNumFactura());
            productos = ProductoDao.listarProductos();
            fichaFactura = fichaFactura(factura);

            add(fichaFactura);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private HorizontalLayout fichaFactura(Factura factura) throws IOException {
        HorizontalLayout fichaPelicula = new HorizontalLayout();
        fichaPelicula.setWidth("100%");
        H2 clienteTxt = new H2("Cliente:");
        Avatar cliente = new Avatar(factura.getCliente().getNombre(),
                factura.getCliente().getImage());
        cliente.setWidth("50px");
        cliente.setHeight("50px");
        H3 nombreCliente = new H3(factura.getCliente().getNombre());
        Dialog panelAddDetalle = panelAddDetalle(factura);
        VerticalLayout ditails = DetailsBasic(factura,panelAddDetalle);
        fichaPelicula.add(clienteTxt,cliente,nombreCliente,ditails,panelAddDetalle);

        return fichaPelicula;
    }

    private Dialog panelAddDetalle(Factura factura) {
        Dialog content = new Dialog();
        Button acceptButton = new Button("Aceptar");
        Button cancelButton = new Button("Cancelar");
        cantidad = new IntegerField("Cantidad");
        comboBoxProductos = new ComboBox<>("Selecciona un producto");
        comboBoxProductos.setItems(productos);
        comboBoxProductos.setPattern("producto");
        comboBoxProductos.setItemLabelGenerator(Producto::getNombre);
        comboBoxProductos.setRenderer(new TextRenderer<>(Producto::getNombre));
        if (!productos.isEmpty()) {
            comboBoxProductos.setValue(productos.get(0));

        }

        acceptButton.addClickListener(click -> {
                try {
                    Detalle detalle = new Detalle(factura,comboBoxProductos.getValue(),cantidad.getValue());
                    DetalleDao.guardarDetalle(detalle);
                    detallesDeFactura.add(detalle);
                    refrescarTabla();
                    Notification notification = Notification.show("Producto añadido correctamente :)");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    content.close();
                } catch (Exception exceptionCrearPelicula) {
                    Notification notification = Notification.show("Error al añadir el producto");
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
        });
        acceptButton.addClickShortcut(Key.ENTER);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelButton.addClickListener(cancelar ->{
            content.close();
            vaciarCampos();
        });

        FormLayout formAddDetalle = new FormLayout(comboBoxProductos,cantidad);
        content.add(formAddDetalle);
        content.getFooter().add(acceptButton);
        content.getFooter().add(cancelButton);
        return content;
    }

    private void vaciarCampos() {

    }

    public VerticalLayout DetailsBasic(Factura factura, Dialog panelAddDetalle) {
        H3 metodoPago = new H3("Metodo de pago: \n" + factura.getNumPago().getNombre());
        H3 fechaCreacion = new H3("Fecha:\n"+ factura.getFechaCreacion());
        H3 productos = new H3("Detalles: \n");
        Button addDetalleButton = new Button(new Icon(VaadinIcon.PLUS));
        addDetalleButton.addClickListener(e->{
           panelAddDetalle.open();
        });
        tablaDetalles = new Grid<>(Detalle.class,false);
        tablaDetalles.setItems(detallesDeFactura);
        tablaDetalles.addColumn(detalle -> detalle.getProducto().getNombre()).setHeader("Nombre del producto");
        tablaDetalles.addColumn(Detalle::getCantidad).setHeader("Cantidad");
        tablaDetalles.addColumn(detalle -> detalle.getProducto().getPrecio()).setHeader("P.U.");
        tablaDetalles.addColumn(Detalle::getPrecioTotal).setHeader("Total");
        tablaDetalles.addColumn(new ComponentRenderer<>(Button::new, (buttonEliminar, DetalleGrid) -> {
            buttonEliminar.addThemeVariants(ButtonVariant.LUMO_ERROR);
            buttonEliminar.addClickListener(e -> {
                try {
                    eliminarDetalle(DetalleGrid);
                    Notification notification = Notification.show("Se ha eliminado el detalle correctammente");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    refrescarTabla();
                } catch (NumberFormatException e1) {
                    Notification notification = Notification.show("Error al eliminar el detalle");
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            });
            buttonEliminar.setIcon(new Icon(VaadinIcon.TRASH));
        })).setHeader("Eliminar");

        Button pagarFactura = new Button("Pagar");
        if (factura.isPagado()) {
            pagarFactura.setEnabled(false);
            addDetalleButton.setEnabled(false);
        }
        pagarFactura.addClickListener(event -> {
            FacturaDao.pagar(factura);
            pagarFactura.setEnabled(false);
            addDetalleButton.setEnabled(false);
            Notification notification = Notification.show("Factura pagada!");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });

        H3 total = new H3("Total: " + calcularTotal() + "€");
        HorizontalLayout panelPago = new HorizontalLayout(pagarFactura, total);

        VerticalLayout content = new VerticalLayout(metodoPago, productos,addDetalleButton,
                tablaDetalles,panelPago);
        return content;
    }

    private int calcularTotal() {
        int total = 0;
        for (Detalle detalle: detallesDeFactura){
            total+=detalle.getPrecioTotal();
        }
        return total;
    }

    private void eliminarDetalle(Detalle detalleGrid) {
        try {
            DetalleDao.eliminarDetalle(detalleGrid);
            detallesDeFactura.remove(detalleGrid);
        } catch (Exception e) {
            Notification.show(e.getMessage());
        }
    }

    private void refrescarTabla() {
        tablaDetalles.setItems(detallesDeFactura);
        tablaDetalles.getDataProvider().refreshAll();
        vaciarCampos();
    }



}
