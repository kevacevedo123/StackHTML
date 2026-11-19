package StackHTML;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class InterfazGrafica extends JFrame {

	private SistemaHTML sistema;

	private JTextField campoNombre;
	private JTextArea areaHTML;
	private JTextArea areaResultados;

	public InterfazGrafica() {
		

		sistema = new SistemaHTML();

		setTitle("StackHTML - Validador de HTML");
		setSize(900, 650);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		// Panel principal
		JPanel principal = new JPanel(new BorderLayout(10, 10));

		// Campo para el nombre
		JPanel panelNombre = new JPanel(new BorderLayout(5, 5));
		panelNombre.add(new JLabel("Nombre del documento:"), BorderLayout.WEST);

		campoNombre = new JTextField();
		panelNombre.add(campoNombre, BorderLayout.CENTER);

		principal.add(panelNombre, BorderLayout.NORTH);

		// Área para escribir HTML
		areaHTML = new JTextArea();
		areaHTML.setTabSize(4);

		JScrollPane scrollHTML = new JScrollPane(areaHTML);
		scrollHTML.setBorder(javax.swing.BorderFactory.createTitledBorder("Editor HTML"));

		// Área para mostrar resultados
		areaResultados = new JTextArea();
		areaResultados.setEditable(false);
		areaResultados.setLineWrap(true);
		areaResultados.setWrapStyleWord(true);

		JScrollPane scrollResultados = new JScrollPane(areaResultados);
		scrollResultados.setBorder(javax.swing.BorderFactory.createTitledBorder("Resultados"));

		JPanel panelCentro = new JPanel(new GridLayout(2, 1, 5, 5));
		panelCentro.add(scrollHTML);
		panelCentro.add(scrollResultados);

		principal.add(panelCentro, BorderLayout.CENTER);

		// Botones
		JPanel panelBotones = new JPanel(new GridBagLayout());

		agregarBoton(panelBotones, "Agregar documento", this::agregarDocumento);
		agregarBoton(panelBotones, "Cargar archivo", this::cargarDocumento);
		agregarBoton(panelBotones, "Actualizar documento", this::actualizarDocumento);
		agregarBoton(panelBotones, "Eliminar documento", this::eliminarDocumento);

		agregarBoton(panelBotones, "Validar documento", this::validarDocumento);
		agregarBoton(panelBotones, "Validar texto", this::validarTexto);
		agregarBoton(panelBotones, "Ver reporte", this::verReporte);
		agregarBoton(panelBotones, "Guardar reporte", this::guardarReporte);

		agregarBoton(panelBotones, "Listar documentos", this::listarDocumentos);
		agregarBoton(panelBotones, "Listar reportes", this::listarReportes);
		agregarBoton(panelBotones, "Estadísticas", this::mostrarEstadisticas);
		agregarBoton(panelBotones, "Limpiar", this::limpiar);
		JButton botonVer = new JButton("Ver documento");
		botonVer.addActionListener(e -> verDocumento());

		GridBagConstraints gbcVer = new GridBagConstraints();

		gbcVer.gridx = 0;
		gbcVer.gridy = panelBotones.getComponentCount() / 4;
		gbcVer.gridwidth = 4;
		gbcVer.fill = GridBagConstraints.BOTH;
		gbcVer.weightx = 1.0;
		gbcVer.weighty = 1.0;
		gbcVer.insets = new Insets(3, 3, 3, 3);

		panelBotones.add(botonVer, gbcVer);

		principal.add(panelBotones, BorderLayout.SOUTH);

		add(principal);
	}

	// Crea un botón y conecta su acción

private void agregarBoton(JPanel panel, String texto, Runnable accion) {

    JButton boton = new JButton(texto);
    boton.addActionListener(e -> accion.run());

    GridBagConstraints gbc = new GridBagConstraints();

    gbc.gridx = panel.getComponentCount() % 4;
    gbc.gridy = panel.getComponentCount() / 4;

    gbc.gridwidth = 1;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;

    gbc.insets = new Insets(3, 3, 3, 3);

    panel.add(boton, gbc);
}


	// Obtiene el nombre escrito por el usuario
	private String obtenerNombre() {

		String nombre = campoNombre.getText();

		if (nombre == null || nombre.trim().isEmpty()) {
			mostrarMensaje("Debes escribir el nombre del documento.");
			return null;
		}

		return nombre;
	}

	// Muestra un mensaje en el área de resultados
	private void mostrarMensaje(String mensaje) {

		areaResultados.setText(mensaje);
	}

	// Limpia los campos de entrada
	private void limpiarCampos() {

		campoNombre.setText("");
		areaHTML.setText("");
	}

	// Agregar documento escrito manualmente
	private void agregarDocumento() {

		String nombre = obtenerNombre();

		if (nombre == null)
			return;

		if (sistema.buscarDocumento(nombre) != null) {
			mostrarMensaje("Ya existe un documento con ese nombre.");
			return;
		}

		String contenido = areaHTML.getText();

		if (sistema.agregarDocumento(nombre, contenido)) {
			mostrarMensaje("Documento agregado correctamente.");
			limpiarCampos();
		} else {
			mostrarMensaje("No se pudo agregar el documento.");
		}
	}

	// Seleccionar y cargar un archivo HTML
	private void cargarDocumento() {

		String nombre = obtenerNombre();

		if (nombre == null)
			return;

		if (sistema.buscarDocumento(nombre) != null) {
			mostrarMensaje("Ya existe un documento con ese nombre.");
			return;
		}

		JFileChooser selector = new JFileChooser();

		selector.setDialogTitle("Seleccionar archivo HTML");

		FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos HTML (*.html, *.htm)", "html", "htm");

		selector.setFileFilter(filtro);
		selector.setAcceptAllFileFilterUsed(false);

		int resultado = selector.showOpenDialog(this);

		if (resultado != JFileChooser.APPROVE_OPTION) {
			mostrarMensaje("Selección cancelada.");
			return;
		}

		File archivo = selector.getSelectedFile();

		if (sistema.cargarDocumento(nombre, archivo.getAbsolutePath())) {

			Documento documento = sistema.buscarDocumento(nombre);
			areaHTML.setText(documento.getContenido());

			mostrarMensaje("Documento cargado correctamente.");
		} else {

			mostrarMensaje("No se pudo cargar el archivo.");
		}
	}

	// Consultar el contenido de un documento registrado
	private void verDocumento() {

		String nombre = obtenerNombre();

		if (nombre == null)
			return;

		Documento documento = sistema.buscarDocumento(nombre);

		if (documento == null) {
			mostrarMensaje("No existe un documento con ese nombre.");
			return;
		}

		areaHTML.setText(documento.getContenido());
		areaHTML.setCaretPosition(0);

		mostrarMensaje("Documento encontrado. Puedes consultar su contenido o modificarlo.");
	}

	// Actualizar un documento existente
	private void actualizarDocumento() {

		String nombre = obtenerNombre();

		if (nombre == null)
			return;

		if (sistema.actualizarDocumento(nombre, areaHTML.getText())) {
			mostrarMensaje("Documento actualizado correctamente.");
			limpiarCampos();
		} else {
			mostrarMensaje("No existe un documento con ese nombre.");
		}
	}

	// Eliminar un documento
	private void eliminarDocumento() {

		String nombre = obtenerNombre();

		if (nombre == null)
			return;

		int confirmacion = JOptionPane.showConfirmDialog(this,
				"¿Seguro que deseas eliminar el documento " + nombre + "?", "Confirmar eliminación",
				JOptionPane.YES_NO_OPTION);

		if (confirmacion != JOptionPane.YES_OPTION)
			return;

		if (sistema.eliminarDocumento(nombre)) {
			areaHTML.setText("");
			mostrarMensaje("Documento eliminado correctamente.");
			limpiarCampos();
		} else {
			mostrarMensaje("No existe un documento con ese nombre.");
		}
	}

	// Validar un documento registrado
	private void validarDocumento() {

		String nombre = obtenerNombre();

		if (nombre == null)
			return;

		Reporte reporte = sistema.validarDocumento(nombre);

		if (reporte == null) {
			mostrarMensaje("No existe un documento con ese nombre.");
		} else {
			mostrarMensaje(reporte.toString());
			limpiarCampos();
		}
	}

	// Validar texto sin registrar un documento
	private void validarTexto() {

		String nombre = campoNombre.getText();

		if (nombre.trim().isEmpty()) {
			nombre = "Validación sin registrar";
		}

		Reporte reporte = sistema.validarContenido(nombre, areaHTML.getText());

		mostrarMensaje(reporte.toString());
		limpiarCampos();
	}

	// Consultar el reporte más reciente
	private void verReporte() {

		String nombre = obtenerNombre();

		if (nombre == null)
			return;

		Reporte reporte = sistema.buscarReporte(nombre);

		if (reporte == null) {
			mostrarMensaje("No hay reportes de ese documento.");
		} else {
			mostrarMensaje(reporte.toString());
			limpiarCampos();
		}
	}

	// Guardar el reporte en un archivo
	private void guardarReporte() {

		String nombre = obtenerNombre();

		if (nombre == null)
			return;

		if (sistema.buscarReporte(nombre) == null) {
			mostrarMensaje("No hay ningún reporte de ese documento.");
			return;
		}

		JFileChooser selector = new JFileChooser();

		selector.setDialogTitle("Guardar reporte");

		FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos de texto (*.txt)", "txt");

		selector.setFileFilter(filtro);
		selector.setSelectedFile(new File(nombre + ".txt"));

		int resultado = selector.showSaveDialog(this);

		if (resultado != JFileChooser.APPROVE_OPTION) {
			mostrarMensaje("Guardado cancelado.");
			return;
		}

		String ruta = selector.getSelectedFile().getAbsolutePath();

		if (sistema.guardarReporte(nombre, ruta)) {
			mostrarMensaje("Reporte guardado correctamente.");
			limpiarCampos();
		} else {
			mostrarMensaje("No se pudo guardar el reporte.");
		}
	}

	// Mostrar documentos registrados
	private void listarDocumentos() {

		mostrarMensaje(sistema.listarDocumentos());
	}

	// Mostrar historial de reportes
	private void listarReportes() {

		mostrarMensaje(sistema.listarReportes());
	}

	// Mostrar estadísticas
	private void mostrarEstadisticas() {

		String resultado = "Documentos registrados: " + sistema.cantidadDocumentos() + "\nReportes en el historial: "
				+ sistema.cantidadReportes() + "\nDocumentos válidos: " + sistema.cantidadDocumentosValidos();

		String nombre = campoNombre.getText();

		if (!nombre.trim().isEmpty()) {

			int errores = sistema.cantidadErrores(nombre);

			if (errores == -1) {
				resultado += "\nNo hay reportes de ese documento.";
			} else {
				resultado += "\nErrores del último reporte: " + errores;
			}
		}

		mostrarMensaje(resultado);
	}

	// Limpiar los campos
	private void limpiar() {

		campoNombre.setText("");
		areaHTML.setText("");
		areaResultados.setText("");
	}
}