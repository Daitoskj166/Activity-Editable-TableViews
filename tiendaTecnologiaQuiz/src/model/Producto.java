package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Producto {
    private final IntegerProperty referencia = new SimpleIntegerProperty();
    private final StringProperty nombre = new SimpleStringProperty();
    private final DoubleProperty precio = new SimpleDoubleProperty();
    private final IntegerProperty cantidad = new SimpleIntegerProperty();

    public Producto(int referencia, String nombre, double precio, int cantidad) {
        this.referencia.set(referencia);
        this.nombre.set(nombre);
        this.precio.set(precio);
        this.cantidad.set(cantidad);
    }

    // Getters, setters, and property methods
    public int getReferencia() {
        return referencia.get();
    }

    public void setReferencia(int referencia) {
        this.referencia.set(referencia);
    }

    public IntegerProperty referenciaProperty() {
        return referencia;
    }

    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public double getPrecio() {
        return precio.get();
    }

    public void setPrecio(double precio) {
        this.precio.set(precio);
    }

    public DoubleProperty precioProperty() {
        return precio;
    }

    public int getCantidad() {
        return cantidad.get();
    }

    public void setCantidad(int cantidad) {
        this.cantidad.set(cantidad);
    }

    public IntegerProperty cantidadProperty() {
        return cantidad;
    }
}