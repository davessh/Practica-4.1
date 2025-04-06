import java.util.ArrayList;

public class Farkle {
    private ArrayList<Dado> dadosEnJuego;
    private ArrayList<Dado> dadosSeleccionados;
    private ArrayList<Jugador> jugadores;
    private int puntosTurno;
    boolean esEscalera = true;
    boolean todosIguales = true;
    private int turnoActual;

    public Farkle() {
        jugadores = new ArrayList<>();
        dadosEnJuego = new ArrayList<>();
        dadosSeleccionados = new ArrayList<>();
        turnoActual = 0;
        puntosTurno = 0;
    }

    public void comenzarJuego() {
        jugadores.add(new Jugador("player 1"));
        jugadores.add(new Jugador("player 2"));
        while (true) {
            Jugador jugadorActual = jugadores.get(turnoActual);
            jugadorActual.iniciarTurno();
            dadosEnJuego = lanzarDados();

            boolean tieneCombinacion = hayCombinaciones(dadosEnJuego);
            if (tieneCombinacion) {
                jugadorActual.sumarPuntos(calcularPuntos(dadosEnJuego));
                System.out.println(jugadorActual.getNombre() + " tiene puntos de combinacion");
            }
            else {
                System.out.println(jugadorActual.getNombre() + "Farkle,no tiene combinación");
                jugadorActual.terminarTurno(false);
                turnoActual = (turnoActual + 1) % jugadores.size();
                continue;
            }

            if (jugadorActual.getPuntuacionTotal() >= 10000) {
                System.out.println(jugadorActual.getNombre() + " ha ganado el juego!");
                break;  // Termina el juego
            }


            jugadorActual.terminarTurno(false);
            turnoActual = (turnoActual + 1) % jugadores.size();
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
        // se verifica si es escalera
        for (int i = 1; i <= 6; i++) {
            if (contador[i] != 1) {
                esEscalera = false;
                break;
            }
        }

        if (esEscalera) {
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

    //método diseñado para determinar si todos mis valores tirados son iguales
    public boolean sonTodosIguales(){
        if (dadosEnJuego.isEmpty()){
            return false;
        }

        int valorInicial = dadosEnJuego.get(0).getValor();
        for(Dado dado : dadosEnJuego){
            if(dado.getValor() != valorInicial){
                return false;
            }
            }
        return true;
        }


    public void seleccionarDado(Dado dado) {
        dadosEnJuego.remove(dado);
        dadosSeleccionados.add(dado);
    }

    public void deseleccionarDado(Dado dado) {
        dadosSeleccionados.remove(dado);
        dadosEnJuego.add(dado);
    }

    public int calcularPuntos(ArrayList<Dado> dados) {
        int puntos = 0;
        for (Dado dado : dados) {
            if (dado.getValor() == 1) {
                puntos += 100;
            } else if (dado.getValor() == 5) {
                puntos += 50;
            }
        }
        return puntos;
    }

        public static void main (String[]args){
            Farkle juego = new Farkle();
            juego.comenzarJuego();
        }
}
