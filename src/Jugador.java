public class Jugador {
    private String nombre;
    private int puntuacionTotal;
    private int puntuacionRonda;
    private boolean estaActivo; //Para saber si es su turno
// Constructor de jugador
    public Jugador(String nombre) {
        this.nombre = nombre;
        this.puntuacionTotal = 0;
        this.puntuacionRonda = 0;
        this.estaActivo = false;
    }

    public void sumarPuntos(int puntos) {
        this.puntuacionTotal += puntos;
    }
    //Metodo necesario para la gestion de turnos
    public void iniciarTurno(){
        puntuacionRonda = 0;
        estaActivo = true;
    }
    //Metodo que suma mi puntuacion obtenida
    public void terminarTurno(boolean sumarPuntaje){
        if(sumarPuntaje){
            puntuacionTotal += puntuacionRonda;
        }
        puntuacionRonda = 0;
        estaActivo = false;
    }
    //getters y setters
    public int getPuntuacionTotal() {
        return puntuacionTotal;
    }

    public void setPuntuacionRonda(int puntuacionRonda) {
        this.puntuacionRonda = puntuacionRonda;
    }

    public String getNombre() {
        return nombre;
    }

}
