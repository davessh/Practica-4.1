import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class VentanaJuego {

    private ArrayList<Dado> dadosLanzados = new ArrayList<>();
    private ArrayList<Dado> dadosSeleccionados = new ArrayList<>();

    private JPanel panelLanzados;
    private JPanel panelSeleccionados;
    private JFrame frame;

    private Jugador jugador;

    public VentanaJuego() {

        jugador = new Jugador("Jugador 1");

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
        ArrayList<Dado> dadosGenerados = jugador.tirarDados();

        panelLanzados.removeAll();
        dadosLanzados.clear();

        for (Dado dado : dadosGenerados) {
            dado.getBoton().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ev) {
                    moverDado(dado);
                }
            });
            dadosLanzados.add(dado);
            panelLanzados.add(dado.getBoton());
        }

        // Actualizar la interfaz
        panelLanzados.revalidate();
        panelLanzados.repaint();
    }

    private void moverDado(Dado dado) {
        panelLanzados.remove(dado.getBoton());
        panelSeleccionados.add(dado.getBoton());

        dadosLanzados.remove(dado);
        dadosSeleccionados.add(dado);

        panelLanzados.revalidate();
        panelLanzados.repaint();
        panelSeleccionados.revalidate();
        panelSeleccionados.repaint();
    }

    public static void main(String[] args) {
        new VentanaJuego();
    }
}


