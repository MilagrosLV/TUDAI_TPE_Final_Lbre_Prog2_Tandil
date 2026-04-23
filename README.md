# TPE Final Libre - Programación 2 - TUDAI Tandil

## índice

1. \[¿Cómo ejecutar el programa?] (#¿Cómo ejecutar el programa?)



### Crear un nuevo Estado de Celda

Para extender el juego con nuevas reglas, el sistema utiliza el Patrón State:

1. Crear una nueva clase que implemente la interfaz Estado.
2. Se le da nuevo comportamiento.
3. Registrar el nuevo Estado en io/CragadorTablero.java en el método crearEstadoSegunCaracter(c).
4. (Opcional) Modificar Estados existentes para que "evolucionen" al nuevo.

