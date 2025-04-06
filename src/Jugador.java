import java.util.ArrayList;

public class Jugador {
    private String nombre;
    private int puntuacionTotal;
    private int puntuacionRonda;
    private boolean estaActivo; //Para saber si es su turno

//     ArrayList<Dado> dadosLanzados = new ArrayList<>();
//     ArrayList<Dado> dadosSeleccionados = new ArrayList<>();

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.puntuacionTotal = 0;
        this.puntuacionRonda = 0;
        this.estaActivo = false;
    }


//    public ArrayList<Dado> tirarDados() {
//        ArrayList<Dado> dadosGenerados = new ArrayList<>();
//
//        for (int i = 0; i < 6; i++) {
//            Dado dado = new Dado();
//            dadosGenerados.add(dado); // Agregar el dado al ArrayList
//        }
//
//        dadosLanzados.clear();
//        dadosLanzados.addAll(dadosGenerados);
//
//        return dadosGenerados;
//    }

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
//    public void reiniciarPuntuacionRonda() {
//        this.puntuacionRonda = 0;
//    }
//
//    public void agregarPuntosTotales() {
//        this.puntuacionTotal += puntuacionRonda;
//    }


}
