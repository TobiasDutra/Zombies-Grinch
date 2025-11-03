package juego;

import java.util.*;
import entorno.Entorno;
import entorno.Herramientas;

public class GestorZombies {
    private Zombie[] zombies;
    private int maxZombies;
    private int contadorTiempo;
    private int tiempoProximaAparicion;
    private int cantidadActual;

    public GestorZombies(int maxZombies) {
    	this.zombies = new Zombie[maxZombies];
        this.maxZombies = maxZombies;
        this.contadorTiempo = 0;
        this.tiempoProximaAparicion = generarTiempoRandom();
        this.cantidadActual = 0;
    }

    public void actualizar(Entorno entorno) {
        
        contadorTiempo++;

        if (cantidadActual < maxZombies && contadorTiempo >= tiempoProximaAparicion) {
            generarZombie();
            contadorTiempo = 0;
            tiempoProximaAparicion = generarTiempoRandom();
        }

        for (int i = 0; i < cantidadActual; i++) {
            Zombie z = zombies[i];
            if (z != null) {
                z.mover();
                z.dibujar(entorno);
            }
        }
        System.out.println("Cantidad de zombies: " + cantidadActual);
    }

    private int generarTiempoRandom() {
        return 100 + new Random().nextInt(200);
    }

    private void generarZombie() {
        Random random = new Random();

        boolean apareceDesdeArriba = random.nextBoolean();//50%
        double x;
        int y;
        int ancho = 60;
        int alto = 60;
        double velocidad = 1 + random.nextDouble() * 1.5;

        if (apareceDesdeArriba) {
            int desplazamientoX = (random.nextBoolean()) ? 100 : 200;//pixeles
            x = 1200 - desplazamientoX;
            y = 50;
        } else {
            x = 1200;
            y = 60 + random.nextInt(5) * 120;
        }

        Zombie z = new Zombie(x, y, ancho, alto, velocidad);
        z.setApareceDesdeArriba(apareceDesdeArriba);

        zombies[cantidadActual] = z;
        cantidadActual++;
    }

    public Zombie[] getZombies() {
        return zombies;
    }
}
