package modelo;

public class EstadoMuerto implements Estado {

	/** Número exacto de vecinos necesario para reproducirse */
	private final int VECINOS_PARA_REPRODUCIR = 3;

	@Override
	public boolean isViva() {
		return false;
	}

	@Override
	public char getRepresentacion() {
		//Regla: se simboliza con un punto, porque se puso de ejemplo en la consigna
		return '.';
	}

	@Override
	public Estado SigEstado(int vecinosVivos) {
		// Regla: reproducción - nace con exactamente 3 vecinos vivos
		if(vecinosVivos == VECINOS_PARA_REPRODUCIR) {
			return new EstadoVivo();
		}
		// En cualquier otro caso, permanece muerta
		return this;
	}
}


