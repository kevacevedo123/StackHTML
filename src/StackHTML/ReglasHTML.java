package StackHTML;

public class ReglasHTML {

    // Etiquetas que no llevan cierre
    private String[] etiquetasVacias;

    // Arreglo con las etiquetas vacias que soporta
    public ReglasHTML() {
    	etiquetasVacias = new String[] {
    		    "br", "img", "hr", "input", "meta", "link",
    		    "area", "base", "col", "embed", "source",
    		    "track", "wbr", "param"
    		};
    }

    // Quita los espacios de los extremos y pasa a minusculas
    public String normalizar(String nombre) {
        if (nombre == null)
            return "";
        return nombre.trim().toLowerCase();
    }

    // Devuelve true si la etiqueta esta en la lista de etiquetas vacias
    public boolean esEtiquetaVacia(String nombre) {
        String n = normalizar(nombre);
        boolean encontrada = false;
        int i = 0;
        while (i < etiquetasVacias.length && !encontrada) {
            if (etiquetasVacias[i].equals(n))
                encontrada = true;
            i++;
        }
        return encontrada;
    }
}
