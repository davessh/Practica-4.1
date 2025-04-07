import java.util.ArrayList;
import java.util.Scanner;

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
        Scanner scanner = new Scanner(System.in);

        // Configuración inicial de jugadores
        System.out.println("¡Bienvenidos a Farkle!");
        System.out.print("Ingrese el nombre del Jugador 1: ");
        String nombreJugador1 = scanner.nextLine();
        System.out.print("Ingrese el nombre del Jugador 2: ");
        String nombreJugador2 = scanner.nextLine();

        jugadores.add(new Jugador(nombreJugador1));
        jugadores.add(new Jugador(nombreJugador2));

        while (true) {
            Jugador jugadorActual = jugadores.get(turnoActual);
            System.out.println("\n--- Turno de " + jugadorActual.getNombre() + " ---");
            System.out.println("Puntuación actual: " + jugadorActual.getPuntuacionTotal());

            jugadorActual.iniciarTurno();
            dadosEnJuego = lanzarDados();
            dadosSeleccionados.clear();
            puntosTurno = 0;

            boolean turnoActivo = true;
            while (turnoActivo) {
                System.out.println("\nDados lanzados: " + obtenerValoresDados(dadosEnJuego));

                boolean tieneCombinacion = hayCombinaciones(dadosEnJuego);
                if (tieneCombinacion) {
                    ArrayList<String> combinaciones = obtenerCombinaciones(dadosEnJuego);
                    System.out.println("\nCombinaciones disponibles:");
                    for (String combinacion : combinaciones) {
                        System.out.println("- " + combinacion);
                    }

                    // Interacción para seleccionar dados
                    System.out.println("\nSeleccione los dados que desea guardar (ej. 1 3 5):");
                    String seleccion = scanner.nextLine();
                    ArrayList<Dado> seleccionados = procesarSeleccion(seleccion, dadosEnJuego);

                    if (seleccionados.isEmpty()) {
                        System.out.println("No seleccionó dados válidos. Intente nuevamente.");
                        continue;
                    }

                    // Validar que la selección contiene combinaciones válidas
                    if (!esSeleccionValida(seleccionados)) {
                        System.out.println("Selección no válida. Debe seleccionar dados que formen combinaciones.");
                        continue;
                    }

                    // Calcular puntos y actualizar
                    int puntosGanados = calcularPuntos(seleccionados);
                    puntosTurno += puntosGanados;
                    //jugadorActual.sumarPuntos(puntosGanados);

                    // Mover dados seleccionados
                    for (Dado dado : seleccionados) {
                        seleccionarDado(dado);
                    }

                    System.out.println("Puntos ganados en esta ronda: " + puntosGanados);
                    System.out.println("Puntos acumulados en el turno: " + puntosTurno);

                    // Preguntar si quiere continuar
                    if (dadosEnJuego.isEmpty()) {
                        System.out.println("¡Todos los dados seleccionados! Lanzando 6 dados nuevamente.");
                        dadosEnJuego = lanzarDados();
                        dadosSeleccionados.clear();
                        continue;
                    }

                    System.out.print("¿Desea lanzar los " + dadosEnJuego.size() + " dados restantes? (s/n): ");
                    String continuar = scanner.nextLine().toLowerCase();

                    if (!continuar.equals("s")) {
                        jugadorActual.sumarPuntos(puntosTurno);
                        System.out.println(jugadorActual.getNombre() + " suma " + puntosTurno + " puntos. Total: " + jugadorActual.getPuntuacionTotal());
                        turnoActivo = false;
                    } else {
                        dadosEnJuego = lanzarDados();
                    }
                } else {
                    System.out.println(jugadorActual.getNombre() + " ¡Farkle! No hay combinaciones posibles.");
                    puntosTurno = 0;
                    turnoActivo = false;
                }
            }

            // Verificar si alguien ganó
            if (jugadorActual.getPuntuacionTotal() >= 1000) {
                System.out.println("\n¡FELICIDADES " + jugadorActual.getNombre().toUpperCase() + "! ¡HAS GANADO EL JUEGO!");
                System.out.println("Puntuación final: " + jugadorActual.getPuntuacionTotal());
                break;
            }

            // Pasar al siguiente jugador
            jugadorActual.terminarTurno(false);
            turnoActual = (turnoActual + 1) % jugadores.size();
        }
        scanner.close();
    }

    // Métodos auxiliares adicionales necesarios
    private ArrayList<Dado> procesarSeleccion(String input, ArrayList<Dado> dadosDisponibles) {
        ArrayList<Dado> seleccionados = new ArrayList<>();
        String[] indices = input.split(" ");

        try {
            for (String indiceStr : indices) {
                int indice = Integer.parseInt(indiceStr) - 1;
                if (indice >= 0 && indice < dadosDisponibles.size()) {
                    seleccionados.add(dadosDisponibles.get(indice));
                }
            }
        } catch (NumberFormatException e) {
            return new ArrayList<>();
        }

        return seleccionados;
    }

    private boolean esSeleccionValida(ArrayList<Dado> seleccion) {
        // Verifica que al menos haya un 1, un 5 o una combinación válida
        int[] contador = new int[7];
        for (Dado dado : seleccion) {
            contador[dado.getValor()]++;
        }

        // Verificar si hay 1s o 5s individuales
        if (contador[1] > 0 || contador[5] > 0) {
            return true;
        }

        // Verificar otras combinaciones
        for (int i = 1; i <= 6; i++) {
            if (contador[i] >= 3) {
                return true;
            }
        }

        return false;
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

        // Verificar combinaciones especiales primero
        if (esEscalera(contador) || hay3Pares(contador)) {
            return true;
        }

        // Verificar combinaciones de iguales
        for (int i = 1; i <= 6; i++) {
            if (contador[i] >= 3 || (i == 1 && contador[i] > 0) || (i == 5 && contador[i] > 0)) {
                return true;
            }
        }

        return false;
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
        int totalDados = dados.size();

        // Escalera completa
        if (esEscalera(contador)) {
            return 2500;
        }

        // Tres pares
        if (hay3Pares(contador)) {
            return 1500;
        }

        // Combinaciones de iguales
        for (int i = 1; i <= 6; i++) {
            if (contador[i] == 6) {
                return 3000;
            }
            if (contador[i] == 5) {
                return 2000;
            }
            if (contador[i] == 4) {
                return 1000;
            }
            if (contador[i] == 3) {
                puntos += (i == 1) ? 1000 : i * 100;
                contador[i] = 0; // Reset para no contar estos dados nuevamente
            }
        }

        // Unos y cincos individuales
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

    private String obtenerValoresDados(ArrayList<Dado> dados) {
        StringBuilder sb = new StringBuilder();
        for (Dado dado : dados) {
            sb.append(dado.getValor()).append(" ");
        }
        return sb.toString().trim();
    }

    public static void main(String[] args) {
        Farkle juego = new Farkle();
        juego.comenzarJuego();
    }
}

