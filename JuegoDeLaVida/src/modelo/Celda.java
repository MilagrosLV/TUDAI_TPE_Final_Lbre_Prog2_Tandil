package modelo;

public class Celda {
	//La Celda va a com¿nocer su posición y su Estado actuasl, también pregunta su Estado Siguiente.
	private Estado estadoActual, estadoSig;
	
	public Celda (Estado estadoActual) { //ESTADO INICIAL
		this.estadoActual=estadoActual;
	}
	
	//getter
	public Estado getEstadoActual() {
		return estadoActual;
	}
	public Estado getEstadoSig() {
		return estadoSig;
	}

	//METODOS Estado
	public void calcularSig(int vecinosVivos) {
		//Se delega el comportamiento a Estado, para saber cuál será su siguiente estado.
		this.estadoSig = getEstadoActual().SigEstado(vecinosVivos);
	}
	public boolean isViva() {
		//Buscar si está viva la Celda
		return this.estadoActual.isViva();
	}
	
	public void evolucionar() {
		//El cambio para la siguiente generación
		this.estadoActual = getEstadoSig();
	}
	
	public char mostrar() {
		return getEstadoActual().getRepresentacion();
	}

	
	
	

	
	
}
