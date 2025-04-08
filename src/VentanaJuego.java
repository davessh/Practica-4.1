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

    private JPanel panelLanzados, panelSeleccionados, panelPuntuaciones, panelBotones;
    private JButton botonTirar;
    private JButton botonAcumular, botonJugar, botonSalir,botonMostrarCombinaciones;
    private JFrame frame;
    private Farkle juego;
    private ArrayList<JLabel> etiquetasPuntuaciones;



    public VentanaJuego(Farkle juego) {
        dadosLanzados = new ArrayList<>();
        dadosSeleccionados = new ArrayList<>();
        this.juego = juego;
        this.jugadores = new ArrayList<>();
        iniciarMenuJuego();
    }


    public void iniciarMenuJuego() {
        frame = new JFrame("Menú Farkle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JPanel panelMenu = new JPanel();
        panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.Y_AXIS));
        panelMenu.setBackground(new Color(255,252,201));

        ImageIcon icono = new ImageIcon("G:\\4toSemestre\\POO\\Practica-4.1\\imagenes\\farkleLogo2.png");
        //ImageIcon icono = new ImageIcon("C:\\Users\\Usuario\\IdeaProjects\\Practica-4.1\\imagenes\\farkleLogo2.png");
        JLabel etiquetaImagen = new JLabel(icono);
        etiquetaImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMenu.add(Box.createVerticalStrut(20));
        panelMenu.add(etiquetaImagen);

        botonJugar = new JButton("Jugar");
        botonJugar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonJugar.setMaximumSize(new Dimension(200, 40));
        panelMenu.add(Box.createVerticalStrut(30));
        panelMenu.add(botonJugar);

        botonSalir = new JButton("Salir");
        botonSalir.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonSalir.setMaximumSize(new Dimension(200, 40));
        panelMenu.add(Box.createVerticalStrut(10));
        panelMenu.add(botonSalir);

        botonSalir.addActionListener(e -> System.exit(0));

        botonJugar.addActionListener(e -> {
            frame.dispose();
            inicializarJugadores();
            iniciarJuego();
        });


        frame.add(panelMenu, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public void inicializarJugadores() {
        int numeroCantidadDeJugadores;
        do {
            String cantidadJugadores = JOptionPane.showInputDialog(frame,
                    "Ingresa la cantidad de jugadores (Mínimo 2, Máximo 10)");
            if (cantidadJugadores == null) {
                System.exit(0);
                frame.dispose();
            }
            try {
                numeroCantidadDeJugadores = Integer.parseInt(cantidadJugadores);
            } catch (NumberFormatException e) {
                numeroCantidadDeJugadores = 0;
                JOptionPane.showMessageDialog(frame, "Debes ingresar un número válido",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } while (numeroCantidadDeJugadores < 2 || numeroCantidadDeJugadores > 10);

        for (int i = 0; i < numeroCantidadDeJugadores; i++) {
            String nombreJugador = JOptionPane.showInputDialog(frame, "Ingresa el nombre del jugador " + (i + 1) + ":");

            //nombres si no se ingresa nada
            if (nombreJugador == null || nombreJugador.trim().isEmpty()) {
                nombreJugador = "Jugador" + (i + 1);
            }
            jugadores.add(new Jugador(nombreJugador));
        }
        juego.setJugadores(jugadores);


        jugadorActual = jugadores.get(0);

    }

    public void iniciarJuego() {
        frame = new JFrame("Juego Farkle");
        frame.setSize(1000, 800);
        frame.setLayout(new BorderLayout());


        etiquetasPuntuaciones = new ArrayList<>();

        panelPuntuaciones = new JPanel();
        panelPuntuaciones.setBounds(800, 50, 150, 300);
        panelPuntuaciones.setLayout(new BoxLayout(panelPuntuaciones, BoxLayout.Y_AXIS));
        panelPuntuaciones.setBackground(new Color(230,241,250));
        frame.add(panelPuntuaciones);

        actualizarPuntuaciones();

        panelLanzados = new JPanel();
        panelLanzados.setBounds(250, 50, 500, 500);
        panelLanzados.setBackground(new Color(255,252,201));
        panelLanzados.setOpaque(false);
        frame.add(panelLanzados);

        panelSeleccionados = new JPanel();
        panelSeleccionados.setBounds(250, 250, 500, 500);
        panelSeleccionados.setBackground(new Color(255,252,201));
        panelSeleccionados.setOpaque(true);
        frame.add(panelSeleccionados);





        botonTirar = new JButton("Tirar dados");
        botonTirar.setBounds(320, 170, 120, 40);
        frame.add(botonTirar);

        botonAcumular = new JButton("Acumular");
        botonAcumular.setBounds(440, 170, 120, 40);
        frame.add(botonAcumular);

        botonMostrarCombinaciones = new JButton("Mostrar tabla");
        botonMostrarCombinaciones.setBounds(560,170,120,40);
        frame.add(botonMostrarCombinaciones);

        panelBotones = new JPanel();
        panelBotones.setBounds(320, 500, 120, 40);
        panelBotones.add(botonTirar);
        panelBotones.add(botonAcumular);
        panelBotones.add(botonMostrarCombinaciones);
        frame.add(panelBotones);
        panelBotones.setBackground(new Color(255,252,201));
        panelBotones.setOpaque(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(208, 240, 192));
        frame.setVisible(true);

        botonTirar.addActionListener(e -> {
            tirarDados();
            puedeAcumular();
        });
        botonAcumular.addActionListener(e -> {
            if(puedeAcumular()){
               boolean tieneCombinacion= juego.hayCombinaciones(dadosLanzados);
               if(tieneCombinacion && juego.esHotDice(dadosLanzados)){
                   int puntuacionOptima = juego.calcularPuntuacionOptima(dadosLanzados);
                   //Agregar algo para que diga que fue hotdice
                   //Se preggunta si quiere tirar otra vez o acumular
                   //if
                   juego.setPuntosTurno(juego.getPuntosTurno()+puntuacionOptima);
                   jugadorActual.sumarPuntos(puntuacionOptima);
                   actualizarPuntuaciones();
                   dadosSeleccionados.clear();
                   dadosLanzados.clear();
                   dadosLanzados = juego.lanzarDados();
               }
//               else if(tieneCombinacion){

               //}
            }

        });

        botonMostrarCombinaciones.addActionListener(e -> {
            mostrarCombinaciones();
        });
    }

    private void actualizarPuntuaciones() {
        panelPuntuaciones.removeAll();         // Borra etiquetas viejas
        etiquetasPuntuaciones.clear();         // Limpia la lista

        for (Jugador jugador : jugadores) {
            JLabel etiqueta = new JLabel(jugador.getNombre() + ": " + jugador.getPuntuacionTotal() + " puntos");
            etiqueta.setFont(new Font("Arial", Font.BOLD, 14));
            etiqueta.setForeground(Color.BLACK);
            etiquetasPuntuaciones.add(etiqueta);
            panelPuntuaciones.add(etiqueta);
        }

        panelPuntuaciones.revalidate();
        panelPuntuaciones.repaint();
    }

    private boolean puedeAcumular(){
        if(dadosSeleccionados.isEmpty()) {
            botonAcumular.setEnabled(false);
            return false;
        } else if(juego.hayCombinaciones(dadosLanzados)) {
            botonAcumular.setEnabled(true);
            return true;
        }
        botonAcumular.setEnabled(false);
        return false;
    }
    private void tirarDados() {
        dadosLanzados.clear();
        dadosLanzados = juego.lanzarDados();
        juego.setDadosEnJuego(dadosLanzados);
        panelLanzados.removeAll();


        for (Dado dado : dadosLanzados) {
            dado.getBoton().addActionListener(ev -> {
                moverDado(dado);
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
//            dado.getBoton().setEnabled(false);
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

    public void mostrarCombinaciones() {
        JPanel panelDeCombinaciones = new JPanel();
        //JLabel labelDeCombinaciones = new JLabel(new ImageIcon("C:\\Users\\PC OSTRICH\\Pr-ctica-4\\Combinaciones.png"));
        JLabel labelDeCombinaciones = new JLabel(new ImageIcon("C:\\Users\\Usuario\\Desktop\\Pr-ctica-4-main[1]\\Combinaciones.png"));
        panelDeCombinaciones.add(labelDeCombinaciones);
        JOptionPane optionPane = new JOptionPane(panelDeCombinaciones, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION);
        JDialog ventanaC = optionPane.createDialog("Combinaciones");
        ventanaC.setLocation(1145, 70);
        ventanaC.setVisible(true);
    }
}


//Cantidad de jugadores
//Nombres de jugadores
//cantidad de puntos limite


