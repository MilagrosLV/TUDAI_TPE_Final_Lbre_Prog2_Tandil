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
		// Regla: Reproducción - Una celda Muerta con exactamente 3 vecinos vivos se convierte en viva
		if (vecinosVivos == 3) {
			return new EstadoVivo();
		}
		// En cualquier otro caso, la celda muerta permanece muerta
		return this;
	}

}
