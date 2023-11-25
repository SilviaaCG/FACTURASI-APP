package org.facturasi.FRONTend.views;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.facturasi.BACKend.clases.*;
import org.facturasi.BACKend.daos.DetalleDao;
import org.facturasi.BACKend.daos.FacturaDao;

import java.util.List;

@PageTitle("FacturaView")
@Route(value = "FacturaView", layout = MainLayout.class)
public class FacturaView extends VerticalLayout {
    //https://vaadin.com/docs/latest/components/dialog#draggable
    //Objetos
    private Producto producto;
    private Detalle detalle;
    private Factura factura;
    private boolean editable;

    //DAOs
    private FacturaDao fd = new FacturaDao();
    private DetalleDao dd = new DetalleDao();


    //Listas
    private List<Factura> facturas;
    private Grid<Factura> tablaFactura;

    //Campos de formulario
    //int numFactura, Cliente cliente, ModoPago numPago, List<Detalle> detalles
    private IntegerField idFacturaField;
    private Select<Cliente> clienteSelect;
    private Select<ModoPago> modoPagoSelect;
    //int numDetalle, Factura factura, Producto producto, int cantidad, double precioTotal
    private IntegerField idDetalleField;
    private Select<Producto> productoSelect;
    private IntegerField cantidadField;

public FacturaView(){
    //Titulo del panel
    H1 title1 = new H1("Facturas");
    title1.getStyle().set("1000", "var(--lumo-font-size-l)")
            .set("margin", "0");
    add(title1);
    // CARGA
    try {
        //Cargo las facturas de la base de datos
        facturas = fd.listarFacturas();
    } catch (Exception e) {
        Notification notification = Notification.show("Error al cargar las facturas.");
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        e.printStackTrace();
    }
    // PANELES
    VerticalLayout panelCrearModificarFacturas = new VerticalLayout();
    panelCrearModificarFacturas = panelCrearModificarFacturas();

    HorizontalLayout panelButtons = new HorizontalLayout();
    panelButtons = panelButtons(panelCrearModificarFacturas);

    tablaFactura = crearTablaFactura(panelCrearModificarFacturas);


    add(title1, panelButtons, panelCrearModificarFacturas,tablaFactura );
    panelCrearModificarFacturas.setVisible(false);
    panelButtons.setVisible(true);

}

