import java.util.ArrayList;

public class Jugador {
    private String nombre;
    private int puntuacionTotal;
    private int puntuacionRonda;
    private boolean estaActivo; //Para saber si es su turno

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.puntuacionTotal = 0;
        this.puntuacionRonda = 0;
        this.estaActivo = false;
    }

    public String getNombre() {
        return nombre;
    }

    public void sumarPuntos(int puntos) {
        this.puntuacionRonda += puntos;
    }

    public void iniciarTurno(){
        puntuacionRonda = 0;
        estaActivo = true;
    }

    public void terminarTurno(boolean sumarPuntaje){
        if(sumarPuntaje){
            puntuacionTotal += puntuacionRonda;
        }
        puntuacionRonda = 0;
        estaActivo = false;
    }

    public int getPuntuacionTotal() {
        return puntuacionTotal;
    }

    public int getPuntuacionRonda() {
        return puntuacionRonda;
    }

    public boolean estaActivo() {
        return estaActivo;
    }



}
