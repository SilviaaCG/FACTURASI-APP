package org.facturasi.FRONTend.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.facturasi.BACKend.clases.Producto;
import org.facturasi.BACKend.daos.ProductoDao;
import org.facturasi.BACKend.enumerados.Categoria;


import java.util.List;
import java.util.stream.Collectors;

@PageTitle("ProductoView")
@Route(value = "ProductoView", layout = MainLayout.class)
public class ProductoView extends Div {
    //Objetos
    private boolean editable;
    Producto producto;
    //DAOs

    //Listas
    VirtualList<Producto> list;
    private List<Producto> productos = ProductoDao.listarProductos();

    // Campos de formulario
    private IntegerField idField;
    private TextField imageUrlField;
    private TextField nombreField;
    private NumberField precioField;
    private IntegerField stockField;
    static Select<Categoria> categoriaMultiSelect;
    //Paneles
    VerticalLayout addProductForm;
    private ComponentRenderer<Component, Producto> productoCardRenderer = new ComponentRenderer<Component, Producto>(
            producto -> {
                HorizontalLayout cardLayout = new HorizontalLayout();
                cardLayout.setWidth("100%");
                cardLayout.setMargin(true);
                cardLayout.setMargin(true);
                Avatar avatar = new Avatar(producto.getNombre(),
                        producto.getImageURL());
                avatar.setHeight("80px");
                avatar.setWidth("80px");

                VerticalLayout infoLayout = new VerticalLayout();
                infoLayout.setSpacing(false);
                infoLayout.setPadding(false);
                infoLayout.getElement().appendChild(
                        ElementFactory.createStrong(producto.getNombre()));
                infoLayout.add(new Div(new Text(producto.getCategoria().getNombre())));
                infoLayout.add(new Div(new Text("Stock: " + producto.getStock())));
                infoLayout.add(new Div(new Text("Precio: " + producto.getPrecio())));
                infoLayout.add(new Div(new Text("Categoria: " + producto.getCategoria().getNombre())));
                cardLayout.add(avatar, infoLayout);
                cardLayout.getStyle().set("background-color", "lightblue");
                //Botones editar y eliminar
                Div botonesDiv = divBotones(producto);
                cardLayout.add(botonesDiv);

                return cardLayout;
            });

    private Div divBotones(Producto producto) {
        Div divBotones = new Div();
        divBotones.getStyle().set("margin-left", "auto");
        Button editButton = new Button(new Icon(VaadinIcon.EDIT));
        editButton.addClickListener(e->{
            editable = true;
            //llenar campos si se selecciona editar
                idField.setValue(producto.getIdProducto());
                nombreField.setValue(producto.getNombre());
                imageUrlField.setValue(producto.getImageURL());
                precioField.setValue(producto.getPrecio());
                stockField.setValue(producto.getStock());
                this.producto = producto;
                categoriaMultiSelect.setValue(producto.getCategoria());
            addProductForm.setVisible(true);
        });
        Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
        deleteButton.addClickListener(e->{
            ProductoDao.eliminarProducto(producto);
            productos.remove(producto);
            actualizarLista();
        });
        divBotones.add(editButton,deleteButton);
        return divBotones;
    }

