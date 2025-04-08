import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class VentanaJuego {

   private ArrayList<Dado> dadosLanzados;
   private ArrayList<Dado> dadosSeleccionados;
   private ArrayList<Jugador> jugadores;  // ArrayList para los jugadores
    private Jugador jugadorActual;

    private JPanel panelLanzados;
    private JPanel panelSeleccionados;
    private JFrame frame;
    private Farkle juego;


    public VentanaJuego() {

        frame = new JFrame("Menú Farkle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JPanel panelMenu = new JPanel();
        panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.Y_AXIS));
        panelMenu.setBackground(Color.BLUE);

        ImageIcon icono = new ImageIcon("G:\\4toSemestre\\POO\\Practica-4.1\\imagenes\\farkleLogo2.png");
        //ImageIcon icono = new ImageIcon("C:\\Users\\Usuario\\IdeaProjects\\Practica-4.1\\imagenes\\farkleLogo.png");
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
            Farkle farkle = new Farkle();
            new VentanaJuego(farkle);
        });

        frame.add(panelMenu, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public VentanaJuego(Farkle juego) {
        dadosLanzados = new ArrayList<>();
        dadosSeleccionados = new ArrayList<>();
        this.juego = juego;
        this.jugadores = new ArrayList<>();
        int cantidadJugadores = 2;
        for (int i = 0; i < cantidadJugadores; i++) {
            String nombreJugador = JOptionPane.showInputDialog(frame, "Ingresa el nombre del jugador " + (i + 1) + ":");

            //nombres si no se ingresa nada
            if (nombreJugador == null || nombreJugador.trim().isEmpty()) {
                nombreJugador = "Jugador" + (i + 1);
            }

            jugadores.add(new Jugador(nombreJugador));
        }


         jugadorActual = jugadores.get(0);


        frame = new JFrame("Juego Farkle");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        panelLanzados = new JPanel();
        panelLanzados.setBounds(50, 50, 500, 500);
        panelLanzados.setOpaque(false);
        frame.add(panelLanzados);

        panelSeleccionados = new JPanel();
        panelSeleccionados.setBounds(50, 250, 500, 500);
        panelSeleccionados.setOpaque(false);
        frame.add(panelSeleccionados);

        JButton btnTirar = new JButton("Tirar dados");
        btnTirar.setBounds(230, 170, 120, 30);
        frame.add(btnTirar);

        btnTirar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tirarDados();
            }
        });

        frame.setVisible(true);
    }


    private void tirarDados() {

        dadosLanzados.clear();
        dadosLanzados = juego.lanzarDados();

        panelLanzados.removeAll();


        for (Dado dado : dadosLanzados) {
            dado.getBoton().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ev) {
                    moverDado(dado);
                }
            });
            panelLanzados.add(dado.getBoton());
        }
        panelLanzados.revalidate();
        panelLanzados.repaint();
    }

    private void moverDado(Dado dado) {
        if (dadosLanzados.contains(dado)) {
            panelLanzados.remove(dado.getBoton());
            panelSeleccionados.add(dado.getBoton());

            dadosLanzados.remove(dado);
            dadosSeleccionados.add(dado);
            dado.getBoton().setEnabled(false);
            juego.seleccionarDado(dado);
        } else if (dadosSeleccionados.contains(dado)) {
            panelSeleccionados.remove(dado.getBoton());
            panelLanzados.add(dado.getBoton());

            dadosSeleccionados.remove(dado);
            dadosLanzados.add(dado);
            juego.deseleccionarDado(dado);
        }


        panelLanzados.revalidate();
        panelLanzados.repaint();
        panelSeleccionados.revalidate();
        panelSeleccionados.repaint();
    }

    public static void main(String[] args) {
        new VentanaJuego();  // Iniciar la ventana del juego
    }
}


