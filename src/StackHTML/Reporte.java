package StackHTML;


import java.util.ArrayList;

public class Reporte {

    // Datos generales y resultados de una validacion.
    private String nombreDocumento;
    private ArrayList<ErrorHTML> errores;
    private int totalEtiquetas;
    private int profundidadMax;

    // Inicializa un reporte vacío para el documento indicado.
    public Reporte(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
        this.errores = new ArrayList<ErrorHTML>();
        this.totalEtiquetas = 0;
        this.profundidadMax = 0;
    }

    public void agregarError(ErrorHTML e) {
        errores.add(e);
    }
    public boolean esValido() {
        return errores.isEmpty();
    }
    public int cantidadErrores() {
        return errores.size();
    }
    public String getNombreDocumento() {
        return nombreDocumento;
    }
    public ArrayList<ErrorHTML> getErrores() {
        return errores;
    }
    public int getTotalEtiquetas() {
        return totalEtiquetas;
    }
    // Devuelve la profundidad máxima alcanzada por la pila.
    public int getProfundidadMax() {
        return profundidadMax;
    }
    public void setTotalEtiquetas(int totalEtiquetas) {
        this.totalEtiquetas = totalEtiquetas;
    }
    // Guarda la profundidad máxima informada al terminar la validación.
    public void setProfundidadMax(int profundidadMax) {
        this.profundidadMax = profundidadMax;
    }

    // Construye el reporte completo para mostrarlo o guardarlo en un archivo.
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("Reporte de: ").append(nombreDocumento).append("\n");

        if (esValido()) {
            sb.append("Resultado: VALIDO\n");
        } else {
            sb.append("Resultado: CON ERRORES\n");
        }

        sb.append("Etiquetas analizadas: ").append(totalEtiquetas).append("\n");
        sb.append("Profundidad máxima de la pila: ").append(profundidadMax).append("\n");
        sb.append("Errores encontrados: ").append(cantidadErrores());

        // Agrega los errores en el mismo orden en que fueron registrados.
        for (int i = 0; i < errores.size(); i++) {
            sb.append("\n    ").append(i + 1).append(". ");
            sb.append(errores.get(i).toString());
        }

        return sb.toString();
    }

}
