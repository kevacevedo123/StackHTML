package StackHTML;

public class Documento {

    // Datos de un documento HTML registrado en el sistema
    private String nombre;
    private String ruta;
    private String contenido;

    public Documento(String nombre, String ruta, String contenido) {
        this.nombre = nombre;
        this.ruta = ruta;
        this.contenido = contenido;
    }

    public String getNombre() {
        return nombre;
    }

    public String getRuta() {
        return ruta;
    }

    // Devuelve el texto HTML del doc
    public String getContenido() {
        return contenido;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    // Cambia el texto HTML
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    // Dos documentos son iguales si tienen el mismo nombre (el nombre es unico en el sistema).
    // Asi ArrayList puede usar contains, indexOf y remove buscando por nombre
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Documento))
            return false;
        Documento otro = (Documento) o;
        return nombre != null && nombre.equals(otro.getNombre());
    }

    // Va junto con equals: documentos iguales dan el mismo codigo
    @Override
    public int hashCode() {
        return (nombre == null) ? 0 : nombre.hashCode();
    }
}