package org.facturasi.FRONTend.views;


import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.facturasi.BACKend.clases.Cliente;
import org.facturasi.BACKend.clases.Factura;
import org.facturasi.BACKend.daos.ClienteDao;

import java.util.List;
import java.util.Set;

@PageTitle("ClienteView")
@Route(value = "ClienteView", layout = MainLayout.class)
public class ClienteView extends VerticalLayout {
    // LISTAS
    private List<Cliente> clientes;
    private List<Factura> Facturas;
    private Grid <Cliente> tablaCliente;
    // OBJETOS
    private Cliente cliente;
    private boolean editable;
    ClienteDao cd = new ClienteDao();

    // Campos de formulario
    private IntegerField idField;
    private TextField imageUrlField;
    private TextField nombreField;
    private TextField apellidosField;
    private TextField direccionField;
    private TextField correoField;
    private IntegerField telfField;



    public ClienteView(){
        //Titulo del panel
        H1 title1 = new H1("Clientes");
        title1.getStyle().set("1000", "var(--lumo-font-size-l)")
                .set("margin", "0");
        add(title1);
        // CARGA
        try {
            //Cargo los clientes de la base de datos
            clientes = cd.listarClientes();
        } catch (Exception e) {
            Notification notification = Notification.show("Error al cargar los clientes.");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            e.printStackTrace();
        }
        // PANELES
        VerticalLayout panelCrearModificarClientes = new VerticalLayout();
        panelCrearModificarClientes = panelCrearModificarClientes();

        HorizontalLayout panelButtons = new HorizontalLayout();
        panelButtons = panelButtons(panelCrearModificarClientes);

        tablaCliente = crearTablaClientes(panelCrearModificarClientes);


        add(title1, panelButtons, panelCrearModificarClientes,tablaCliente  );
        panelCrearModificarClientes.setVisible(false);
        panelButtons.setVisible(true);





    }


