package controller;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import application.Main;
import data.DBConnection;
import data.DBConnectionFactory;
import data.ExcelService;
import data.ProductoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Producto;
import model.UserSession;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

public class RegistroProductosController {

    @FXML
    private TableColumn<Producto, Integer> columnCantidad;

    @FXML
    private TableColumn<Producto, String> columnNombre; // Updated type to String

    @FXML
    private TableColumn<Producto, Double> columnPrecio;

    @FXML
    private TableView<Producto> tableProductos;

    @FXML
    private TableColumn<Producto, Double> columnPrecioE;

    @FXML
    private TableColumn<Producto, Integer> columnReferenciaE;
    
    @FXML
    private TableColumn<Producto, Integer> columnReferencia;

    @FXML
    private TableView<Producto> tableExcel;

    @FXML
    private TableColumn<Producto, String> columnNombreE;

    @FXML
    private Button cargarE;

    private Connection connection = DBConnectionFactory.getConnectionByRole(UserSession.getInstance().getRole())
            .getConnection();
    private ProductoDAO productoDAO = new ProductoDAO(connection);

    @FXML
    public void initialize() {
        // Make tableProductos editable
        tableProductos.setEditable(true);

        ObservableList<Producto> availableProductos = FXCollections.observableArrayList();
        for (Producto producto : productoDAO.fetch()) {
            availableProductos.add(producto);
        }

        // Bind columns to properties and make them editable
        columnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnReferencia.setCellValueFactory(new PropertyValueFactory<>("referencia"));
        columnNombre.setCellFactory(TextFieldTableCell.forTableColumn());
        columnNombre.setOnEditCommit(event -> {
            Producto product = event.getRowValue();
            product.setNombre(event.getNewValue());
            productoDAO.update(product);
        });

        columnPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        columnPrecio.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        columnPrecio.setOnEditCommit(event -> {
            Producto product = event.getRowValue();
            product.setPrecio(event.getNewValue());
            productoDAO.update(product);        });

        columnCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        columnCantidad.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        columnCantidad.setOnEditCommit(event -> {
            Producto product = event.getRowValue();
            product.setCantidad(event.getNewValue());
            productoDAO.update(product);        });

        // Set data to TableView
        tableProductos.setItems(availableProductos);
    }


    private void cargarProductos() {
        ObservableList<Producto> availableProductos = FXCollections.observableArrayList();
        for (Producto producto : productoDAO.fetch()) {
            availableProductos.add(producto);
        }
        tableProductos.setItems(availableProductos);
    }

    @FXML
    void eliminar(ActionEvent event) {
        if (!tableProductos.getSelectionModel().isEmpty()) {
            Producto producto = tableProductos.getSelectionModel().getSelectedItem();
            productoDAO.delete(producto.getReferencia());
            Main.showAlert("Eliminacion Exitosa", "Eliminacion Exitosa", "El equipo se elimino satisfactoriamente",
                    Alert.AlertType.CONFIRMATION);
            initialize();
        } else {
            Main.showAlert("Seleccione un registro", "Seleccione un registro", "Debe seleccionar un dato de la tabla",
                    Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void registrar(ActionEvent event) {
        if (!tableExcel.getSelectionModel().isEmpty()) {
            Producto seleccionado = tableExcel.getSelectionModel().getSelectedItem();

            if (!productoDAO.authenticate(seleccionado.getReferencia())) {
                Producto nuevoProducto = new Producto(seleccionado.getReferencia(), seleccionado.getNombre(),
                        seleccionado.getPrecio(), seleccionado.getCantidad());

                productoDAO.save(nuevoProducto);
                Main.showAlert("Registro Exitoso", "Registro Exitoso", "El producto se registró satisfactoriamente.",
                        Alert.AlertType.CONFIRMATION);
                initialize();
            } else {
                Main.showAlert("Referencia repetida", "Referencia repetida", "Debe registrar una referencia diferente.",
                        Alert.AlertType.WARNING);
            }
        } else {
            Main.showAlert("Sin selección", "Debe seleccionar un producto de la tabla Excel.", "",
                    Alert.AlertType.WARNING);
        }
    }

    @FXML
    void actualizar(ActionEvent event) {
        // This method can now be removed since updates are handled via the TableView edit events
    }

    @FXML
    void crearExcel(ActionEvent event) {
        ExcelService.createExcelFormat("Producto.xlsx");
    }

    @FXML
    void cargarExcel(ActionEvent event) {
        FileChooser file = new FileChooser();
        file.setTitle("Seleccionar archivo de excel");
        file.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arhivos Excel", "*.xlsx"));

        Stage stage = (Stage) cargarE.getScene().getWindow();
        File archivoSeleccionado = file.showOpenDialog(stage);
        if (archivoSeleccionado != null) {
            ArrayList<Producto> productosExcel = ExcelService.fetchExcel(archivoSeleccionado);
            loadTableView(productosExcel);
        } else {
            Main.showAlert("Seleccione un archivo", "Seleccione un archivo", "Seleccione un archivo",
                    Alert.AlertType.WARNING);
        }
    }

    private void loadTableView(ArrayList<Producto> productos) {
        columnReferenciaE.setCellValueFactory(new PropertyValueFactory<>("referencia"));
        columnPrecioE.setCellValueFactory(new PropertyValueFactory<>("precio"));
        columnNombreE.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnNombreE.setCellFactory(TextFieldTableCell.forTableColumn());
        columnNombreE.setOnEditCommit(event -> {
            Producto product = event.getRowValue();
            product.setNombre(event.getNewValue());
            productoDAO.update(product);
        });

        tableExcel.getItems().setAll(productos);
        tableExcel.setEditable(true);
    }

    @FXML
    void cerrarSesion(ActionEvent event) {
        UserSession.getInstance().destroy();
        Main.loadView("/view/Login.fxml");
    }
}