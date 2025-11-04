package juego;

import java.awt.*;
import entorno.Entorno;
import entorno.InterfaceJuego;
import entorno.Herramientas;

class HUD {
    private Entorno entorno;
    private Color color = new Color(255, 150, 46);
	private Image backgroundImage = Herramientas.cargarImagen("Frontyard.jpg");

    public HUD(Entorno entorno) {
        this.entorno = entorno;
    }

    public void dibujarFondo() {
        entorno.dibujarImagen(backgroundImage, 840, 530, 0, 1.25);
        entorno.dibujarRectangulo(0, 0, 3200, 350, 0, new Color(255, 150, 46));
    }

    public void dibujar(int eliminados, int restantes, int tiempo) {
        entorno.cambiarFont("Constantia", 18, color, entorno.NEGRITA);
        entorno.escribirTexto("ELIMINADOS: " + eliminados, 600, 30);
        entorno.escribirTexto("RESTANTES: " + restantes, 600, 60);
        entorno.escribirTexto("TIEMPO: " + tiempo + " s", 600, 90);
    }
}
