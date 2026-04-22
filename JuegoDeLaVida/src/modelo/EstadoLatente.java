package modelo;

public class EstadoLatente implements Estado {
	private final int NRO_VECINO=1;

	@Override
	public boolean isViva() {
		// Muerta
		return false;
	}

	@Override
	public char getRepresentacion() {
		//Se representa con X mayuscula, porque se puso de ejemplo en la consigna
		return 'X';
	}

	@Override
	public Estado SigEstado(int vecinosVivos) {
		//Regla: con exactamente 1 vecino vivo se convierte en una celda "Viva" en la siguiente generación.
		if(vecinosVivos == NRO_VECINO) {
			return new EstadoVivo();
		}
		
		return this;
	}

}
