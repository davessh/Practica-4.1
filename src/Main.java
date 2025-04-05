import javax.swing.*;
import java.util.ArrayList;

public class Main {

    private ArrayList<Dado> dadosLanzados = new ArrayList<>();
    private ArrayList<Dado> dadosSeleccionados = new ArrayList<>();

    private JPanel panelLanzados;
    private JPanel panelSeleccionados;

    public static void main(String[] args) {
        JPanel panel = new JPanel();
        Dado dado = new Dado();
        panel.add(dado.getBoton());
        JFrame frame = new JFrame("Farkle");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(panel);
        frame.setVisible(true);

    }
}
