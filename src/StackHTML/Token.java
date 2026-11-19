package StackHTML;


public class Token {

    private TipoToken tipo;
    private String nombre;
    private int linea;
    private int columna;

    public Token(TipoToken tipo, String nombre, int linea, int columna) {
        this.tipo = tipo;
        this.nombre = nombre;
        this.linea = linea;
        this.columna = columna;
    }

    public TipoToken getTipo() {
        return tipo;
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
