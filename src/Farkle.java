import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Farkle {
    private ArrayList<Dado> dadosEnJuego;
    private ArrayList<Dado> dadosSeleccionados;
    private ArrayList<Jugador> jugadores;
    private int puntosTurno;
//    private JFrame frame;
//
//    public Farkle() {
//        mostrarMenu();
//    }

    public Farkle() {
        //this.jugadores = jugadores;
        dadosEnJuego = new ArrayList<>();
        dadosSeleccionados = new ArrayList<>();
        puntosTurno = 0;
    }

    public void comenzarJuego() {
        boolean continuar = true;
        while (continuar) {
            dadosEnJuego = lanzarDados();
            if(hayCombinaciones(dadosEnJuego)){

            }

        }

    }

    public ArrayList<Dado> lanzarDados() {
        dadosEnJuego.clear();
        int cantidadDados = 6 - dadosSeleccionados.size();
        System.out.println("cantidad de dados: " + cantidadDados);
        for (int i = 0; i < cantidadDados; i++) {
            dadosEnJuego.add(new Dado());
        }
        return dadosEnJuego;
    }

    public boolean hayCombinaciones(ArrayList<Dado> dados) {
        int[] contador = new int[7];
        //Se cuenta cuántas veces salió un valor en los dados lanzados y sirve para saber cuales valores podrían formar combinaciones
        for(Dado dado : dadosEnJuego){
            contador[dado.getValor()]++;
        }
        boolean combinacion = false;

        //Si hay unos o cincos
        if (contador[1] > 0 || contador[5] > 0) {
            combinacion = true;
        }
        //Si hay tres del mismo valor
        for (int i = 1; i <= 6; i++) {
            if (contador[i] >= 3) {
                combinacion = true;
            }
        }
        //Si hay escalera
        if (contador[1]==1 && contador[2]==1 && contador[3]==1 &&
                contador[4]==1 && contador[5]==1 && contador[6]==1) {
            combinacion = true;
        }

        //Si hay 3 pares
        int pares = 0;
        for (int i = 1; i <= 6; i++) {
            if (contador[i] == 2) {
                pares++;
            }
        }
        if (pares == 3) {
            combinacion = true;
        }

        return combinacion;

    }

    public void seleccionarDado(Dado dado) {
        dadosEnJuego.remove(dado);
        dadosSeleccionados.add(dado);
    }

    public void deseleccionarDado(Dado dado) {
        dadosSeleccionados.remove(dado);
        dadosEnJuego.add(dado);
    }



    public static void main(String[] args) {
       // new Farkle();
    }
}
