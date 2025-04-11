import java.util.ArrayList;
import java.util.Scanner;

public class Farkle {
    private ArrayList<Dado> dadosEnJuego;
    private ArrayList<Dado> dadosSeleccionados;
    private ArrayList<Jugador> jugadores;
    private int puntosTurno;
    private int turnoActual;
    private int puntosParciales;

    public Farkle() {
        jugadores = new ArrayList<>();
        dadosEnJuego = new ArrayList<>();
        dadosSeleccionados = new ArrayList<>();
        turnoActual = 0;
        puntosTurno = 0;
        puntosParciales = 0;
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

    public boolean esSeleccionValida(ArrayList<Dado> seleccion) {
        if (seleccion.isEmpty()) {
            return false;
        }

        int[] contador = contadorCoincidencias(seleccion);

        // Verificar combinaciones especiales primero
        if (esEscalera(contador) && seleccion.size() == 6) {
            return true;
        }

        if (hay3Pares(contador) && seleccion.size() == 6) {
            return true;
        }

        // Verificar tríos o más
        for (int i = 1; i <= 6; i++) {
            if (contador[i] >= 3) {
                // Si hay un trío, verificar que el resto sean 1s o 5s
                for (int j = 1; j <= 6; j++) {
                    if (j != i && contador[j] > 0) {
                        if (j != 1 && j != 5) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }

        // Si no hay tríos, verificar que todos sean 1s o 5s
        for (int i = 1; i <= 6; i++) {
            if (contador[i] > 0 && i != 1 && i != 5) {
                return false;
            }
        }

        return contador[1] > 0 || contador[5] > 0;
    }

    public ArrayList<Dado> lanzarDados(int cantidad) {
        ArrayList<Dado> dados = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            dados.add(new Dado());
        }
        return dados;
    }

    public void sumarPuntosParciales(int puntos) {
        this.puntosParciales += puntos;
    }

    public void guardarPuntosParciales() {
        this.puntosTurno += this.puntosParciales;
        this.puntosParciales = 0;
    }

    public void sumarPuntosTurno(int puntos) {
        this.puntosTurno += puntos;
    }

    public int getPuntosParciales() {
        return puntosParciales;
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

    public int calcularPuntos(ArrayList<Dado> dados) {
        int[] contador = contadorCoincidencias(dados);
        int puntos = 0;
        int trios = 0;

        // Escalera completa (1-2-3-4-5-6) - 2500 puntos
        if (esEscalera(contador) && dados.size() == 6) {
            return 2500; // Esta combinación usa todos los dados, podemos retornar inmediatamente
        }

        // Tres pares - 1500 puntos
        if (hay3Pares(contador) && dados.size() == 6) {
            return 1500; // Esta combinación usa todos los dados, podemos retornar inmediatamente
        }

        // 6 dados iguales - 3000 puntos
        if (haySeisIguales(contador) && dados.size() == 6) {
            return 3000;
        }

        // Contar tríos para combinación de dos tríos
        for (int i = 1; i <= 6; i++) {
            if (contador[i] >= 3) {
                trios++;
            }
        }
        if (trios >= 2 && dados.size() == 6) {
            return 2500;
        }

        // Procesar 5 dados iguales (2000 puntos para todos los números)
        for (int i = 1; i <= 6; i++) {
            if (contador[i] >= 5) {
                puntos += 2000;
                contador[i] -= 5; // Restar los dados contados
                continue;
            }
        }

        // 4 dados iguales 1000 puntos
        for (int i = 1; i <= 6; i++) {
            if (contador[i] >= 4) {
                puntos += 1000;
                contador[i] -= 4; // Restar los dados contados
                continue;
            }
        }

        // Procesar tríos (tercias) con sus valores específicos
        for (int i = 1; i <= 6; i++) {
            if (contador[i] >= 3) {
                switch (i) {
                    case 1:
                        puntos += 1000; // Tercia de 1 = 1000 puntos
                        break;
                    case 2:
                        puntos += 200;  // Tercia de 2 = 200 puntos
                        break;
                    case 3:
                        puntos += 300;  // Tercia de 3 = 300 puntos
                        break;
                    case 4:
                        puntos += 400;  // Tercia de 4 = 400 puntos
                        break;
                    case 5:
                        puntos += 500;  // Tercia de 5 = 500 puntos
                        break;
                    case 6:
                        puntos += 600;  // Tercia de 6 = 600 puntos
                        break;
                }
                contador[i] -= 3; // Restar los dados contados, pueden quedar sobrantes
            }
        }

        // Unos y cincos individuales (se suman a la puntuación de las combinaciones)
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

    public void setPuntosParciales(int puntosParciales) {
        this.puntosParciales = puntosParciales;
    }

    public ArrayList<Dado> lanzarDadosRestantes(int cantidad) {
        ArrayList<Dado> dados = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            Dado nuevoDado = new Dado();
            nuevoDado.lanzar();
            dados.add(nuevoDado);
        }
        return dados;
    }
}

