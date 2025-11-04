package juego;

import entorno.Entorno;

public class GestorPlantas {
    private PlantaAvatar[] avataresPlantas;

    public GestorPlantas(PlantaAvatar[] avataresPlantas) {
        this.avataresPlantas = avataresPlantas;
    }

    public void dibujarPlantas(Entorno entorno) {
        for (PlantaAvatar planta : avataresPlantas) {
            if (planta != null) {
                planta.dibujar(entorno);
            }
        }
    }

    public void dibujarYActualizarBolasDeFuego(Entorno entorno) {
        for (PlantaAvatar planta : avataresPlantas) {
            if (planta != null) {
                for (int j = 0; j < planta.bolasDeFuego.length; j++) {
                    if (planta.bolasDeFuego[j] != null) {
                        planta.bolasDeFuego[j].dibujar(entorno);
                        planta.bolasDeFuego[j].mover();

                        if (planta.bolasDeFuego[j].getX() > entorno.ancho()) {
                            planta.bolasDeFuego[j] = null;
                        }
                    }
                }
            }
        }
    }
}
