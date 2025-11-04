package juego;


import java.awt.*;
import java.util.Arrays;

import entorno.Entorno;
import entorno.InterfaceJuego;
import entorno.Herramientas;

public class Juego extends InterfaceJuego
{
    // El objeto Entorno que controla el tiempo y otros
    private Entorno entorno;
    private HUD hud;
    private Tablero tablero;
    private GestorPlantas gestorPlantas;

    // Variables
    Zombie[] zombies;
    BolaDeFuego bolaDeFuego;
    Casilla[] casillasTablero;
	PlantaAvatar[] avataresPlantas;
	ZombieAvatar[] avataresZombies;
    GestorZombies gestorZombies;
    
    private int zombiesEliminados = 0;
    private int zombiesRestantes = 100;
    private int tiempoDeJuego = 0;


    Juego()
    {
        // Inicializa el objeto entorno
        this.entorno = new Entorno(this, "La Invasión de los Zombies Grinch", 1260, 900);

        this.hud = new HUD(entorno);
        this.tablero = new Tablero(5, 9, 320, 310, 150);
        //this.avataresPlantas = new PlantaAvatar[100];

		// Creamos Avatares de plantas
		this.avataresPlantas = new PlantaAvatar[100];
		int contadorPlantasInicial = 0;
		for(int i = 0; i < 2; i++){
            boolean isRoseBlade = i == 0;
            this.avataresPlantas[i] = new PlantaAvatar(isRoseBlade ? "RoseBlade" : "WallNut", 60 + 120*i, 60, 100, 100);
            contadorPlantasInicial++;
        }
		this.gestorPlantas = new GestorPlantas(this.avataresPlantas);

        // Creamos Avatares de zombies
		this.avataresZombies = new ZombieAvatar[1];
		for(int i = 0; i < 1; i++){
            this.avataresZombies[i] = new ZombieAvatar(1200, 60, 100, 100);
        }

        // Creamos zombies
        gestorZombies = new GestorZombies(50);

        // Creamos casillas
        this.casillasTablero = new Casilla[45];
        int contador = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 9; j++) {
                boolean imparFila = i%2 == 1;
                boolean imparColumna = j%2 == 1;
                boolean isRegalo = j == 0;
                this.casillasTablero[contador] = new Casilla(contador, 320 + 100*j, 310 + 100*i, 150, 150, false, isRegalo, "casilla"+(imparFila ? "clara" : "oscura")+(imparColumna ? "2" : "1")+".png");
                contador++;
            }
        }

        // Iniciamos el juego
        this.entorno.iniciar();

    }

    public void tick() {

    	actualizarTiempoDeJuego();
    	
        hud.dibujarFondo();
        hud.dibujar(zombiesEliminados, zombiesRestantes, tiempoDeJuego);


        tablero.dibujar(entorno);

        // Dibujamos y movemos zombies
        gestorZombies.actualizar(entorno);

        // Dibujamos avatares de plantas
        gestorPlantas.dibujarPlantas(entorno);

        // Dibujamos avatares de zombies
        for (ZombieAvatar zombie : avataresZombies) {
            zombie.dibujar(entorno);
        }

        // Dibujamos bolas de fuego
        if (entorno.sePresiono(entorno.TECLA_ESPACIO)) {
            gestorPlantas.dispararPlantasEnJuego();
        }

        // Detectamos los click izquierdo y deseleccionamos todo
        if (entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO)) {
            gestorPlantas.manejarClickPresionado(entorno.mouseX(), entorno.mouseY());
        }

        //Primera colocación
        if (entorno.seLevantoBoton(entorno.BOTON_IZQUIERDO)) {
            gestorPlantas.manejarClickSoltado(entorno.mouseX(), entorno.mouseY(), this.casillasTablero);
        }

        gestorPlantas.manejarMovimientoConFlechas(entorno, this.casillasTablero);

        // Arrastre del avatar
        if (entorno.estaPresionado(entorno.BOTON_IZQUIERDO)) {
             gestorPlantas.manejarArrastre(entorno.mouseX(), entorno.mouseY());
        }

        // Cooldown
        gestorPlantas.actualizarRecargas();
        
        gestorPlantas.dibujarYActualizarBolasDeFuego(entorno);
    }

    @SuppressWarnings("unused")
    public static void main(String[] args)
    {
        Juego juego = new Juego();
    }

    private void actualizarTiempoDeJuego() {
        tiempoDeJuego = entorno.tiempo() / 1000;
    }

}
