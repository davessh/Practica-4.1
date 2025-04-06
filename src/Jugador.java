import java.util.ArrayList;

public class Jugador {
    int puntuacionTotal;
    int puntuacionRonda;
    boolean puedeJugar;

     ArrayList<Dado> dadosLanzados = new ArrayList<>();
     ArrayList<Dado> dadosSeleccionados = new ArrayList<>();

    public Jugador(String nombre) {
        puntuacionTotal = 0;
        puntuacionRonda = 0;
        puedeJugar = false;
    }


    public ArrayList<Dado> tirarDados() {
        ArrayList<Dado> dadosGenerados = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            Dado dado = new Dado();
            dadosGenerados.add(dado); // Agregar el dado al ArrayList
        }

        dadosLanzados.clear();
        dadosLanzados.addAll(dadosGenerados);

        return dadosGenerados;
    }


    public void agregarPuntos(int puntos) {
        this.puntuacionRonda += puntos;
    }

    public void reiniciarPuntuacionRonda() {
        this.puntuacionRonda = 0;
    }

    public void agregarPuntosTotales() {
        this.puntuacionTotal += puntuacionRonda;
    }

    public int getPuntuacionTotal() {
        return puntuacionTotal;
    }

    public int getPuntuacionRonda() {
        return puntuacionRonda;
    }

    public boolean sePuedeJugar() {
        return puedeJugar;
    }
}
