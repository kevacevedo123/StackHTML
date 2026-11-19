package StackHTML;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            InterfazGrafica ventana = new InterfazGrafica();
            ventana.setVisible(true);

        });
    }
}