    private Grid<Factura> crearTablaFactura(VerticalLayout panelCrearModificarFacturas) {
        Grid<Factura> tablaFactura = new Grid<>(Factura.class, false);
        if(facturas!=null){
            tablaFactura.setItems(facturas);
        }
        tablaFactura.addColumn(Factura::getNumFactura).setHeader("Número");
        tablaFactura.addColumn(factura -> factura.getCliente().getNombre()).setHeader("Cliente");
        tablaFactura.addColumn(Factura::getFechaCreacion).setHeader("Fecha de creación");
        tablaFactura.addColumn(factura -> factura.getNumPago().getNombre()).setHeader("Modo de pago");
        tablaFactura.addThemeVariants(GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        tablaFactura.addColumn(new ComponentRenderer<>(Button::new, (buttonEliminar, facturaGrid) -> {
            buttonEliminar.addThemeVariants(ButtonVariant.LUMO_ERROR);
            buttonEliminar.addClickListener(e -> {
                try {
                    eliminarFactura(facturaGrid);
                    Notification notification = Notification.show("Se ha eliminado la factura correctammente");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    tablaFactura.setItems(facturas);
                    tablaFactura.getDataProvider().refreshAll();
                } catch (NumberFormatException e1) {
                    Notification notification = Notification.show("Error al eliminar la factura");
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            });

            buttonEliminar.setIcon(new Icon(VaadinIcon.TRASH));
        })).setHeader("Eliminar");
        tablaFactura.addColumn(new ComponentRenderer<>(Button::new, (buttonEditar, facturaGrid) -> {
            buttonEditar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            buttonEditar.addClickListener(e -> {

                try {
                    factura = facturaGrid;
                    //LLENAR CAMPOS DE FORMULARIO
                    /**imageUrlField.setValue(cliente.getImage());
                    nombreField.setValue(cliente.getNombre());
                    apellidosField.setValue(cliente.getApellidos());
                    direccionField.setValue(cliente.getDireccion());
                    correoField.setValue(cliente.getCorreo());
                    telfField.setValue(cliente.getTelefono());
                    */
                    editable = true;
                    panelCrearModificarFacturas.setVisible(true);

                } catch (Exception e1) {
                    Notification notification = Notification.show("No se puede editar.\nEditable = " + editable);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    e1.printStackTrace();
                }
                actualizarTabla(panelCrearModificarFacturas);

            });
            buttonEditar.setIcon(new Icon(VaadinIcon.EDIT));
        })).setHeader("Editar");


        return tablaFactura;
    }

    private void eliminarFactura(Factura facturaGrid) {
        try {
            fd.eliminarFactura(facturaGrid);
            facturas.remove(facturaGrid);
        } catch (Exception e) {
            Notification.show(e.getMessage());
        }
    }

    private HorizontalLayout panelButtons(VerticalLayout panelCrearModificarFacturas) {
        HorizontalLayout panelButtons = new HorizontalLayout();
        Button crearButton = new Button("Crear");
        crearButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        crearButton.addClickListener(e -> {
            panelCrearModificarFacturas.setVisible(true);
            editable = false;
            vaciarCampos();

        });
        panelCrearModificarFacturas.setVisible(false);
        panelButtons.add(crearButton);
        return panelButtons;
    }

    private VerticalLayout panelCrearModificarFacturas() {
        VerticalLayout panelCrearModificarFacturas = new VerticalLayout();
        FormLayout formularioCrearFacturas = new FormLayout();
        Button aceptarModificacionCreacionButton = new Button("Aceptar");
        Button cancelarModificacionCreacionButtos = new Button("Cancelar");
        //En factura
        idFacturaField = new IntegerField("ID");
        idFacturaField.setVisible(false);
        clienteSelect = new Select<>();
        clienteSelect.setLabel("Cliente");
        modoPagoSelect = new Select<>();
        modoPagoSelect.setLabel("Modo de pago");
        //En detalle
        idDetalleField = new IntegerField();
        idDetalleField.setEnabled(false);
        productoSelect = new Select<>();
        productoSelect.setLabel("Producto");
        cantidadField = new IntegerField("Cantidad");
        cantidadField.setStepButtonsVisible(true);
        cantidadField.setMin(1);
        //cantidadField.setMax(producto.getStock());

        formularioCrearFacturas.add(idFacturaField, clienteSelect, modoPagoSelect, idDetalleField, productoSelect, cantidadField);

        aceptarModificacionCreacionButton.addClickListener(click -> {
            if (editable == true) {
                try {
                    //Primero se crea la factura, despues los detalles y por ultimo agregamos a la lista de detalles de la factura
                    factura.setCliente(clienteSelect.getValue());
                    factura.setNumPago(modoPagoSelect.getValue());
                    fd.guardarFactura(factura);
                    facturas = fd.listarFacturas();

                    /**detalle.setFactura(factura);
                    detalle.setProducto(productoSelect.getValue());
                    detalle.setCantidad(cantidadField.getValue());
                    dd.guardarDetalle(detalle);

                    factura.addDetalles(detalle);*/
                    actualizarTabla(panelCrearModificarFacturas);

                    Notification notification = Notification.show("Factura modificado correctamente");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                } catch (Exception e) {
                    Notification notification = Notification.show("Error al modificar la factura");
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
                editable = false;
            } else {
                try {
                    Factura factura = new Factura(clienteSelect.getValue(),modoPagoSelect.getValue());
                    fd.guardarFactura(factura);
                    facturas.add(factura);
                    actualizarTabla(panelCrearModificarFacturas);
                    Notification notification = Notification.show("Cliente creado correctamente :)");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                } catch (Exception exceptionCrearPelicula) {
                    Notification notification = Notification.show("Error al crear el cliente");
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }

            }
        });
        aceptarModificacionCreacionButton.addClickShortcut(Key.ENTER);
        cancelarModificacionCreacionButtos.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelarModificacionCreacionButtos.addClickListener(cancelar ->{
            panelCrearModificarFacturas.setVisible(false);
            vaciarCampos();
        });
        panelCrearModificarFacturas.add(formularioCrearFacturas, aceptarModificacionCreacionButton, cancelarModificacionCreacionButtos);
        return  panelCrearModificarFacturas;
    }
    private void actualizarTabla(VerticalLayout panelCrearModificarFacturas) {
        tablaFactura.setItems(facturas);
        tablaFactura.getDataProvider().refreshAll();
        vaciarCampos();
        panelCrearModificarFacturas.setVisible(false);
    }

    private void vaciarCampos() {
    }

}