    private HorizontalLayout panelButtons(){
        HorizontalLayout panelButtons = new HorizontalLayout();
        Button addButton = new Button("Crear");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(e->{
            editable = false;
            vaciarCampos();
            addProductForm.setVisible(true);
        });
        TextField searchButton = buscarProducto();
        panelButtons.add(addButton,searchButton);
        panelButtons.setVisible(true);
        panelButtons.setAlignItems(FlexComponent.Alignment.CENTER);
        return panelButtons;
    }
    private VerticalLayout addProductForm(){
        VerticalLayout addProductFormVLayaut = new VerticalLayout();
        FormLayout addProductForm = new FormLayout();
        Button aceptarModificacionCreacionButton = new Button("Aceptar");
        Button cancelarModificacionCreacionButtos = new Button("Cancelar");
        idField = new IntegerField("ID");
        idField.setVisible(false);
        nombreField = new TextField("Nombre");
        imageUrlField = new TextField("URL de imagen");
        precioField = new NumberField("Precio");
        precioField.setSuffixComponent(VaadinIcon.DOLLAR.create());
        stockField = new IntegerField("Stock");
        categoriaMultiSelect = new Select<>();
        categoriaMultiSelect.setLabel("Categoría");
        categoriaMultiSelect.addValueChangeListener(e -> {
            e.getValue();
        });
        categoriaMultiSelect.setItemLabelGenerator(Categoria::getNombre);
        categoriaMultiSelect.setItems(Categoria.values());
        addProductForm.add(idField, imageUrlField, nombreField, imageUrlField, precioField,stockField, categoriaMultiSelect);

        aceptarModificacionCreacionButton.addClickListener(click -> {
            if (!editable) {
                try {
                    Producto producto = new Producto(imageUrlField.getValue(), nombreField.getValue(), precioField.getValue(), stockField.getValue(), categoriaMultiSelect.getValue());
                    ProductoDao.guardarProducto(producto);
                    productos.add(producto);
                    addProductFormVLayaut.setVisible(false);
                    vaciarCampos();
                    actualizarLista();
                    Notification notification = Notification.show("Producto creado correctamente :)");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                } catch (Exception exceptionCrearPelicula) {
                    Notification notification = Notification.show("Error al crear el producto");
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    exceptionCrearPelicula.printStackTrace();
                }
            }else{
                try {
                    producto.setNombre(nombreField.getValue());
                    producto.setImageURL(imageUrlField.getValue());
                    producto.setCategoria(categoriaMultiSelect.getValue());
                    producto.setPrecio(precioField.getValue());
                    producto.setStock(stockField.getValue());
                    ProductoDao.guardarProducto(producto);
                    productos = ProductoDao.listarProductos();
                    actualizarLista();
                    addProductFormVLayaut.setVisible(false);
                    vaciarCampos();
                    Notification notification = Notification.show("Producto modificado correctamente");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                } catch (Exception e) {
                    Notification notification = Notification.show("Error al modificar el producto");
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    e.printStackTrace();
                }
                producto = null;
                editable = false;
            }

        });
        aceptarModificacionCreacionButton.addClickShortcut(Key.ENTER);
        cancelarModificacionCreacionButtos.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelarModificacionCreacionButtos.addClickListener(cancelar ->{
            addProductFormVLayaut.setVisible(false);
            vaciarCampos();
        });
        addProductFormVLayaut.add(addProductForm,aceptarModificacionCreacionButton,cancelarModificacionCreacionButtos);
        return addProductFormVLayaut;
    }

    private void actualizarLista() {

            // Configurar el proveedor de datos y actualizar la lista virtual
            ListDataProvider<Producto> dataProvider = new ListDataProvider<>(productos);
            list.setDataProvider(dataProvider);
            // Notificar al componente de la actualización
            list.getDataProvider().refreshAll();

    }

    private void vaciarCampos() {
        idField.clear();
        imageUrlField.clear();
        nombreField.clear();
        precioField.clear();
        stockField.clear();
        categoriaMultiSelect.clear();
    }
    private TextField buscarProducto(){
        // Crear un TextField para la búsqueda
        TextField searchField = new TextField();
        searchField.setSuffixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.addValueChangeListener(e -> filtrarLista(e.getValue()));
        return  searchField;
    }
    private void filtrarLista(String filtro) {
        List<Producto> resultadosFiltrados = productos.stream()
                .filter(producto -> producto.getNombre().toLowerCase().contains(filtro.toLowerCase()))
                .collect(Collectors.toList());

        ListDataProvider<Producto> dataProvider = new ListDataProvider<>(resultadosFiltrados);
        list.setDataProvider(dataProvider);
        // Notificar al componente de la actualización
        list.getDataProvider().refreshAll();
    }


    public ProductoView() {
        H1 title1 = new H1("Productos");
        title1.getStyle().set("1000", "var(--lumo-font-size-l)")
                .set("margin", "10");
        add(title1);
        HorizontalLayout panelButtons = new HorizontalLayout();
        panelButtons = panelButtons();
        list = new VirtualList<>();
        list.setItems(productos);
        list.setRenderer(productoCardRenderer);
        addProductForm = addProductForm();
        addProductForm.setVisible(false);

        add(title1,panelButtons,addProductForm,list);
    }


}