    private Grid<Cliente> crearTablaClientes(VerticalLayout panelCrearModificarClientes) {
        Grid<Cliente> tablaCliente = new Grid<>(Cliente.class, false);
        tablaCliente.setItems(clientes);
        tablaCliente.addColumn(Cliente::getIdCliente).setHeader("ID");
        tablaCliente.addColumn(createAvatarRenderer()).setHeader("Imagen").setAutoWidth(true).setFlexGrow(0);
        tablaCliente.addColumn(Cliente::getNombre).setHeader("Nombre");
        tablaCliente.addColumn(Cliente::getApellidos).setHeader("Apellidos");
        tablaCliente.addColumn(Cliente::getDireccion).setHeader("Dirección");
        tablaCliente.addColumn(Cliente::getTelefono).setHeader("Teléfono");
        tablaCliente.addThemeVariants(GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        tablaCliente.addColumn(new ComponentRenderer<>(Button::new, (buttonEliminar, clienteGrid) -> {
            buttonEliminar.addThemeVariants(ButtonVariant.LUMO_ERROR);
            buttonEliminar.addClickListener(e -> {
                try {
                    eliminarCliente(clienteGrid);
                    Notification notification = Notification.show("Se ha eliminado la película correctammente");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    tablaCliente.setItems(clientes);
                    tablaCliente.getDataProvider().refreshAll();

                } catch (NumberFormatException e1) {
                    Notification notification = Notification.show("Error al eliminar la película");
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            });

            buttonEliminar.setIcon(new Icon(VaadinIcon.TRASH));
        })).setHeader("Eliminar");
        tablaCliente.addColumn(new ComponentRenderer<>(Button::new, (buttonEditar, clienteGrid) -> {
            buttonEditar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            buttonEditar.addClickListener(e -> {

                try {
                    cliente = clienteGrid;

                    imageUrlField.setValue(cliente.getImage());
                    nombreField.setValue(cliente.getNombre());
                    apellidosField.setValue(cliente.getApellidos());
                    direccionField.setValue(cliente.getDireccion());
                    correoField.setValue(cliente.getCorreo());
                    telfField.setValue(cliente.getTelefono());

                    editable = true;
                    panelCrearModificarClientes.setVisible(true);

                } catch (Exception e1) {
                    Notification notification = Notification.show("No se puede editar.\nEditable = " + editable);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    e1.printStackTrace();
                }
                clientes = cd.listarClientes();
                tablaCliente.setItems(clientes);
                tablaCliente.getDataProvider().refreshAll();

            });
            buttonEditar.setIcon(new Icon(VaadinIcon.EDIT));
        })).setHeader("Editar");

        return tablaCliente;
    }

    private HorizontalLayout panelButtons(VerticalLayout panelCrearModificarClientes) {
        HorizontalLayout panelButtons = new HorizontalLayout();

        Button crearButton = new Button("Crear");
        crearButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        crearButton.addClickListener(e -> {
            panelCrearModificarClientes.setVisible(true);
            editable = false;
            vaciarCampos();

        });
        panelCrearModificarClientes.setVisible(false);
        panelButtons.add(crearButton);
        return panelButtons;
    }
    /**
     * muestra el panel para crear o modificar un cliente.
     * @return panelCrearModificarPeliculas
     */
    private VerticalLayout panelCrearModificarClientes() {
        VerticalLayout panelCrearModificarClientes = new VerticalLayout();
        FormLayout formularioCrearClientes = new FormLayout();
        Button aceptarModificacionCreacionButton = new Button("Aceptar");
        Button cancelarModificacionCreacionButtos = new Button("Cancelar");

        idField = new IntegerField("ID");
        idField.setVisible(false);
        nombreField = new TextField("Nombre");
        imageUrlField = new TextField("URL de imagen");
        apellidosField = new TextField("Apellido");
        direccionField = new TextField("Dirección");
        correoField = new TextField("Correo electrónico");
        telfField = new IntegerField("Número de teléfono");
        formularioCrearClientes.add(idField, imageUrlField, nombreField, apellidosField, direccionField, correoField, telfField);
        aceptarModificacionCreacionButton.addClickListener(click -> {
            if (editable == true) {
                try {

                    cliente.setNombre(nombreField.getValue());
                    cliente.setApellidos(apellidosField.getValue());
                    cliente.setDireccion(direccionField.getValue());
                    cliente.setCorreo(correoField.getValue());
                    cliente.setTelefono(telfField.getValue());
                    cliente.setImage(imageUrlField.getValue());
                    cd.guardarCliente(cliente);
                    clientes = cd.listarClientes();
                    refrescarTabla(panelCrearModificarClientes);

                    Notification notification = Notification.show("Cliente modificado correctamente");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                } catch (Exception e) {
                    Notification notification = Notification.show("Error al modificar el cliente");
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
                editable = false;
            } else {
                try {
                    Cliente cliente = new Cliente(imageUrlField.getValue(), nombreField.getValue(),
                            apellidosField.getValue(), direccionField.getValue(), correoField.getValue(),
                            telfField.getValue());
                    cd.guardarCliente(cliente);
                    clientes.add(cliente);
                    refrescarTabla(panelCrearModificarClientes);

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
            panelCrearModificarClientes.setVisible(false);
            vaciarCampos();
        });

        panelCrearModificarClientes.add(formularioCrearClientes, aceptarModificacionCreacionButton, cancelarModificacionCreacionButtos);
        return panelCrearModificarClientes;
    }
    private void refrescarTabla(VerticalLayout panelCrearModificarClientes) {
        tablaCliente.setItems(clientes);
        tablaCliente.getDataProvider().refreshAll();
        vaciarCampos();
        panelCrearModificarClientes.setVisible(false);
    }
    private Renderer<Cliente> createAvatarRenderer() {
        return LitRenderer.<Cliente> of(

                                "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                                        + "  <vaadin-avatar img=\"${item.pictureUrl}\" name=\"${item.fullName}\"></vaadin-avatar>"
                                        + "  <vaadin-vertical-layout style=\"line-height: var(--lumo-line-height-m);\">"
                                        + "    <span> ${item.fullName} </span>"
                                        + "    <span style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">"
                                        + "      ${item.email}" + "    </span>"
                                        + "  </vaadin-vertical-layout>"
                                        + "</vaadin-horizontal-layout>")
                .withProperty("pictureUrl", Cliente::getImage);
    }
    private void vaciarCampos() {
        imageUrlField.clear();
        nombreField.clear();
        apellidosField.clear();
        direccionField.clear();
        correoField.clear();
        telfField.clear();
    }
    /**
     * elimina un cliente de la tabla clientes
     * @param cliente
     */
    private void eliminarCliente(Cliente cliente) {
        try {
            cd.eliminarCliente(cliente);
            clientes.remove(cliente);
        } catch (Exception e) {
            Notification.show(e.getMessage());
        }
    }
}
