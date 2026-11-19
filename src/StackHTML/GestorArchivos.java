package StackHTML;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GestorArchivos {

    public GestorArchivos() {
    }

    // Lee el archivo completo y conserva los saltos de linea del documento.
    // Devuelve null si no se pudo leer.
    public String leerArchivo(String ruta) {
        if (ruta == null || ruta.isEmpty())
            return null;

        StringBuilder contenido = new StringBuilder();
        FileReader fr = null;
        BufferedReader b = null;
        boolean leido = false;

        try {
            fr = new FileReader(ruta);
            b = new BufferedReader(fr);
            String linea;
            while ((linea = b.readLine()) != null)
                contenido.append(linea).append('\n');
            leido = true;
        } catch (IOException e) {
            // no se pudo leer: leido se queda en false
        } finally {
            if (fr != null) {
                try {
                    b.close();
                    fr.close();
                } catch (IOException e) {
                    leido = false;
                }
            }
        }

        if (leido)
            return contenido.toString();
        return null;
    }

    // Guarda el texto del reporte en la ruta indicada. Devuelve true si se guardo bien.
    public boolean guardarReporte(Reporte reporte, String ruta) {
        if (reporte == null || ruta == null || ruta.isEmpty())
            return false;

        FileWriter fw = null;
        BufferedWriter b = null;
        boolean guardado = false;

        try {
            fw = new FileWriter(ruta);
            b = new BufferedWriter(fw);
            b.write(reporte.toString());
            guardado = true;
        } catch (IOException e) {
            // no se pudo escribir: guardado se queda en false
        } finally {
            if (fw != null) {
                try {
                    b.close();
                    fw.close();
                } catch (IOException e) {
                    guardado = false;
                }
            }
        }
        return guardado;
    }

}
