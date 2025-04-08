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

//        // Configuración inicial de jugadores
//        System.out.println("¡Bienvenidos a Farkle!");
//        System.out.print("Ingrese el nombre del Jugador 1: ");
//        String nombreJugador1 = scanner.nextLine();
//        System.out.print("Ingrese el nombre del Jugador 2: ");
//        String nombreJugador2 = scanner.nextLine();

//        jugadores.add(new Jugador(nombreJugador1));
//        jugadores.add(new Jugador(nombreJugador2));



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
                if (tieneCombinacion && esHotDice(dadosEnJuego)) {
                    int puntuacionOptima = calcularPuntuacionOptima(dadosEnJuego);
                    System.out.println("\n¡HOT DICE! Todos los dados pueden ser seleccionados.");
                    System.out.println("Puntuación óptima: " + puntuacionOptima);

                    // Automáticamente seleccionar todos los dados
                    ArrayList<Dado> todosDados = new ArrayList<>(dadosEnJuego);

                    // Preguntar si desea acumular la puntuación o volver a tirar
                    System.out.print("¿Desea acumular esta puntuación (A) o volver a tirar los 6 dados (T)? ");
                    String decision = scanner.nextLine().toUpperCase();

                    if (decision.equals("A")) {
                        // Acumular puntuación
                        puntosTurno += puntuacionOptima;
                        jugadorActual.sumarPuntos(puntosTurno);
                        System.out.println(jugadorActual.getNombre() + " suma " + puntosTurno + " puntos. Total: " + jugadorActual.getPuntuacionTotal());
                        turnoActivo = false;
                    } else {
                        // Volver a tirar todos los dados
                        puntosTurno += puntuacionOptima;
                        System.out.println("Puntos acumulados en este turno: " + puntosTurno);
                        dadosEnJuego.clear();
                        dadosSeleccionados.clear();
                        dadosEnJuego = lanzarDados();
                    }
                }
                else if (tieneCombinacion) {
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
                } else if(dadosEnJuego.isEmpty()) {
                    System.out.println("Se quedó sin dados, desea volver a tirar? s/n");
                    String continuar = scanner.nextLine().toLowerCase();
                    if (!continuar.equals("s")) {
                        jugadorActual.sumarPuntos(puntosTurno);
                        System.out.println(jugadorActual.getNombre() + " suma " + puntosTurno + " puntos. Total: " + jugadorActual.getPuntuacionTotal());
                        turnoActivo = false;
                    } else
                        dadosEnJuego = lanzarDados();
                } else {
                    System.out.println(jugadorActual.getNombre() + " ¡Farkle! No hay combinaciones posibles.");
                    puntosTurno = 0;
                    turnoActivo = false;
                }
            }

            // Verificar si alguien ganó
            if (jugadorActual.getPuntuacionTotal() >= 5000) {
                System.out.println("\n¡FELICIDADES " + jugadorActual.getNombre().toUpperCase() + "! ¡HAS GANADO EL JUEGO!");
                System.out.println("Puntuación final: " + jugadorActual.getPuntuacionTotal());
                break;
            }

            // Pasar al siguiente jugador
            jugadorActual.terminarTurno(false);
            dadosEnJuego.clear();
            dadosSeleccionados.clear();
            turnoActual = (turnoActual + 1) % jugadores.size();
        }
        scanner.close();
    }

    private boolean sonTodosSeleccionablesEnCombinacion(ArrayList<Dado> dados) {
        int[] contador = contadorCoincidencias(dados);

        // Si es escalera o 3 pares, todos los dados son parte de la combinación
        if (esEscalera(contador) || hay3Pares(contador)) {
            return true;
        }

        // Contar cuántos dados pueden ser parte de combinaciones válidas
        int dadosEnCombinacion = 0;

        // Verificar tríos o más
        for (int i = 1; i <= 6; i++) {
            if (contador[i] >= 3) {
                dadosEnCombinacion += contador[i];
            } else if (i == 1) {
                // Los 1 siempre cuentan
                dadosEnCombinacion += contador[i];
            } else if (i == 5) {
                // Los 5 siempre cuentan
                dadosEnCombinacion += contador[i];
            }
        }

        return dadosEnCombinacion == dados.size();
    }

    public int calcularPuntuacionOptima(ArrayList<Dado> dados) {
        // Si todos los dados pueden formar combinaciones, los seleccionamos todos
        return calcularPuntos(dados);
    }

    public boolean esHotDice(ArrayList<Dado> dados) {
        // Si todos los dados pueden ser seleccionados como combinación válida
        return hayCombinaciones(dados) && sonTodosSeleccionablesEnCombinacion(dados);
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
        int[] contador = contadorCoincidencias(seleccion);
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
        if (hayTresIguales(contador) || hayCuatroIguales(contador) || hayCincoIguales(contador) || haySeisIguales(contador)) {
            return true;
        }

        // Verificar unos y cincos individuales
        if (contador[1] > 0 || contador[5] > 0) {
            return true;
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

    private boolean haySeisIguales(int[] contador) {
        for (int i = 1; i <= 6; i++) {
            if (contador[i] == 6) {
                return true;
            }
        }
        return false;
    }

    private boolean hayCincoIguales(int[] contador) {
        for (int i = 1; i <= 6; i++) {
            if (contador[i] >= 5) {
                return true;
            }
        }
        return false;
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
        int trios = 0;

        // Escalera completa (1-2-3-4-5-6) - 2500 puntos
        if (esEscalera(contador)) {
            return 2500; // Esta combinación usa todos los dados, podemos retornar inmediatamente
        }

        // Tres pares - 1500 puntos
        if (hay3Pares(contador)) {
            return 1500; // Esta combinación usa todos los dados, podemos retornar inmediatamente
        }

        // 6 dados iguales - 3000 puntos
        if(haySeisIguales(contador)) {
            return 3000;
        }

        // Dos trios - 2500 puntos
        for (int i = 1; i <= 6; i++) {
            if (contador[i] >= 3) {
                trios++;
            }
        }
        if (trios >= 2) {
            return 2500; // Usa todos los dados
        }

        // Combinaciones especiales para unos (4 unos = 1000, 5 unos = 2000)
        if (hayCuatroIguales(contador)) {
            puntos += (contador[1] == 5) ? 2000 : 1000;
            contador[1] = 0;
        }

        // Combinaciones de 5 iguales (2000 puntos) - excepto unos que ya se manejó
        for (int i = 2; i <= 6; i++) {
            if (contador[i] == 5) {
                puntos += 2000;
                contador[i] = 0;
            }
        }

        // Combinaciones de 4 iguales (1000 puntos) - excepto unos que ya se manejó
        for (int i = 2; i <= 6; i++) {
            if (contador[i] == 4) {
                puntos += 1000;
                contador[i] = 0;
            }
        }

        // Trios (3 iguales)
        for (int i = 1; i <= 6; i++) {
            if (contador[i] >= 3) {
                puntos += (i == 1) ? 1000 : i * 100;
                contador[i] -= 3; // Restamos 3 para dejar los dados sobrantes
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

    public ArrayList<Dado> getDadosEnJuego() {
        return dadosEnJuego;
    }

    public ArrayList<Dado> getDadosSeleccionados() {
        return dadosSeleccionados;
    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

    public int getPuntosTurno() {
        return puntosTurno;
    }

    public int getTurnoActual() {
        return turnoActual;
    }

    public void setDadosEnJuego(ArrayList<Dado> dadosEnJuego) {
        this.dadosEnJuego = dadosEnJuego;
    }

    public void setDadosSeleccionados(ArrayList<Dado> dadosSeleccionados) {
        this.dadosSeleccionados = dadosSeleccionados;
    }

    public void setJugadores(ArrayList<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public void setPuntosTurno(int puntosTurno) {
        this.puntosTurno = puntosTurno;
    }

    public void setTurnoActual(int turnoActual) {
        this.turnoActual = turnoActual;
    }

    public static void main(String[] args) {
        Farkle juego = new Farkle();
        juego.comenzarJuego();
    }
}

