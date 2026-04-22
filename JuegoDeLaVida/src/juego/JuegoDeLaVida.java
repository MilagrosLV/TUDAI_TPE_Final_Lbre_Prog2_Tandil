package juego;
//import vista.VistaJuego;

import java.io.FileNotFoundException;

import io.CargadorTablero;
import modelo.Tablero;

public class JuegoDeLaVida {

	public static void main(String[] args) {
        try {
            // 1. Cargar el tablero
            Tablero tablero = CargadorTablero.cargarDesdeArchivo("ejemplos/semilla1.txt");;
			
            
            int generacion = 0;
            boolean activo = true;

            // 2. Bucle de ejecución
            while (activo) {
                System.out.println("\n--- Generación: " + generacion + " ---");
                tablero.mostrar();

                activo = tablero.avanzarGeneracion();
                generacion++;

                if (!activo) {
                    System.out.println("La población se ha estabilizado.");
                }

                Thread.sleep(500); // Intervalo configurable (0.5 seg)
            }

        } catch (FileNotFoundException e) {
            System.err.println("Error: No se encontró el archivo de semilla.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("La ejecución fue interrumpida.");
        } catch (Exception e) {
            System.err.println("Error crítico: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
