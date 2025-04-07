import java.util.ArrayList;

public class Farkle {
    private ArrayList<Dado> dadosEnJuego;
    private ArrayList<Dado> dadosSeleccionados;
    private ArrayList<Jugador> jugadores;
    private int puntosTurno;
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
                int puntos = calcularPuntos(dadosEnJuego);
                jugadorActual.sumarPuntos(puntos);
                System.out.println(jugadorActual.getNombre() + " tiene puntos de combinacion: " + puntos);
                for (String combinacion : obtenerCombinaciones(dadosEnJuego)) {
                    System.out.println("- " + combinacion);
                }
                System.out.println("Puntos ganados: " + puntos);
            } else {
                System.out.println(jugadorActual.getNombre() + " Farkle, no tiene combinación");
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

    public int[] contadorCoincidencias(ArrayList<Dado> dados) {
        int[] contador = new int[7];
        for (Dado dado : dados) {
            contador[dado.getValor()]++;
        }
        return contador;
    }

    public boolean hayCombinaciones(ArrayList<Dado> dados) {
        int[] contador = contadorCoincidencias(dados);
        return esEscalera(contador)
                || hay3Pares(contador)
                || hayTresIguales(contador)
                || hayCuatroIguales(contador)
                || sonTodosIguales();
    }

    private boolean esEscalera(int[] contador) {
        for (int i = 1; i <= 6; i++) {
            if (contador[i] != 1) {
                return false;
            }
        }
        return true;
    }

    private boolean hayTresIguales(int[] contador) {
        for (int i = 1; i <= 6; i++) {
            if (contador[i] >= 3) {
                return true;
            }
        }
        return false;
    }

    private boolean hayCuatroIguales(int[] contador) {
        for (int i = 1; i <= 6; i++) {
            if (contador[i] >= 4) {
                return true;
            }
        }
        return false;
    }

    public boolean hay3Pares(int[] contador) {
        int pares = 0;
        for (int i = 1; i <= 6; i++) {
            if (contador[i] == 2) {
                pares++;
            }
        }
        return pares == 3;
    }

    public boolean sonTodosIguales() {
        if (dadosEnJuego.isEmpty()) {
            return false;
        }

        int valorInicial = dadosEnJuego.get(0).getValor();
        for (Dado dado : dadosEnJuego) {
            if (dado.getValor() != valorInicial) {
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
        int[] contador = contadorCoincidencias(dados);
        int puntos = 0;

        // Escalera: 1 al 6, cada uno una vez
        if (esEscalera(contador)) {
            return 1500;
        }

        // Tres pares
        if (hay3Pares(contador)) {
            return 1500;
        }

        // Dos ternas
        int ternas = 0;
        for (int i = 1; i <= 6; i++) {
            if (contador[i] == 3) {
                ternas++;
            }
        }
        if (ternas == 2) {
            return 2500;
        }

        // Repeticiones de 3 o más
        for (int i = 1; i <= 6; i++) {
            if (contador[i] >= 3) {
                int base = (i == 1) ? 1000 : i * 100;
                int multiplicador = contador[i] - 2;
                puntos += base * multiplicador;
                contador[i] = 0; // Reset the count for these dice
            }
        }

        // Unos y cincos sueltos
        puntos += contador[1] * 100;
        puntos += contador[5] * 50;

        return puntos;
    }

    public ArrayList<String> obtenerCombinaciones(ArrayList<Dado> dados) {
        ArrayList<String> combinaciones = new ArrayList<>();
        int[] contador = contadorCoincidencias(dados);

        if (esEscalera(contador)) {
            combinaciones.add("Escalera (1-6)");
        }

        if (hay3Pares(contador)) {
            combinaciones.add("Tres pares");
        }

        for (int i = 1; i <= 6; i++) {
            if (contador[i] >= 3) {
                combinaciones.add("Tres o más " + i + "s");
            }
            if (i == 1 && contador[i] > 0 && contador[i] < 3) {
                combinaciones.add(contador[i] + " uno(s) individuales");
            }
            if (i == 5 && contador[i] > 0 && contador[i] < 3) {
                combinaciones.add(contador[i] + " cinco(s) individuales");
            }
        }

        return combinaciones;
    }

    public static void main(String[] args) {
        Farkle juego = new Farkle();
        juego.comenzarJuego();
    }
}

