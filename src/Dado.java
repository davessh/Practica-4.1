import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Dado {
    private int valor;
    private JButton boton;
    private static Random rnd = new Random();

    public Dado() {
        lanzar();
    }
    public void lanzar() {
        valor = rnd.nextInt(6) +1;
        String ruta = "G:\\4toSemestre\\POO\\Practica-4.1\\imagenes\\dado" + valor + ".png";
        //String ruta = "C:\\Users\\Usuario\\IdeaProjects\\Practica-4.1\\imagenes\\dado" + valor + ".png";
        Icon icono = new ImageIcon(ruta);

        Image img = ((ImageIcon) icono).getImage();
        Image newImg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);  // Cambia el tamaño aquí
        icono = new ImageIcon(newImg);
        boton = new JButton(icono);
        boton.setBorder(null);
        boton.setContentAreaFilled(false);

    }

    public JButton getBoton() {
        return boton;
    }
    public int getValor() {
        return valor;
    }
}
