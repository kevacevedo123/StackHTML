# StackHTML

Validador de etiquetas HTML hecho en Java usando **pilas**. Revisa que las etiquetas de un documento se cierren en el orden correcto y genera un reporte con los errores, indicando la línea y la columna de cada uno.

Proyecto de la materia **Estructuras de Datos**.

## Por qué una pila

Las etiquetas anidadas se cierran en el orden contrario al que se abrieron: la última que se abre es la primera que se debe cerrar (LIFO).

```html
<div><section><p>Hola</p></section></div>
```

Entran `div`, `section` y `p`, y tienen que salir `p`, `section` y `div`. Por eso cada apertura hace `push` y cada cierre se compara con la cima de la pila.

## Errores que detecta

| Error | Cuándo pasa | Ejemplo |
|---|---|---|
| `CIERRE_SIN_APERTURA` | Se cierra una etiqueta y la pila está vacía | `<p>Hola</p></div>` |
| `ANIDAMIENTO_INCORRECTO` | Se cierra una etiqueta que no está abierta | `<p>Hola mundo</div>` |
| `ETIQUETA_SIN_CERRAR` | Una etiqueta se abrió y nunca se cerró | `<div><p>Hola</div>` |

Las etiquetas vacías (`br`, `img`, `hr`, `input`, etc.) no entran a la pila porque no llevan cierre. Los comentarios `<!-- -->` se ignoran.

## Funcionalidades

- Escribir HTML directamente en la interfaz o cargar un archivo `.html` / `.htm`.
- Validar el documento y ver el reporte con los errores.
- Guardar varios documentos y consultar el historial de reportes.
- Exportar un reporte a un archivo `.txt`.

## Estructura

```
src/StackHTML/
├── Main.java              Punto de entrada, abre la interfaz
├── InterfazGrafica.java   Ventana en Swing
├── SistemaHTML.java       Gestiona documentos y hace la validación
├── PilaEtiquetas.java     Pila de etiquetas abiertas (extiende Stack)
├── EtiquetaAbierta.java   Etiqueta guardada en la pila (nombre, línea, columna)
├── Tokenizador.java       Convierte el texto en tokens
├── Token.java
├── TipoToken.java         APERTURA, CIERRE, VACIA
├── ReglasHTML.java        Define qué etiquetas son vacías
├── Reporte.java           Resultado de una validación
├── ErrorHTML.java
├── TipoError.java
├── Documento.java
└── GestorArchivos.java    Lectura de archivos y guardado de reportes
ejemplos/                  Archivos HTML para probar
```

## Cómo ejecutarlo

Se necesita Java 8 o superior.

```bash
javac -encoding UTF-8 -d out src/StackHTML/*.java
java -cp out StackHTML.Main
```

También se puede abrir la carpeta en NetBeans, Eclipse o IntelliJ y ejecutar `Main.java`.

## Ejemplo

Para el documento:

```html
<div>
  <section>
    <p>Hola
  </section>
</div>
```

El reporte es:

```
Reporte de: etiqueta_sin_cerrar.html
Resultado: CON ERRORES
Etiquetas analizadas: 5
Profundidad máxima de la pila: 3
Errores encontrados: 1
    1. [ETIQUETA_SIN_CERRAR] Línea 3, columna 5: la etiqueta <p> nunca se cerro
```

Cuando llega `</section>`, la pila tiene `div`, `section` y `p`. El método `buscar("section")` de `PilaEtiquetas` encuentra `section` usando una pila auxiliar (sin acceder por índice), y como `p` quedó encima sin cerrarse, se registra el error.
