
package StackHTML;

import java.util.ArrayList;

public class SistemaHTML {

	// Documentos registrados, historial de reportes y las clases que usa para
	// trabajar
	private ArrayList<Documento> documentos;
	private ArrayList<Reporte> reportes;
	private Tokenizador tokenizador;
	private GestorArchivos gestorArchivos;

	public SistemaHTML() {
		this.documentos = new ArrayList<Documento>();
		this.reportes = new ArrayList<Reporte>();
		this.tokenizador = new Tokenizador();
		this.gestorArchivos = new GestorArchivos();
	}

	// Devuelve el documento con ese nombre, o null si no existe.
	// indexOf usa el equals de Documento, que compara por nombre
	public Documento buscarDocumento(String nombre) {
		int pos = documentos.indexOf(new Documento(nombre, "", ""));
		return (pos == -1) ? null : documentos.get(pos);
	}

	// Agrega un documento escrito a mano (ruta vacia). Rechaza nombres vacios o
	// repetidos
	public boolean agregarDocumento(String nombre, String contenido) {
		if (nombre == null || nombre.length() == 0 || contenido == null)
			return false;

		Documento d = new Documento(nombre, "", contenido);

		if (documentos.contains(d))
			return false;

		documentos.add(d);
		return true;
	}

	// Lee el archivo y registra el documento con su ruta. Devuelve false si no se
	// pudo leer
	public boolean cargarDocumento(String nombre, String ruta) {
		if (nombre == null || nombre.length() == 0 || buscarDocumento(nombre) != null)
			return false;

		String contenido = gestorArchivos.leerArchivo(ruta);

		if (contenido == null)
			return false;

		documentos.add(new Documento(nombre, ruta, contenido));
		return true;
	}

	// Cambia el contenido de un documento que ya existe
	public boolean actualizarDocumento(String nombre, String nuevoContenido) {
		Documento d = buscarDocumento(nombre);

		if (d == null || nuevoContenido == null)
			return false;

		d.setContenido(nuevoContenido);
		return true;
	}

	// Quita un documento de la lista (remove devuelve true si lo encontro).
	// Sus reportes anteriores se conservan en el historial
	public boolean eliminarDocumento(String nombre) {
		return documentos.remove(new Documento(nombre, "", ""));
	}

	// Valida un documento registrado. Devuelve null si el documento no existe
	public Reporte validarDocumento(String nombre) {
		Documento d = buscarDocumento(nombre);

		if (d == null)
			return null;

		return validarContenido(d.getNombre(), d.getContenido());
	}

	// Recorre los tokens una sola vez y usa la pila para revisar el orden de
	// cierre.
	// Guarda el reporte en el historial y lo devuelve
	public Reporte validarContenido(String nombre, String contenido) {
		Reporte reporte = new Reporte(nombre);

		// Si no hay contenido, guarda y devuelve un reporte vacío
		if (contenido == null || contenido.isEmpty()) {
			reportes.add(reporte);
			return reporte;
		}

		ArrayList<Token> tokens = tokenizador.tokenizar(contenido);
		PilaEtiquetas pila = new PilaEtiquetas(); // pila nueva en cada validacion
		int total = 0;
		int profundidadMax = 0;

		for (Token t : tokens) {
			total++;

			if (t.getTipo() == TipoToken.APERTURA) {
				pila.push(new EtiquetaAbierta(t.getNombre(), t.getLinea(), t.getColumna()));

				if (pila.size() > profundidadMax)
					profundidadMax = pila.size();
			}

			// Las etiquetas VACIA no entran a la pila porque no necesitan cierre
			else if (t.getTipo() == TipoToken.CIERRE) {

				// Verifica si la pila está vacía para confirmar que no hay ninguna etiqueta de
				// apertura
				if (pila.isEmpty()) {
					// Registra un error cuando se encuentra un cierre sin ninguna apertura
					reporte.agregarError(new ErrorHTML(TipoError.CIERRE_SIN_APERTURA,
							"se encontro </" + t.getNombre() + "> pero no hay ninguna etiqueta abierta", t.getLinea(),
							t.getColumna(), ""));
				}
				// Si la pila no está vacía, busca si la etiqueta que queremos cerrar está entre
				// las abiertas
				else {
					// Busca la posición de la etiqueta dentro de la pila; devuelve -1 si no la
					// encuentra
					int posicion = pila.buscar(t.getNombre());

					// Si no encuentra la etiqueta, significa que no existe una apertura
					// correspondiente
					if (posicion == -1) {
						// Obtiene la etiqueta de la cima para indicar cuál se esperaba cerrar
						EtiquetaAbierta cima = pila.peek();
						// Registra un error de anidamiento incorrecto
						reporte.agregarError(new ErrorHTML(TipoError.ANIDAMIENTO_INCORRECTO,
								"se esperaba </" + cima.getNombre() + "> pero se encontro </" + t.getNombre() + ">",
								t.getLinea(), t.getColumna(), cima.getNombre()));
					}

					// Si encuentra la etiqueta, comprueba si hay otras etiquetas por encima que
					// deban retirarse
					else {
						// Mientras haya etiquetas por encima de la que queremos cerrar
						while (pila.size() - 1 > posicion) {
							// Retira las etiquetas que quedaron sin cerrar en el orden correcto
							EtiquetaAbierta abierta = pila.pop();

							// Registra un error por cada etiqueta que tuvo que retirar
							reporte.agregarError(new ErrorHTML(TipoError.ETIQUETA_SIN_CERRAR,
									"la etiqueta <" + abierta.getNombre() + "> nunca se cerro", abierta.getLinea(),
									abierta.getColumna(), abierta.getNombre()));
						}

						// Retira la etiqueta que sí encontró su cierre
						pila.pop();
					}
				}
			}
		}

		// Lo que quedo en la pila nunca se cerro
		while (!pila.isEmpty()) {
			EtiquetaAbierta abierta = pila.pop();

			reporte.agregarError(new ErrorHTML(TipoError.ETIQUETA_SIN_CERRAR,
					"la etiqueta <" + abierta.getNombre() + "> nunca se cerro", abierta.getLinea(),
					abierta.getColumna(), abierta.getNombre()));
		}

		reporte.setTotalEtiquetas(total);
		reporte.setProfundidadMax(profundidadMax);
		reportes.add(reporte);

		return reporte;
	}

