package StackHTML;

import java.util.ArrayList;

public class Tokenizador {

    private ReglasHTML reglas;

    public Tokenizador() {
        reglas = new ReglasHTML();
    }

    //Metodos auxiliares---------------------------------------------------

    // Devuelve true si en la posicion i empieza una etiqueta: un '<' seguido de '/' o de una letra
    private boolean esInicioEtiqueta(String contenido, int i) {
        boolean resultado = false;
        if (contenido.charAt(i) == '<' && i + 1 < contenido.length())
            resultado = contenido.charAt(i + 1) == '/' || Character.isLetter(contenido.charAt(i + 1));
        return resultado;
    }

    // Busca el '>' que cierra la etiqueta, ignorando los que esten entre comillas.
    // Devuelve su posicion, o -1 si la etiqueta nunca se cierra
    private int buscarFinEtiqueta(String contenido, int inicio) {

        boolean dentroComillas = false;
        boolean encontrado = false;
        int i = inicio;

        while (i < contenido.length() && !encontrado) {

            if (contenido.charAt(i) == '"')
                dentroComillas = !dentroComillas;

            if (contenido.charAt(i) == '>' && !dentroComillas)
                encontrado = true;
            else
                i++;
        }

        return (encontrado) ? i : -1;
    }

    // Devuelve el nombre de la etiqueta, sin la barra inicial ni los atributos.
    // El nombre termina en el primer espacio en blanco (espacio, tab o salto de linea) o en '/'
    private String extraerNombre(String contenidoEtiqueta) {

        String contenido = contenidoEtiqueta.trim();

        if (contenido.startsWith("/"))
            contenido = contenido.substring(1); // quita la barra inicial

        int fin = 0;

        while (fin < contenido.length() && !Character.isWhitespace(contenido.charAt(fin)) && contenido.charAt(fin) != '/')
            fin++;

        return contenido.substring(0, fin);
    }

    // Decide si la etiqueta es de cierre, vacia o de apertura
    private TipoToken identificarTipo(String contenidoEtiqueta, String nombre) {

        String contenido = contenidoEtiqueta.trim();

        if (contenido.startsWith("/"))
            return TipoToken.CIERRE;

        if (contenido.endsWith("/") || reglas.esEtiquetaVacia(nombre))
            return TipoToken.VACIA;

        return TipoToken.APERTURA;
    }

    //---------------------------------------------------------------------

    public ArrayList<Token> tokenizar(String contenido) {

        ArrayList<Token> tokens = new ArrayList<Token>();

        int i = 0;
        int linea = 1;
        int columna = 1; // columna del caracter que esta en la posicion i

        while (i < contenido.length()) {

            int fin = i + 1; // por defecto se avanza un solo caracter

            if (contenido.startsWith("<!--", i)) {

                // Comentario: se salta completo sin crear tokens
                int finComentario = contenido.indexOf("-->", i + 4);

                if (finComentario == -1)
                    fin = contenido.length(); // sin cerrar: se ignora el resto
                else
                    fin = finComentario + 3;

            } else if (esInicioEtiqueta(contenido, i)) {

                int finEtiqueta = buscarFinEtiqueta(contenido, i);

                if (finEtiqueta != -1) {

                    String contenidoEtiqueta = contenido.substring(i + 1, finEtiqueta);
                    String nombre = reglas.normalizar(extraerNombre(contenidoEtiqueta));
                    TipoToken tipo = identificarTipo(contenidoEtiqueta, nombre);

                    tokens.add(new Token(tipo, nombre, linea, columna));

                    fin = finEtiqueta + 1; // se salta toda la etiqueta, incluido el '>'
                }
            }

            // Actualiza linea y columna con todo lo que se salto, de i hasta fin
            for (int j = i; j < fin; j++) {
                if (contenido.charAt(j) == '\n') {
                    linea++;
                    columna = 1;
                } else {
                    columna++;
                }
            }

            i = fin;
        }

        return tokens;
    }

}
