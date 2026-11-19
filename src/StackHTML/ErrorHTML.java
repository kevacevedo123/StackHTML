package StackHTML;

public class ErrorHTML {

    // Informacion necesaria para describir un error del documento.
    private TipoError tipo;
    private String mensaje;
    private int linea;
    private int columna;
    private String etiquetaEsperada;

    // Guarda los datos del error encontrado durante la validacion.
    public ErrorHTML(TipoError tipo, String mensaje, int linea, int columna, String etiquetaEsperada) {
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.linea = linea;
        this.columna = columna;
        this.etiquetaEsperada = etiquetaEsperada;
    }

    public TipoError getTipo() {
        return tipo;
    }
    public String getMensaje() {
        return mensaje;
    }
    public int getLinea() {
        return linea;
    }
    public int getColumna() {
        return columna;
    }
    public String getEtiquetaEsperada() {
        return etiquetaEsperada;
    }

    // Presenta el error con su tipo y ubicacion en el documento.
    @Override
    public String toString() {
        return "[" + tipo + "] Línea " + linea + ", columna " + columna + ": " + mensaje;
    }

}
