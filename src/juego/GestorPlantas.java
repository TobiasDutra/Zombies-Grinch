package juego;

import entorno.Entorno;

public class GestorPlantas {
    private PlantaAvatar[] avataresPlantas;
    private int contadorPlantas; // Movimos el contador aquí

    public GestorPlantas(PlantaAvatar[] avataresPlantas) {
        this.avataresPlantas = avataresPlantas;
        
        // El gestor ahora cuenta cuántas plantas iniciales (avatares del HUD) hay
        this.contadorPlantas = 0;
        for (PlantaAvatar planta : avataresPlantas) {
            if (planta != null) {
                this.contadorPlantas++;
            }
        }
    }

    /**
     * Dibuja todas las plantas en sus posiciones.
     */
    public void dibujarPlantas(Entorno entorno) {
        for (PlantaAvatar planta : avataresPlantas) {
            if (planta != null) {
                planta.dibujar(entorno);
            }
        }
    }

    /**
     * Mueve, dibuja y elimina las bolas de fuego que salen de la pantalla.
     */
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

    /**
     * Hace que todas las plantas en juego que puedan disparar, disparen.
     */
    public void dispararPlantasEnJuego() {
        for (PlantaAvatar planta : avataresPlantas) {
            if (planta != null && planta.estaEnJuego && planta.tipoPlanta.equals("RoseBlade")) {
                planta.disparar();
            }
        }
    }

    /**
     * Combina la lógica de selección y deselección con el clic.
     */
    public void manejarClickPresionado(int mx, int my) {
        boolean clicEnPlanta = false;

        // 1. Revisa si se clickeó una planta para seleccionarla
        for (PlantaAvatar planta : avataresPlantas) {
            if (planta != null && cursorDentro(planta, mx, my, 20)) {
                System.out.print("entre" + planta.imagen);
                
                // Deselecciona todas las demás
                deseleccionarTodas(); 
                planta.seleccionada = true;
                
                clicEnPlanta = true;
                break; // Ya encontramos la planta clickeada
            }
        }

        // 2. Si se hizo clic AFUERA de cualquier planta, deselecciona todas
        if (!clicEnPlanta) {
            deseleccionarTodas();
        }
    }

    /**
     * Intenta colocar una planta en el tablero al soltar el clic.
     */
    public void manejarClickSoltado(int mx, int my, Casilla[] casillasTablero) {
        for (int i = 0; i < this.avataresPlantas.length; i++) {
            if (avataresPlantas[i] != null && avataresPlantas[i].seleccionada && !avataresPlantas[i].estaEnJuego) {

                boolean sobreCasilla = false;
                for (Casilla casilla : casillasTablero) {
                    if (cursorDentroDeCasilla(casilla, mx, my) && !casilla.estaOcupada && !casilla.tieneRegalo) {
                        sobreCasilla = true;
                        avataresPlantas[i].x = casilla.x;
                        avataresPlantas[i].y = casilla.y;
                        casilla.estaOcupada = true;

                        // Lógica para crear un nuevo avatar en el HUD
                        if (this.contadorPlantas < this.avataresPlantas.length) {
                             this.avataresPlantas[this.contadorPlantas] = new PlantaAvatar(
                                avataresPlantas[i].tipoPlanta.equals("RoseBlade") ? "RoseBlade" : "WallNut", 
                                avataresPlantas[i].tipoPlanta.equals("RoseBlade") ? 60 : 180, 
                                60, 100, 100);
                            this.contadorPlantas++;
                        }

                        avataresPlantas[i].estaEnJuego = true;
                        avataresPlantas[i].casillaId = casilla.id;
                        break;
                    }
                }

                if (!sobreCasilla) {
                    // Vuelve al inicio del avatar
                    avataresPlantas[i].x = avataresPlantas[i].initialX;
                    avataresPlantas[i].y = avataresPlantas[i].initialY;
                }

                avataresPlantas[i].seleccionada = false;
                break; // Solo una planta puede ser soltada a la vez
            }
        }
    }

