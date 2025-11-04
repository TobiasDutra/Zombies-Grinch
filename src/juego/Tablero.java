package juego;

import java.awt.*;
import entorno.Entorno;
import entorno.InterfaceJuego;
import entorno.Herramientas;

class Tablero {
    private final Casilla[] casillas;
    private final int filas, columnas;

    public Tablero(int filas, int columnas, int xInicio, int yInicio, int tamaño) {
        this.filas = filas;
        this.columnas = columnas;
        this.casillas = new Casilla[filas * columnas];
        inicializarCasillas(xInicio, yInicio, tamaño);
    }

    private void inicializarCasillas(int xInicio, int yInicio, int tamaño) {
        int contador = 0;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                boolean esRegalo = j == 0;
                casillas[contador++] = new Casilla(
                    contador, 
                    xInicio + 100 * j, 
                    yInicio + 100 * i, 
                    tamaño, tamaño, 
                    false, esRegalo,
                    "casilla" + (i % 2 == 1 ? "clara" : "oscura") + (j % 2 == 1 ? "2" : "1") + ".png"
                );
            }
        }
    }

    public void dibujar(Entorno entorno) {
        for (Casilla c : casillas) c.dibujar(entorno);
    }

    public Casilla getCasilla(int id) {
        return casillas[id];
    }
}