	// Devuelve el reporte mas reciente de ese documento (se recorre desde el
	// final), o null
	public Reporte buscarReporte(String nombre) {
		int i = reportes.size() - 1;

		while (i >= 0 && !reportes.get(i).getNombreDocumento().equals(nombre))
			i--;

		return (i == -1) ? null : reportes.get(i);
	}

	// Guarda en un archivo el reporte mas reciente del documento
	public boolean guardarReporte(String nombre, String ruta) {
		Reporte r = buscarReporte(nombre);

		if (r == null)
			return false;

		return gestorArchivos.guardarReporte(r, ruta);
	}

	public int cantidadDocumentos() {
		return documentos.size();
	}

	public int cantidadReportes() {
		return reportes.size();
	}

	// Cantidad de errores del reporte mas reciente. Devuelve -1 si no hay reporte
	public int cantidadErrores(String nombre) {
		Reporte r = buscarReporte(nombre);
		return (r == null) ? -1 : r.cantidadErrores();
	}

	// Cuenta los documentos cuyo ultimo reporte es valido
	public int cantidadDocumentosValidos() {
		int contador = 0;

		for (Documento d : documentos) {
			Reporte r = buscarReporte(d.getNombre());

			if (r != null && r.esValido())
				contador++;
		}

		return contador;
	}

	// Arma el listado de documentos con StringBuilder para no crear un String nuevo
	// en cada vuelta
	public String listarDocumentos() {
		if (documentos.isEmpty())
			return "No hay documentos registrados";

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < documentos.size(); i++) {
			Documento d = documentos.get(i);
			String origen = (d.getRuta().length() == 0) ? "escrito a mano" : d.getRuta();

			if (i > 0)
				sb.append("\n");

			sb.append(i + 1).append(". ").append(d.getNombre()).append(" (").append(origen).append(")");
		}

		return sb.toString();
	}

	// Arma el historial de reportes, del mas antiguo al mas reciente
	public String listarReportes() {
		if (reportes.isEmpty())
			return "No hay reportes en el historial";

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < reportes.size(); i++) {
			Reporte r = reportes.get(i);
			String resultado = (r.esValido()) ? "VALIDO" : "CON ERRORES";

			if (i > 0)
				sb.append("\n");

			sb.append(i + 1).append(". ").append(r.getNombreDocumento()).append(" - ").append(resultado);
			sb.append(" - errores: ").append(r.cantidadErrores());
			sb.append(" - etiquetas: ").append(r.getTotalEtiquetas());
		}

		return sb.toString();
	}
}