    /**
     * Mueve la planta seleccionada en el tablero usando las flechas.
     */
    public void manejarMovimientoConFlechas(Entorno entorno, Casilla[] casillasTablero) {
        for (int i = 0; i < this.avataresPlantas.length; i++) {
            
            // Buscar la única planta que está seleccionada
            if (avataresPlantas[i] != null && avataresPlantas[i].seleccionada) {

                // CASO 1: Es un avatar del HUD (no está en juego)
                if (!avataresPlantas[i].estaEnJuego) {
                     // Si se presiona cualquier flecha, simplemente se deselecciona
                     if (entorno.sePresiono(entorno.TECLA_ARRIBA) || entorno.sePresiono(entorno.TECLA_ABAJO) ||
                         entorno.sePresiono(entorno.TECLA_DERECHA) || entorno.sePresiono(entorno.TECLA_IZQUIERDA)) {
                         
                         avataresPlantas[i].seleccionada = false;
                     }
                     // Encontramos la planta seleccionada, así que salimos del bucle 'for'
                     break;
                }

                // CASO 2: Es una planta en el tablero (SÍ está en juego)

                // Tecla arriba
                if (entorno.sePresiono(entorno.TECLA_ARRIBA)) {
                    if (casillaDesdeFlechaArriba(avataresPlantas[i].casillaId, casillasTablero)) {
                        moverPlantaFLECHAS(i, -9, casillasTablero);
                        break; // ÉXITO: Rompe el 'for', la planta SIGUE seleccionada
                    }
                    avataresPlantas[i].seleccionada = false; // FALLO: Deselecciona
                    break; // Rompe el 'for' (ya se procesó la tecla)
                
                // Tecla abajo
                } else if (entorno.sePresiono(entorno.TECLA_ABAJO)) {
                    if (casillaDesdeFlechaAbajo(avataresPlantas[i].casillaId, casillasTablero)) {
                        moverPlantaFLECHAS(i, +9, casillasTablero);
                        break; // ÉXITO: Sigue seleccionada
                    }
                    avataresPlantas[i].seleccionada = false; // FALLO
                    break;
                
                // Tecla derecha
                } else if (entorno.sePresiono(entorno.TECLA_DERECHA)) {
                    if (casillaDesdeFlechaDerecha(avataresPlantas[i].casillaId, casillasTablero)) {
                        moverPlantaFLECHAS(i, +1, casillasTablero);
                        break; // ÉXITO
                    }
                    avataresPlantas[i].seleccionada = false; // FALLO
                    break;
                
                // Tecla izquierda
                } else if (entorno.sePresiono(entorno.TECLA_IZQUIERDA)) {
                    if (casillaDesdeFlechaIzquierda(avataresPlantas[i].casillaId, casillasTablero)) {
                        moverPlantaFLECHAS(i, -1, casillasTablero);
                        break; // ÉXITO
                    }
                    avataresPlantas[i].seleccionada = false; // FALLO
                    break;
                }
            }
        }
    }

    /**
     * Mueve la planta seleccionada (avatar) siguiendo el cursor.
     * (Este método estaba bien, pero lo incluyo para claridad)
     */
    public void manejarArrastre(int mx, int my) {
        for (PlantaAvatar planta : avataresPlantas) {
            // Esta lógica es correcta: solo se arrastran plantas no puestas en juego
            if (planta != null && planta.seleccionada && planta.estaDisponible && !planta.estaEnJuego) {
                planta.moverse(mx, my);
                break; // Solo una planta se puede arrastrar
            }
        }
    }
    /**
     * Actualiza el contador de recarga de todas las plantas.
     */
    public void actualizarRecargas() {
        for (PlantaAvatar planta : avataresPlantas) {
            if (planta != null) {
                planta.counterTime += 1;
                if (planta.counterTime > 200 && !planta.estaDisponible) {
                    planta.estaDisponible = true;
                }
            }
        }
    }

    // --- MÉTODOS PRIVADOS DE AYUDA (Movidos desde Juego.java) ---

    private void deseleccionarTodas() {
        for (PlantaAvatar planta : this.avataresPlantas) {
            if (planta != null) {
                planta.seleccionada = false;
            }
        }
    }

    private boolean cursorDentro(PlantaAvatar a, int mx, int my, int d) {
        return ((a.x - mx) * (a.x - mx) + (a.y - my) * (a.y - my)) < d * d;
    }

    private boolean cursorDentroDeCasilla(Casilla casilla, int mx, int my) {
        return (mx > casilla.x - casilla.ancho / 2 && mx < casilla.x + casilla.ancho / 2 &&
                my > casilla.y - casilla.alto / 2 && my < casilla.y + casilla.alto / 2);
    }

    private boolean casillaDesdeFlechaArriba(int casillaId, Casilla[] casillasTablero) {
        if (casillaId < 9) {
            return false;
        }
        Casilla nuevaCasilla = casillasTablero[casillaId - 9];
        return !nuevaCasilla.estaOcupada;
    }

    private boolean casillaDesdeFlechaAbajo(int casillaId, Casilla[] casillasTablero) {
        if (casillaId > 35) { // 44 - 9 = 35
            return false;
        }
        Casilla nuevaCasilla = casillasTablero[casillaId + 9];
        return !nuevaCasilla.estaOcupada;
    }

    private boolean casillaDesdeFlechaDerecha(int casillaId, Casilla[] casillasTablero) {
        if (casillaId == 8 || casillaId == 17 || casillaId == 26 || casillaId == 35 || casillaId == 44) {
            return false;
        }
        Casilla nuevaCasilla = casillasTablero[casillaId + 1];
        return !nuevaCasilla.estaOcupada;
    }

    private boolean casillaDesdeFlechaIzquierda(int casillaId, Casilla[] casillasTablero) {
        // IDs de la primera columna
        if (casillaId == 0 || casillaId == 9 || casillaId == 18 || casillaId == 27 || casillaId == 36) {
             return false;
        }
        Casilla nuevaCasilla = casillasTablero[casillaId - 1];
        return !nuevaCasilla.estaOcupada && !nuevaCasilla.tieneRegalo;
    }

    private void moverPlantaFLECHAS(int indice, int cantidadMovimiento, Casilla[] casillasTablero) {
        int idCasillaAnterior = this.avataresPlantas[indice].casillaId;
        int idCasillaNueva = idCasillaAnterior + cantidadMovimiento;

        Casilla nuevaCas = casillasTablero[idCasillaNueva];
        Casilla viejaCas = casillasTablero[idCasillaAnterior];

        // Mover planta a nueva casilla
        this.avataresPlantas[indice].x = nuevaCas.x;
        this.avataresPlantas[indice].y = nuevaCas.y;
        this.avataresPlantas[indice].casillaId = nuevaCas.id;
        
        // Actualizar estado de casillas
        nuevaCas.estaOcupada = true;
        viejaCas.estaOcupada = false;
    }
}