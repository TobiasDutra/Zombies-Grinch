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
    
    private int ContadorPlantas = 0;
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
		for(int i = 0; i < 2; i++){
            boolean isRoseBlade = i == 0;
            this.avataresPlantas[i] = new PlantaAvatar(isRoseBlade ? "RoseBlade" : "WallNut", 60 + 120*i, 60, 100, 100);
            ContadorPlantas++;
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

        // Detectamos los click izquierdo y deseleccionamos todo
        if (entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO)) {
            boolean clicEnPlanta = false;

            for (PlantaAvatar planta : avataresPlantas) {
                if (planta != null && cursorDentro(planta, entorno.mouseX(), entorno.mouseY(), 20)) {
                    clicEnPlanta = true;
                    break;
                }
            }

            // Si no clickeamos ninguna planta → deseleccionar TODAS
            if (!clicEnPlanta) {
                for (PlantaAvatar planta : avataresPlantas) {
                    if (planta != null) {
                        planta.seleccionada = false;
                    }
                }
            }
        }

        // MOVIMIENTO
        for (int i = 0; i < this.avataresPlantas.length; i++) {
            if (avataresPlantas[i] != null) {
                if (entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO) && this.cursorDentro(avataresPlantas[i], entorno.mouseX(), entorno.mouseY(), 20)) {
                    System.out.print("entre" + avataresPlantas[i].imagen);

                    for (PlantaAvatar planta : avataresPlantas) {
                        if (planta != null) {
                        planta.seleccionada = false;
                    }
                }

                    avataresPlantas[i].seleccionada = true;
                }
            }
        }

        //Primera colocación
        for (int i = 0; i < this.avataresPlantas.length; i++) {
            if (avataresPlantas[i] != null) {
                if (entorno.seLevantoBoton(entorno.BOTON_IZQUIERDO) && avataresPlantas[i].seleccionada && !avataresPlantas[i].estaEnJuego) {

                    boolean sobreCasilla = false;
                    for (Casilla casilla : casillasTablero) {

                        if (cursorDentroDeCasilla(casilla, entorno.mouseX(), entorno.mouseY()) && !casilla.estaOcupada && !casilla.tieneRegalo) {
                            sobreCasilla = true;
                            avataresPlantas[i].x = casilla.x;
                            avataresPlantas[i].y = casilla.y;
                            casilla.estaOcupada = true;

                            this.avataresPlantas[ContadorPlantas] = new PlantaAvatar(avataresPlantas[i].tipoPlanta == "RoseBlade" ? "RoseBlade" : "WallNut", avataresPlantas[i].tipoPlanta == "RoseBlade" ? 60 : 180, 60, 100, 100);
                            ContadorPlantas++;

                            avataresPlantas[i].estaEnJuego = true;
                            avataresPlantas[i].casillaId = casilla.id;
                            break;
                        }
                    }

                    if (!sobreCasilla) {
                        // Vuelve al inicio del avatar
                        avataresPlantas[i].x = avataresPlantas[i].initialX;
                        avataresPlantas[i].y = avataresPlantas[i].initialY;
                    }

                    avataresPlantas[i].seleccionada = false;
                }
            }
        }

        // FLECHAS MOVIMIENTO
        for (int i = 0; i < this.avataresPlantas.length; i++) {
            if (avataresPlantas[i] != null) {
                // Tecla arriba
                if (entorno.sePresiono(entorno.TECLA_ARRIBA) && avataresPlantas[i].seleccionada) {
                    if (casillaDesdeFlechaArriba(avataresPlantas[i].casillaId)) {
                        moverPlantaFLECHAS(i, -9);
                        break;
                    }

                    avataresPlantas[i].seleccionada = false;
                // Tecla abajo
                } else if (entorno.sePresiono(entorno.TECLA_ABAJO) && avataresPlantas[i].seleccionada) {
                    if (casillaDesdeFlechaAbajo(avataresPlantas[i].casillaId)) {
                        moverPlantaFLECHAS(i, +9);
                        break;
                    }

                    avataresPlantas[i].seleccionada = false;
                // Tecla derecha
                } else if (entorno.sePresiono(entorno.TECLA_DERECHA) && avataresPlantas[i].seleccionada) {
                    if (casillaDesdeFlechaDerecha(avataresPlantas[i].casillaId)) {
                        moverPlantaFLECHAS(i, +1);
                        break;
                    }

                    avataresPlantas[i].seleccionada = false;
                // Tecla izquierda
                } else if (entorno.sePresiono(entorno.TECLA_IZQUIERDA) && avataresPlantas[i].seleccionada) {
                    if (casillaDesdeFlechaIzquierda(avataresPlantas[i].casillaId)) {
                        moverPlantaFLECHAS(i, -1);
                        break;
                    }

                    avataresPlantas[i].seleccionada = false;
                }
            }
        }

        for (int i = 0; i < this.avataresPlantas.length; i++) {
            if (avataresPlantas[i] != null) {
                if (entorno.estaPresionado(entorno.BOTON_IZQUIERDO) && avataresPlantas[i].seleccionada && avataresPlantas[i].estaDisponible && !avataresPlantas[i].estaEnJuego) {
                    avataresPlantas[i].moverse(entorno.mouseX(), entorno.mouseY());
                }
            }
        }

        // Recarga para disponibilizar planta
        for (int i = 0; i < this.avataresPlantas.length; i++) {
            if (avataresPlantas[i] != null) {
                avataresPlantas[i].counterTime += 1;
                if (avataresPlantas[i].counterTime > 200 && !avataresPlantas[i].estaDisponible) {
                    avataresPlantas[i].estaDisponible = true;
                }
            }
        }

        for (int i = 0; i < this.avataresPlantas.length; i++) {
            if (avataresPlantas[i] != null) {
                for (int j = 0; j < this.avataresPlantas[i].bolasDeFuego.length; j++) {
                    if(this.avataresPlantas[i].bolasDeFuego[j] != null){
                        // Dibujamos y movemos las bolas de fuego
                        this.avataresPlantas[i].bolasDeFuego[j].dibujar(entorno);
                        this.avataresPlantas[i].bolasDeFuego[j].mover();

                        // Si una bola de fuego esta fuera de la pantalla, la establecemos en null
                        if (this.avataresPlantas[i].bolasDeFuego[j].getX() > entorno.ancho()) {
                            this.avataresPlantas[i].bolasDeFuego[j] = null;
                        }
                    }
                }
            }
        }

        // Dibujamos avatares de plantas
        gestorPlantas.dibujarPlantas(entorno);

        // Dibujamos avatares de zombies
        for (ZombieAvatar zombie : avataresZombies) {
            zombie.dibujar(entorno);
        }

        // Dibujamos bolas de fuego
        if (entorno.sePresiono(entorno.TECLA_ESPACIO)) {
            for (PlantaAvatar planta : avataresPlantas) {
                if (planta != null && planta.estaEnJuego && planta.tipoPlanta.equals("RoseBlade")) {
                    planta.disparar();
                }
            }
        }

    }

    @SuppressWarnings("unused")
    public static void main(String[] args)
    {
        Juego juego = new Juego();
    }


    // Funciones extras
    public boolean cursorDentro(PlantaAvatar a, int mx,int my, int d) {
		return( (a.x-mx)* (a.x-mx)+(a.y-my)*(a.y-my))<d*d;
	}

    public boolean cursorDentroDeCasilla(Casilla casilla, int mx, int my) {
        return (mx > casilla.x - casilla.ancho/2 && mx < casilla.x + casilla.ancho/2 && my > casilla.y - casilla.alto/2 && my < casilla.y + casilla.alto/2);
    }

    public boolean casillaDesdeFlechaArriba(int casillaId) {
        if(casillaId < 9){
            return false;
        }
        Casilla nuevaCasilla = this.casillasTablero[casillaId-9];
        if(!nuevaCasilla.estaOcupada){
            return true;
        }else{
            return false;
        }
    }

    public boolean casillaDesdeFlechaAbajo(int casillaId) {
        if(casillaId > 35){
            return false;
        }
        Casilla nuevaCasilla = this.casillasTablero[casillaId+9];
        if(!nuevaCasilla.estaOcupada){
            return true;
        }else{
            return false;
        }
    }

    public boolean casillaDesdeFlechaDerecha(int casillaId) {
        if(casillaId == 8 || casillaId == 17 || casillaId == 26 || casillaId == 35 | casillaId == 44){
            return false;
        }
        Casilla nuevaCasilla = this.casillasTablero[casillaId+1];
        if(!nuevaCasilla.estaOcupada){
            return true;
        }else{
            return false;
        }
    }

    public boolean casillaDesdeFlechaIzquierda(int casillaId) {
        Casilla nuevaCasilla = this.casillasTablero[casillaId-1];
        if(!nuevaCasilla.estaOcupada && !nuevaCasilla.tieneRegalo){
            return true;
        }else{
            return false;
        }
    }

    public void moverPlantaFLECHAS(int indice, int cantidadMovimiento){
        Casilla nuevaCas = this.casillasTablero[(this.avataresPlantas[indice].casillaId)+cantidadMovimiento];

        this.avataresPlantas[indice].x = nuevaCas.x;
        this.avataresPlantas[indice].y = nuevaCas.y;
        nuevaCas.estaOcupada = true;

        // Busco la casilla anterior y la establezco en desocupada
        for (Casilla casillaAnterior : this.casillasTablero) {
            if(this.avataresPlantas[indice].casillaId == casillaAnterior.id){
                casillaAnterior.estaOcupada = false;
            }
        }

        this.avataresPlantas[indice].casillaId = nuevaCas.id;
    }

    private void actualizarTiempoDeJuego() {
        tiempoDeJuego = entorno.tiempo() / 1000;
    }

}
