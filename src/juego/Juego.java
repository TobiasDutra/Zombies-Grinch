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

    // Variables
    Zombie[] zombies;
    Planta planta;
    BolaDeFuego bolaDeFuego;
    Casilla[] casillasTablero;
	PlantaAvatar[] avataresPlantas;
    private int ZombiesEliminados = 0;
    private int ZombiesRestantes = 100;
    private int TiempoDeJuego = 0;
	private Image backgroundImage = Herramientas.cargarImagen("Frontyard.jpg");

    Juego()
    {
        // Inicializa el objeto entorno
        this.entorno = new Entorno(this, "La Invasión de los Zombies Grinch", 1260, 900);

		// Creamos Avatares de plantas
		this.avataresPlantas = new PlantaAvatar[2];
		for(int i = 0; i < this.avataresPlantas.length; i++){
            boolean isRoseBlade = i == 0;
            this.avataresPlantas[i] = new PlantaAvatar(20 + 120*i, 60, 50, 50, isRoseBlade ? "roseblade.jpg" : "wallnut.jpg");
        }

        // Creamos zombies
        this.zombies = new Zombie[5];
        for(int i = 0; i < this.zombies.length; i++){
            this.zombies[i] = new Zombie(750, 60 + 120*i, 60, 60, Math.random()+0.1);
        }

        // Creamos casillas
        this.casillasTablero = new Casilla[45];
        int contador = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 9; j++) {
                boolean imparFila = i%2 == 1;
                boolean imparColumna = j%2 == 1;
                boolean isRegalo = j == 0;
                // Casilla (x,y,ancho,alto,angulo,escala,ocupada,tieneRegalo,imagenFondo)
                this.casillasTablero[contador] = new Casilla(220 + 120*j, 270 + 120*i, 120, 120, false, isRegalo, "casilla"+(imparFila ? "clara" : "oscura")+(imparColumna ? "2" : "1")+".png");
                contador++;
            }
        }
        

        // Creamos plantas
        this.planta = new Planta(50, 300, 40, 60);

        // Iniciamos el juego
        this.entorno.iniciar();

    }

    public void tick()
    {
        // Procesamiento de un instante de tiempo
        this.planta.dibujar(entorno);

		//Tiempo
		int sec= entorno.tiempo()/1000;
		TiempoDeJuego= sec;

		//Dibujamos imagen de fondo (x,y,angulo,escala)
        this.entorno.dibujarImagen(backgroundImage, 840,530, 0, 1.25);

        // Dibujamos seccion superior (x, y, ancho, alto, angulo, color)
        this.entorno.dibujarRectangulo(0,0,3200,350,0,Color.LIGHT_GRAY);

		// Seteamos y escribimos textos
		Color miColor = new Color (255, 204, 0);
        this.entorno.cambiarFont("Constantia", 18, miColor,entorno.NEGRITA);
        this.entorno.escribirTexto("ELIMINADOS: " + ZombiesEliminados , 600 , 30);
        this.entorno.escribirTexto("RESTANTES: " + ZombiesRestantes, 600 , 60);
        this.entorno.escribirTexto("TIEMPO DE JUEGO: " + TiempoDeJuego + " S.", 600 , 90);

		// Dibujamos casillas
        for(int i = 0; i < this.casillasTablero.length; i++){
            this.casillasTablero[i].dibujar(this.entorno);
        }

		// Dibujamos avatares de plantas
        for(int i = 0; i < this.avataresPlantas.length; i++){
            this.avataresPlantas[i].dibujar(this.entorno);
        }

		// Dibujamos y movemos zombies
        for(int i = 0; i < this.zombies.length; i++){
            this.zombies[i].mover();
            this.zombies[i].dibujar(this.entorno);
        }

        if(this.entorno.sePresiono(this.entorno.TECLA_ESPACIO)){
            this.bolaDeFuego = this.planta.disparar();
        }

        if(this.bolaDeFuego != null) {
            this.bolaDeFuego.dibujar(entorno);
            this.bolaDeFuego.mover();
        }

    }


    @SuppressWarnings("unused")
    public static void main(String[] args)
    {
        Juego juego = new Juego();
    }

}
