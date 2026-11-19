# StackHTML

[English](#english) | [Español](#español)

---

## English

HTML tag validator written in Java using **stacks**. It checks that the tags in a document are closed in the correct order and generates a report with the errors found, showing the line and column of each one.

Project for the **Data Structures** course.

### Why a stack

Nested tags are closed in the reverse order in which they were opened: the last tag opened is the first one that must be closed (LIFO).

```html
<div><section><p>Hello</p></section></div>
```

`div`, `section` and `p` go in, and they must come out as `p`, `section` and `div`. That is why every opening tag does a `push` and every closing tag is compared with the top of the stack.

### Errors detected

| Error | When it happens | Example |
|---|---|---|
| `CIERRE_SIN_APERTURA` | A tag is closed while the stack is empty | `<p>Hello</p></div>` |
| `ANIDAMIENTO_INCORRECTO` | A tag that is not open is closed | `<p>Hello world</div>` |
| `ETIQUETA_SIN_CERRAR` | A tag was opened and never closed | `<div><p>Hello</div>` |

Void tags (`br`, `img`, `hr`, `input`, etc.) are not pushed onto the stack because they have no closing tag. Comments `<!-- -->` are ignored.

### Features

- Write HTML directly in the interface or load an `.html` / `.htm` file.
- Validate the document and see the report with its errors.
- Keep several documents and check the report history.
- Export a report to a `.txt` file.

### Structure

```
src/StackHTML/
├── Main.java              Entry point, opens the interface
├── InterfazGrafica.java   Swing window
├── SistemaHTML.java       Manages documents and runs the validation
├── PilaEtiquetas.java     Stack of open tags (extends Stack)
├── EtiquetaAbierta.java   Tag stored in the stack (name, line, column)
├── Tokenizador.java       Turns the text into tokens
├── Token.java
├── TipoToken.java         APERTURA, CIERRE, VACIA
├── ReglasHTML.java        Defines which tags are void
├── Reporte.java           Result of a validation
├── ErrorHTML.java
├── TipoError.java
├── Documento.java
└── GestorArchivos.java    Reads files and saves reports
ejemplos/                  HTML files for testing
```

### How to run it

Requires Java 8 or later.

```bash
javac -encoding UTF-8 -d out src/StackHTML/*.java
java -cp out StackHTML.Main
```

You can also open the folder in NetBeans, Eclipse or IntelliJ and run `Main.java`.

### Example

For the document:

```html
<div>
  <section>
    <p>Hola
  </section>
</div>
```

The report is:

```
Reporte de: etiqueta_sin_cerrar.html
Resultado: CON ERRORES
Etiquetas analizadas: 5
Profundidad máxima de la pila: 3
Errores encontrados: 1
    1. [ETIQUETA_SIN_CERRAR] Línea 3, columna 5: la etiqueta <p> nunca se cerro
```

When `</section>` arrives, the stack holds `div`, `section` and `p`. The `buscar("section")` method in `PilaEtiquetas` finds `section` using an auxiliary stack (without index access), and since `p` was left on top without being closed, the error is recorded.

> The program's interface and reports are in Spanish.

---

## Español

Validador de etiquetas HTML hecho en Java usando **pilas**. Revisa que las etiquetas de un documento se cierren en el orden correcto y genera un reporte con los errores, indicando la línea y la columna de cada uno.

Proyecto de la materia **Estructuras de Datos**.

### Por qué una pila

Las etiquetas anidadas se cierran en el orden contrario al que se abrieron: la última que se abre es la primera que se debe cerrar (LIFO).

```html
<div><section><p>Hola</p></section></div>
```

Entran `div`, `section` y `p`, y tienen que salir `p`, `section` y `div`. Por eso cada apertura hace `push` y cada cierre se compara con la cima de la pila.

### Errores que detecta

| Error | Cuándo pasa | Ejemplo |
|---|---|---|
| `CIERRE_SIN_APERTURA` | Se cierra una etiqueta y la pila está vacía | `<p>Hola</p></div>` |
| `ANIDAMIENTO_INCORRECTO` | Se cierra una etiqueta que no está abierta | `<p>Hola mundo</div>` |
| `ETIQUETA_SIN_CERRAR` | Una etiqueta se abrió y nunca se cerró | `<div><p>Hola</div>` |

Las etiquetas vacías (`br`, `img`, `hr`, `input`, etc.) no entran a la pila porque no llevan cierre. Los comentarios `<!-- -->` se ignoran.

### Funcionalidades

- Escribir HTML directamente en la interfaz o cargar un archivo `.html` / `.htm`.
- Validar el documento y ver el reporte con los errores.
- Guardar varios documentos y consultar el historial de reportes.
- Exportar un reporte a un archivo `.txt`.

### Estructura

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

### Cómo ejecutarlo

Se necesita Java 8 o superior.

```bash
javac -encoding UTF-8 -d out src/StackHTML/*.java
java -cp out StackHTML.Main
```

También se puede abrir la carpeta en NetBeans, Eclipse o IntelliJ y ejecutar `Main.java`.

### Ejemplo

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
