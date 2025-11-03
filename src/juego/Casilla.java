package juego;

import java.awt.*;

import entorno.Entorno;
import entorno.Herramientas;

public class Casilla {
	public int id;
	public double x;
	public int y;
	public int ancho;
	public int alto;
    private double angulo;
    private double escala;
	public boolean estaOcupada;
	public boolean tieneRegalo;
    private Image imagenFondo;
	
	public Casilla(int id, int x, int y, int ancho, int alto, boolean estaOcupada, boolean tieneRegalo, String imagenFondo) {
		this.id = id;
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
        // entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, this.tieneRegalo ? Color.PINK : Color.PINK);
        entorno.dibujarImagen(this.imagenFondo, this.x, this.y, this.angulo, this.escala);

		
		if (this.tieneRegalo) {
			// int x, int y, int ancho, int alto, int hp, String imagenFondo
			Regalo regalo = new Regalo(this.x, this.y, 40, 40, 1, "regalo.png");
			regalo.dibujar(entorno);
		}

	}
}