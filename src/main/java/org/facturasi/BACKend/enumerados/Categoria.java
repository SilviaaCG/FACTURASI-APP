package org.facturasi.BACKend.enumerados;

public enum Categoria {
    TARJETA_GRAFICA("Tarjeta Gráfica"),
    PROCESADOR("Procesador"),
    PLACA_BASE("Placa Base"),
    PERIFERICOS("Periféricos"),
    CABLES("Cables"),
    MEMORIA_RAM("Memoria RAM"),
    ALMACENAMIENTO("Almacenamiento"),
    FUENTE_PODER("Fuente de alimentación");

    private String nombre;

    Categoria(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }


}
