# TPE Final Libre - Programación 2 - TUDAI Tandil

## índice

1. \[¿Cómo ejecutar el programa?] (#¿Cómo ejecutar el programa?)





## ¿Cómo ejecutar el programa?

## Diseño del Sistema
El diseño se basa en el Patrón de Diseño State (Estado) para cumplir con los principios SOLID, específicamente el principio de Abierto/Cerrado (Open/Closed).

## Clases Principales y Responsabilidades
1. Estado (Interface),Define el contrato para todos los estados posibles. Contiene la lógica de transición (determinarSiguienteEstado).
2. Celda,El contexto del sistema. Mantiene una referencia a un objeto Estado y delega en él toda la lógica de comportamiento.
3. Tablero,"Orquestador de la simulación. Gestiona la matriz de celdas, cuenta vecinos vivos y coordina la actualización sincronizada de la grilla."
4. CargadorTablero,Clase de utilidad encargada de la persistencia. Lee archivos de texto y traduce caracteres a instancias de estados específicos.
5. VistaJuego (CLI): "Interfaz de línea de comandos. Gestiona la interacción con el usuario, capturando entradas y mostrando la evolución de la grilla."

### Crear un nuevo Estado de Celda

Para extender el juego con nuevas reglas, el sistema utiliza el Patrón State:

1. Crear una nueva clase que implemente la interfaz Estado.
2. Se le da nuevo comportamiento.
3. Registrar el nuevo Estado en io/CragadorTablero.java en el método crearEstadoSegunCaracter(c).
4. (Opcional) Modificar Estados existentes para que "evolucionen" al nuevo.

