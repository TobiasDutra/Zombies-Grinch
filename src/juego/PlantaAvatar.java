package juego;

import java.awt.*;

import entorno.Entorno;
import entorno.Herramientas;

public class PlantaAvatar {
    public int casillaId;
    public String tipoPlanta;
    public double x;
    public double y;
    public double initialY;
    public double initialX;

	private int ancho;
	private int alto;
    private double angulo;
    private double escala;

	public int counterTime;
    public boolean estaDisponible;
    public boolean seleccionada;
    public boolean estaEnJuego;
	
    public Image imagen;
    public Image imagenDesactivada;
    private Image imagenFondo;

	public BolaDeFuego[] bolasDeFuego;
	public int contadorBolasDeFuego;

    public int counterLife;

	public PlantaAvatar(String tipoPlanta, int x, int y, int ancho, int alto) {
		this.casillaId = -1;
		this.tipoPlanta = tipoPlanta;
		
		this.x = x;
		this.y = y;
		this.initialX = x;
		this.initialY = y;

		this.ancho = ancho;
		this.alto = alto;
		this.angulo = 0;
		this.escala = 1;

		this.counterTime = 0;
		this.estaDisponible = false;
        this.seleccionada=false;
        this.estaEnJuego=false;
		
        this.imagen = Herramientas.cargarImagen(tipoPlanta == "RoseBlade" ? "roseblade.jpg" : "wallnut.jpg");
        this.imagenDesactivada = Herramientas.cargarImagen(tipoPlanta == "RoseBlade" ? "rosebladeDesactivada.jpg" : "wallnutDesactivada.jpg");
        this.imagenFondo = Herramientas.cargarImagen("baseavatar.jpg");

		this.bolasDeFuego = new BolaDeFuego[20];
		this.contadorBolasDeFuego = 0;

		this.counterLife = tipoPlanta == "RoseBlade" ? 50 : 100;
	}


	public boolean getEstaDisponible() {
		return this.estaDisponible;
	}

	public void dibujar(Entorno entorno){
		if(this.estaDisponible && this.seleccionada) {
			// Imagen con color y zoom
            entorno.dibujarImagen(this.imagen, this.x, this.y, this.angulo, this.escala*1.1);
        }else if(this.estaDisponible){
			// Imagen con color
			entorno.dibujarImagen(this.imagen, this.x, this.y, this.angulo, this.escala);
		}else{
			//Imagen en blanco y negro
			entorno.dibujarImagen(this.imagenDesactivada, this.x, this.y, this.angulo, this.escala);
		}

	}

    public void moverse(int x, int y) {
        this.x = x;
        this.y = y;
    }

	public void disparar(){
		int randomOffset = (int) (Math.random() * 10) + 1;
    	double nuevaY = this.y - 10 + randomOffset;

        this.bolasDeFuego[contadorBolasDeFuego] = new BolaDeFuego(this.x, nuevaY, 15, 15, 3);
		contadorBolasDeFuego++;
		if(contadorBolasDeFuego  == 19){
			contadorBolasDeFuego = 0;
		}
	}

}
