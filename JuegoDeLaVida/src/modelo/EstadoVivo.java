package modelo;

public class EstadoVivo implements Estado {
	private final int NRO_DOS=2, NRO_TRES=3;
	private final double PROB_ENFER= 0.25; //25%

	@Override
	public boolean isViva() {
		//Celda VIVA
		return true;
	}

	@Override
	public char getRepresentacion() {
		//Se representa con O mayuscula
		return 'O';
	}
	
	@Override
	public Estado SigEstado(int vecinosVivos) {
		//Regla: Con menos de 2 vecinos vivos, muere. 
		//			Con 2 o 3 vecinos vivos, sobrevive.
		
		//Muere.
		if(vecinosVivos<NRO_DOS || vecinosVivos >NRO_TRES) {
			return new EstadoMuertos();
		}
		
		//Si sobrevive.Probabilidades de enfermarce
		if(Math.random() < PROB_ENFER) {
			return new EstadoEnfermo();
		}
		return null;
	}

}
