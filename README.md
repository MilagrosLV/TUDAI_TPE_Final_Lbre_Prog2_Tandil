# TUDAI\_TPE\_Final\_Lbre\_Prog2\_Tandil



### Crear un nuevo Estado de Celda

Para extender el juego con nuevas reglas, el sistema utiliza el Patrón State:

1. Crear una nueva clase que implemente la interfaz Estado. 
2. Se le da nuevo comportamiento.
3. Registrar el nuevo Estado en io/CragadorTablero.java en el método crearEstadoSegunCaracter(c).
4. (Opcional) Modificar Estados existentes para que "evolucionen" al nuevo.

