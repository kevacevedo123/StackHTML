
package StackHTML;

import java.util.Stack;

public class PilaEtiquetas extends Stack<EtiquetaAbierta> {

    // La clase no tiene atributos propios: las etiquetas abiertas viven
    // unicamente en la pila que se hereda de Stack
    public PilaEtiquetas() {
        super();
    }

    // Busca la apertura mas reciente con ese nombre usando solo operaciones de
    // pila (isEmpty, peek, pop, push) y una pila auxiliar.
    // Devuelve su posicion contada desde el fondo (0 es el fondo), o -1 si no hay
    // ninguna etiqueta abierta con ese nombre. La pila queda igual que antes
    public int buscar(String nombre) {
        PilaEtiquetas auxiliar = new PilaEtiquetas();
        int posicion = -1;
        boolean encontrada = false;

        // Paso 1: baja desde la cima. Lo que no coincide se pasa a la auxiliar
        while (!isEmpty() && !encontrada) {
            if (peek().getNombre().equals(nombre)) {
                encontrada = true;
                posicion = size() - 1; // la etiqueta buscada quedo en la cima
            } else {
                auxiliar.push(pop());
            }
        }

        // Paso 2: devuelve las etiquetas a la pila. Como la auxiliar tambien es
        // LIFO, regresan en el orden original
        while (!auxiliar.isEmpty()) {
            push(auxiliar.pop());
        }

        return posicion;
    }
}
