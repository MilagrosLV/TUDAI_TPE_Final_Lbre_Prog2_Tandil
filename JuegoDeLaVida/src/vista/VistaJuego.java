package vista;
import java.util.Scanner;

import io.CargadorTablero;
import modelo.Celda;
import modelo.Estado;
import modelo.EstadoMuerto;
import modelo.EstadoVivo;
import modelo.Tablero;


public class VistaJuego {

	//leer archivo si se carga uno, 
	//crear tablero
	
	private Tablero tablero;
	private Scanner scanner;
	
	public VistaJuego() {
		this.scanner = new Scanner(System.in);
	}
	
	//Interfaz
	//Solicita al usuario elegir la modalidad de carga del tablero
	//Luego se procede
	public void iniciar() {
		System.out.println("=== BIENVENIDO AL JUEGO DE LA VIDA ===");
		System.out.println("1. Cargar estado inicial desde archivo");
        System.out.println("2. Generar tablero aleatorio");
        System.out.print("Seleccione una opción: ");
        
        int opcion = leerEntero();
        
        try {
            if (opcion == 1) {
                configurarArchivo();
            } else {
                configurarManual();
            }
            
            if (tablero != null) {
                bucleDeEjecucion();
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
	}
	
	
	private void configurarArchivo() throws Exception {
        System.out.print("Ingrese la ruta del archivo (ej: ejemplos/semilla1.txt): ");
        String ruta = scanner.next();
        this.tablero = CargadorTablero.cargarDesdeArchivo(ruta);
    }
	
	private void configurarManual() {
        System.out.print("Ingrese filas: ");
        int f = leerEntero();
        System.out.print("Ingrese columnas: ");
        int c = leerEntero();

        this.tablero = new Tablero(f, c);
        
        // Poblado aleatorio
        for (int i = 0; i < f; i++) {
            for (int j = 0; j < c; j++) {
            	// 30% vivos, 70% muertos
            	Estado inicial; 
            	if (Math.random() > 0.7) {
            	    inicial = new EstadoVivo();
            	} else {
            	    inicial = new EstadoMuerto();
            	}                
            	Celda cInicial = new Celda(inicial);
                tablero.setCelda(i, j, cInicial);
            }
        }
    }
	
	private void bucleDeEjecucion() throws InterruptedException {
        System.out.print("¿Cuántas generaciones ejecutar? (0 para indefinido): ");
        int maxGen = leerEntero();
        System.out.print("Intervalo entre pasos (ms): ");
        int delay = leerEntero();

        int generacion = 0;
        boolean activo = true;

        while (activo && (maxGen == 0 || generacion < maxGen)) {
            System.out.println("\n--- Generación " + generacion + " ---");
            tablero.mostrar();
            
            activo = tablero.avanzarGeneracion();
            generacion++;
            
            if (!activo) {
                System.out.println("\n[Simulación finalizada: El tablero se ha estabilizado]");
                break;
            }
            
            Thread.sleep(delay);
        }
    }

    private int leerEntero() {
        while (!scanner.hasNextInt()) {
            System.out.print("Por favor, ingrese un número válido: ");
            scanner.next();
        }
        return scanner.nextInt();
    }
	

}
