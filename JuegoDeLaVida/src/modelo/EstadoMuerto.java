package modelo;

public class EstadoMuerto implements Estado {

	@Override
	public boolean isViva() {
		// Muerta = false
		return false;
	}

	@Override
	public char getRepresentacion() {
		//Regla: se simboliza con un punto, porque se puso de ejemplo en la consigna
		return '.';
	}

	@Override
	public Estado SigEstado(int vecinosVivos) {
		//Regla: Sobrepoblación, con más de 3 vecinos vivos, muere.
		return this;
	}

}
