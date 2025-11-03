package juego;

import java.awt.Color;
import java.util.Random;

import entorno.Entorno;

public class Zombie {
	
	private double x;
	private double y;
	private int ancho;
	private int alto;
	private double velocidad;

	private boolean apareceDesdeArriba;
	private boolean descendiendo = false;
	private final int Y_OBJETIVO;
	
	public Zombie(double x, int y, int ancho, int alto, double velocidad) {
	    this.x = x;
	    this.y = y;
	    this.ancho = ancho;
	    this.alto = alto;
	    this.velocidad = velocidad;
	    this.Y_OBJETIVO = 60 + new Random().nextInt(5) * 120; // eje y aleatorio
	}
	
	public void setApareceDesdeArriba(boolean desdeArriba) {
	    this.apareceDesdeArriba = desdeArriba;
	    this.descendiendo = desdeArriba;
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
		entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, Color.YELLOW);
	}
	
	public void mover() {
	    if (apareceDesdeArriba && descendiendo) {
	        this.y += velocidad;
	        if (this.y >= Y_OBJETIVO) {
	            this.y = Y_OBJETIVO;
	            this.descendiendo = false;
	        }
	    } else {
	        this.x -= velocidad;
	    }
	}
	
}
