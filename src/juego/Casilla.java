package juego;

import java.awt.*;

import entorno.Entorno;
import entorno.Herramientas;

public class Casilla {
	private double x;
	private int y;
	private int ancho;
	private int alto;
    private double angulo;
    private double escala;
	private boolean estaOcupada;
	private boolean tieneRegalo;
    private Image imagenFondo;
	
	public Casilla(int x, int y, int ancho, int alto, boolean estaOcupada, boolean tieneRegalo, String imagenFondo) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.angulo = 0;
		this.escala = 1.2;
		this.estaOcupada = false;
		this.tieneRegalo = tieneRegalo;
        this.imagenFondo = Herramientas.cargarImagen(imagenFondo);
	}

	public boolean getEstaOcupada() {
		return this.estaOcupada;
	}

	public void dibujar(Entorno entorno){
		// entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, this.tieneRegalo ? Color.PINK : this.colorFondo);
		entorno.dibujarImagen(this.imagenFondo, this.x, this.y, this.angulo, this.escala);
	}
	

}
