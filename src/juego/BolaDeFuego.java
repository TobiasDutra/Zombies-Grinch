package juego;

import java.awt.*;

import entorno.Entorno;
import entorno.Herramientas;

public class BolaDeFuego {
	public double x;
	public double y;
	public int ancho;
	public int alto;
	private double velocidad;

    public Image imagen;
	
	public BolaDeFuego(double x, double y, int ancho, int alto, double velocidad) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.velocidad = velocidad;

        this.imagen = Herramientas.cargarImagen("bolafuego.png");
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public int getAncho() {
		return ancho;
	}

	public int getAlto() {
		return alto;
	}
	
	public void dibujar(Entorno entorno){
		// entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, Color.RED);
		entorno.dibujarImagen(this.imagen, this.x, this.y, 0, 1);

	}
	
	public void mover(){
		this.x += this.velocidad;
	}
}
