import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class VentanaJuego {

    private ArrayList<Dado> dadosLanzados;
    private ArrayList<Dado> dadosSeleccionados;
    private ArrayList<Dado> dadosBloqueados;      // Dados ya bloqueados de tiradas anteriores
    private ArrayList<Jugador> jugadores;  // ArrayList para los jugadores
    private Jugador jugadorActual;
    private int puntosTiradaActual;

    private JPanel panelLanzados, panelSeleccionados, panelPuntuaciones, panelBloqueados, panelBotones;
    private JButton botonTirar;
    private JButton botonAcumular, botonJugar, botonSalir,botonMostrarCombinaciones;
    private JFrame frame;
    private Farkle juego;
    private ArrayList<JLabel> etiquetasPuntuaciones;



    public VentanaJuego(Farkle juego) {
        dadosLanzados = new ArrayList<>();
        dadosSeleccionados = new ArrayList<>();
        dadosBloqueados = new ArrayList<>();
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
        //ImageIcon icono = new ImageIcon("C:\\Users\\GF76\\IdeaProjects\\Practica-4.2\\imagenes\\farkleLogo2.png");
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
        frame.setLayout(null); // Usar layout nulo para posicionamiento absoluto

        etiquetasPuntuaciones = new ArrayList<>();

        // Panel de puntuaciones (derecha)
        panelPuntuaciones = new JPanel();
        panelPuntuaciones.setBounds(800, 50, 150, 200);
        panelPuntuaciones.setLayout(new BoxLayout(panelPuntuaciones, BoxLayout.Y_AXIS));
        panelPuntuaciones.setBackground(new Color(230,241,250));
        frame.add(panelPuntuaciones);

        actualizarPuntuaciones();

        // Panel de dados lanzados (centro)
        panelLanzados = new JPanel();
        panelLanzados.setBounds(250, 50, 500, 200); // Reducir altura para dejar espacio a los botones
        panelLanzados.setBackground(new Color(255,252,201));
        frame.add(panelLanzados);

        // Panel de dados seleccionados (debajo de los lanzados)
        panelSeleccionados = new JPanel();
        panelSeleccionados.setBounds(250, 270, 500, 100); // Ajustar posición y tamaño
        panelSeleccionados.setBackground(new Color(255,252,201));
        frame.add(panelSeleccionados);

        panelBloqueados = new JPanel();
        panelBloqueados.setBounds(250, 390, 500, 100);
        panelBloqueados.setBackground(new Color(215,255,215));
        frame.add(panelBloqueados);

        // Panel de botones (debajo de los seleccionados)
        panelBotones = new JPanel();
        panelBotones.setBounds(250, 560, 500, 80); // Ajustar posición y tamaño
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // Usar FlowLayout para espaciado uniforme

        botonTirar = new JButton("Tirar dados");
        botonAcumular = new JButton("Acumular");
        botonMostrarCombinaciones = new JButton("Mostrar tabla");

        // Ajustar tamaño preferido de los botones si es necesario
        botonTirar.setPreferredSize(new Dimension(120, 40));
        botonAcumular.setPreferredSize(new Dimension(120, 40));
        botonMostrarCombinaciones.setPreferredSize(new Dimension(150, 40));

        panelBotones.add(botonTirar);
        panelBotones.add(botonAcumular);
        panelBotones.add(botonMostrarCombinaciones);
        panelBotones.setBackground(new Color(255,252,201));
        frame.add(panelBotones);

        // Añadir etiqueta informativa para turno actual
        JLabel etiquetaTurnoActual = new JLabel("Turno de: " + jugadorActual.getNombre());
        etiquetaTurnoActual.setBounds(250, 500, 500, 30);
        etiquetaTurnoActual.setFont(new Font("Arial", Font.BOLD, 16));
        etiquetaTurnoActual.setHorizontalAlignment(JLabel.CENTER);
        frame.add(etiquetaTurnoActual);

        // Añadir etiqueta para puntos de la ronda
        JLabel etiquetaPuntosRonda = new JLabel("Puntos ronda: 0");
        etiquetaPuntosRonda.setBounds(250, 530, 500, 30);
        etiquetaPuntosRonda.setFont(new Font("Arial", Font.PLAIN, 14));
        etiquetaPuntosRonda.setHorizontalAlignment(JLabel.CENTER);
        frame.add(etiquetaPuntosRonda);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(208, 240, 192));
        frame.setVisible(true);

        botonTirar.addActionListener(e -> {
            tirarDados();
            actualizarBotonAcumular();
            etiquetaPuntosRonda.setText("Puntos ronda: " + juego.getPuntosTurno());
        });
        botonAcumular.addActionListener(e -> {
            if (dadosSeleccionados.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Debes seleccionar al menos un dado",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (juego.esSeleccionValida(dadosSeleccionados)) {
                puntosTiradaActual = juego.calcularPuntos(dadosSeleccionados);
                juego.sumarPuntosParciales(puntosTiradaActual);

                // Mostrar puntos acumulados
                JOptionPane.showMessageDialog(frame,
                        "Has seleccionado una combinación válida.\n" +
                                "Puntos ganados: " + puntosTiradaActual + "\n" +
                                "Puntos acumulados en este turno: " + juego.getPuntosTurno() + "\n" +
                                "Puntos pendientes por acumular: " + juego.getPuntosParciales(),
                        "Combinación Válida", JOptionPane.INFORMATION_MESSAGE);

                // Preguntar si desea continuar o terminar turno
                String[] opciones = {"Tirar dados restantes", "Terminar turno y acumular puntos"};
                int seleccion = JOptionPane.showOptionDialog(frame,
                        "¿Qué deseas hacer ahora?",
                        "Continuar jugando", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null, opciones, opciones[0]);

                // Mover dados seleccionados al panel de bloqueados
                for (Dado dado : dadosSeleccionados) {
                    panelSeleccionados.remove(dado.getBoton());
                    panelBloqueados.add(dado.getBoton());
                    dadosBloqueados.add(dado);
                }

                // Bloquear los dados seleccionados
                for (Dado dado : dadosBloqueados) {
                    dado.getBoton().setEnabled(false);
                }

                // Limpiar la lista de dados seleccionados
                dadosSeleccionados.clear();

                // Actualizar visualmente los paneles
                panelSeleccionados.revalidate();
                panelSeleccionados.repaint();
                panelBloqueados.revalidate();
                panelBloqueados.repaint();

                // Deshabilitar botón de acumular hasta la próxima tirada
                botonAcumular.setEnabled(false);

                if (seleccion == 1) {
                    // Terminar turno y acumular puntos
                    juego.guardarPuntosParciales(); // Asegurarse de que se añadan los puntos parciales
                    jugadorActual.sumarPuntos(juego.getPuntosTurno());
                    actualizarPuntuaciones();
                    etiquetaPuntosRonda.setText("Puntos ronda: 0");
                    siguienteTurno();
                    etiquetaTurnoActual.setText("Turno de: " + jugadorActual.getNombre());
                } else {
                    // Continuar jugando - habilitar el botón de tirar solamente
                    botonTirar.setEnabled(true);

                    // Deshabilitar los dados lanzados que no fueron seleccionados
                    for (Dado dado : dadosLanzados) {
                        dado.getBoton().setEnabled(false);
                    }
                    etiquetaPuntosRonda.setText("Puntos ronda: " + juego.getPuntosTurno());

                    if (dadosLanzados.isEmpty() && (dadosBloqueados.size() == 6)) {
                        // Si ya seleccionó los 6 dados, debe lanzar 6 nuevos
                        JOptionPane.showMessageDialog(frame,
                                "¡Has seleccionado todos los dados! Puedes lanzar 6 dados nuevos.",
                                "Continuar Jugando", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame,
                        "La selección no forma una combinación válida",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        botonMostrarCombinaciones.addActionListener(e -> {
            mostrarCombinaciones();
        });
    }

    private void siguienteTurno() {
        // Limpiar los dados actuales
        dadosLanzados.clear();
        dadosSeleccionados.clear();
        dadosBloqueados.clear();

        // Limpiar visualmente los paneles de dados
        panelLanzados.removeAll();
        panelSeleccionados.removeAll();
        panelBloqueados.removeAll();
        panelLanzados.revalidate();
        panelLanzados.repaint();
        panelSeleccionados.revalidate();
        panelSeleccionados.repaint();
        panelBloqueados.revalidate();
        panelBloqueados.repaint();

        // Resetear contadores en el juego
        juego.setDadosEnJuego(new ArrayList<>());
        juego.setDadosSeleccionados(new ArrayList<>());
        juego.setPuntosParciales(0);

        // Verificar si alguien ganó
        if (jugadorActual.getPuntuacionTotal() >= 5000) {
            JOptionPane.showMessageDialog(frame,
                    "¡FELICIDADES " + jugadorActual.getNombre().toUpperCase() + "! ¡HAS GANADO EL JUEGO!",
                    "Fin del Juego", JOptionPane.INFORMATION_MESSAGE);
            reiniciarJuego();
            return;
        }

        // Actualizar el turno
        int turnoActual = juego.getTurnoActual();
        turnoActual = (turnoActual + 1) % jugadores.size();
        juego.setTurnoActual(turnoActual);
        jugadorActual = jugadores.get(turnoActual);

        // Resetear puntos del turno
        juego.setPuntosTurno(0);

        // Notificar cambio de turno
        JOptionPane.showMessageDialog(frame,
                "Turno de " + jugadorActual.getNombre(),
                "Cambio de Turno", JOptionPane.INFORMATION_MESSAGE);

        // Habilitar el botón de tirar y deshabilitar el de acumular
        botonTirar.setEnabled(true);
        botonAcumular.setEnabled(false);
    }

    private void actualizarPuntuaciones() {
        panelPuntuaciones.removeAll();
        etiquetasPuntuaciones.clear();

        for (Jugador jugador : jugadores) {
            JLabel etiqueta = new JLabel(jugador.getNombre() + ": " + jugador.getPuntuacionTotal() + " puntos");
            if (jugador == jugadorActual) {
                etiqueta.setFont(new Font("Arial", Font.BOLD, 14));
                etiqueta.setForeground(Color.BLUE);
            } else {
                etiqueta.setFont(new Font("Arial", Font.PLAIN, 14));
                etiqueta.setForeground(Color.BLACK);
            }
            etiquetasPuntuaciones.add(etiqueta);
            panelPuntuaciones.add(etiqueta);
        }

        panelPuntuaciones.revalidate();
        panelPuntuaciones.repaint();
    }

    private void actualizarBotonAcumular() {
        if (dadosSeleccionados.isEmpty()) {
            botonAcumular.setEnabled(false);
        } else if (juego.esSeleccionValida(dadosSeleccionados)) {
            botonAcumular.setEnabled(true);
        } else {
            botonAcumular.setEnabled(false);
        }
    }

    private boolean puedeAcumular() {
        if (dadosSeleccionados.isEmpty()) {
            botonAcumular.setEnabled(false);
            return false;
        } else if (juego.esSeleccionValida(dadosSeleccionados)) {
            botonAcumular.setEnabled(true);
            return true;
        }
        botonAcumular.setEnabled(false);
        return false;
    }

    private void tirarDados() {
        // Limpiar los dados no seleccionados pero deshabilitados
        for (int i = dadosLanzados.size() - 1; i >= 0; i--) {
            panelLanzados.remove(dadosLanzados.get(i).getBoton());
        }
        dadosLanzados.clear();

        // También limpiar los dados seleccionados (que aún no se han acumulado)
        for (int i = dadosSeleccionados.size() - 1; i >= 0; i--) {
            panelSeleccionados.remove(dadosSeleccionados.get(i).getBoton());
        }
        dadosSeleccionados.clear();

        // Si es el primer lanzamiento del turno o se tiraron todos los dados anteriormente
        if (dadosBloqueados.isEmpty()) {
            dadosLanzados = juego.lanzarDados(6); // Tirar 6 dados nuevos
        } else {
            // Mantener los dados bloqueados y tirar solo los restantes
            int dadosRestantes = 6 - dadosBloqueados.size();
            if (dadosRestantes > 0) {
                dadosLanzados = juego.lanzarDados(dadosRestantes);
            } else {
                // Si no hay dados restantes, volver a lanzar 6 dados
                // Primero, mover los dados bloqueados a la lista de seleccionados para guardar sus puntos
                dadosBloqueados.clear();
                panelBloqueados.removeAll();
                panelBloqueados.revalidate();
                panelBloqueados.repaint();

                dadosLanzados = juego.lanzarDados(6);
                // Al tirar 6 dados nuevos, "guardar" los puntos anteriores
                juego.guardarPuntosParciales();
            }
        }

        panelLanzados.removeAll();

        for (Dado dado : dadosLanzados) {
            dado.getBoton().setEnabled(true); // Habilitar los nuevos dados
            dado.getBoton().addActionListener(ev -> {
                moverDado(dado);
            });
            panelLanzados.add(dado.getBoton());
        }

        panelLanzados.revalidate();
        panelLanzados.repaint();

        botonTirar.setEnabled(false);

        // Verificar si hay Farkle inmediatamente después de tirar
        if (!juego.hayCombinaciones(dadosLanzados)) {
            JOptionPane.showMessageDialog(frame,
                    "¡FARKLE! No hay combinaciones válidas. Pierdes los puntos de esta ronda.",
                    "Farkle", JOptionPane.WARNING_MESSAGE);
            juego.setPuntosTurno(0); // Perder puntos de la ronda actual
            juego.setPuntosParciales(0); // También resetear los puntos parciales
            siguienteTurno();
            return;
        }

        // Verificar si es Hot Dice
        if (juego.esHotDice(dadosLanzados)) {
            int puntuacionOptima = juego.calcularPuntuacionOptima(dadosLanzados);

            int respuesta = JOptionPane.showConfirmDialog(frame,
                    "¡HOT DICE! Todos los dados pueden ser seleccionados.\n" +
                            "Puntuación: " + puntuacionOptima + " puntos.\n\n" +
                            "¿Deseas acumular estos puntos y terminar tu turno?",
                    "Hot Dice", JOptionPane.YES_NO_OPTION);

            if (respuesta == JOptionPane.YES_OPTION) {
                // Acumular puntos y terminar turno
                juego.sumarPuntosTurno(puntuacionOptima);
                jugadorActual.sumarPuntos(juego.getPuntosTurno());
                actualizarPuntuaciones();
                siguienteTurno();
            } else {
                // Acumular puntos y seguir jugando con 6 nuevos dados
                juego.sumarPuntosTurno(puntuacionOptima);
                dadosLanzados.clear();
                dadosSeleccionados.clear();
                dadosBloqueados.clear();
                panelLanzados.removeAll();
                panelSeleccionados.removeAll();
                panelBloqueados.removeAll();
                panelLanzados.revalidate();
                panelLanzados.repaint();
                panelSeleccionados.revalidate();
                panelSeleccionados.repaint();
                panelBloqueados.revalidate();
                panelBloqueados.repaint();

                JOptionPane.showMessageDialog(frame,
                        "Puntos acumulados en este turno: " + juego.getPuntosTurno() + "\n" +
                                "Puedes tirar 6 dados nuevos.",
                        "Continuar Jugando", JOptionPane.INFORMATION_MESSAGE);
                botonTirar.setEnabled(true);
            }
        }

        actualizarBotonAcumular();
    }

    private void moverDado(Dado dado) {
        // No permitir mover dados que ya están bloqueados
        if (dadosBloqueados.contains(dado)) {
            return;
        }

        if (dadosLanzados.contains(dado)) {
            // Mover de lanzados a seleccionados
            panelLanzados.remove(dado.getBoton());
            panelSeleccionados.add(dado.getBoton());
            dadosLanzados.remove(dado);
            dadosSeleccionados.add(dado);
        } else if (dadosSeleccionados.contains(dado)) {
            // Mover de seleccionados a lanzados
            panelSeleccionados.remove(dado.getBoton());
            panelLanzados.add(dado.getBoton());
            dadosSeleccionados.remove(dado);
            dadosLanzados.add(dado);
        }

        panelLanzados.revalidate();
        panelLanzados.repaint();
        panelSeleccionados.revalidate();
        panelSeleccionados.repaint();

        actualizarBotonAcumular();
    }

    public void mostrarCombinaciones() {
        JPanel panelDeCombinaciones = new JPanel();
        //JLabel labelDeCombinaciones = new JLabel(new ImageIcon("C:\\Users\\PC OSTRICH\\Pr-ctica-4\\Combinaciones.png"));
        JLabel labelDeCombinaciones = new JLabel(new ImageIcon("G:\\4toSemestre\\POO\\Practica-4.1\\imagenes\\img.png"));
        //JLabel labelDeCombinaciones = new JLabel(new ImageIcon("C:\\Users\\GF76\\IdeaProjects\\Practica-4.2\\imagenes\\img.png"));
        panelDeCombinaciones.add(labelDeCombinaciones);
        JOptionPane optionPane = new JOptionPane(panelDeCombinaciones, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION);
        JDialog ventanaC = optionPane.createDialog("Combinaciones");
        ventanaC.setLocation(1145, 70);
        ventanaC.setVisible(true);
    }

    private void reiniciarJuego() {
        // Cerrar la ventana actual
        frame.dispose();

        // Crear un nuevo juego desde cero
        Farkle nuevoJuego = new Farkle();
        VentanaJuego nuevaVentana = new VentanaJuego(nuevoJuego);
    }
}


