package StackHTML;

public class EtiquetaAbierta {

    // Datos de una etiqueta que se abrio y todavia no se ha cerrado, constr guarda
    // el nombre y la posicion donde se abrió la etiq.
    private String nombre;
    private int linea;
    private int columna;

    public EtiquetaAbierta(String nombre, int linea, int columna) {
        this.nombre = nombre;
        this.linea = linea;
        this.columna = columna;
    }

    public String getNombre() {
        return nombre;
    }

    public int getLinea() {
        return linea;
    }

    public int getColumna() {
        return columna;
    }
}
