import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Farkle {
    private JFrame frame;

    public Farkle() {
        mostrarMenu();
    }

    public void comenzarJuego() {

    }
    private void mostrarMenu() {
        frame = new JFrame("Menú Farkle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JPanel panelMenu = new JPanel();
        panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.Y_AXIS));
        panelMenu.setBackground(Color.GREEN);

        ImageIcon icono = new ImageIcon("C:\\Users\\Usuario\\IdeaProjects\\Practica-4.1\\imagenes\\farkleLogo.png");
        JLabel etiquetaImagen = new JLabel(icono);
        etiquetaImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMenu.add(Box.createVerticalStrut(20));
        panelMenu.add(etiquetaImagen);

        JButton btnJugar = new JButton("Jugar");
        btnJugar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnJugar.setMaximumSize(new Dimension(200, 40));
        panelMenu.add(Box.createVerticalStrut(30));
        panelMenu.add(btnJugar);

        JButton btnSalir = new JButton("Salir");
        btnSalir.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSalir.setMaximumSize(new Dimension(200, 40));
        panelMenu.add(Box.createVerticalStrut(10));
        panelMenu.add(btnSalir);

        btnSalir.addActionListener(e -> System.exit(0));

        btnJugar.addActionListener(e -> {
            frame.dispose();
            new VentanaJuego();
        });

        frame.add(panelMenu, BorderLayout.CENTER);
        frame.setVisible(true);
    }


    public static void main(String[] args) {
        new Farkle();
    }
}
