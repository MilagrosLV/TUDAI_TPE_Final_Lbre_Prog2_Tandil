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
		//Se representa con O mayuscula, porque se puso de ejemplo en la consigna
		return 'O';
	}
	
	@Override
	public Estado SigEstado(int vecinosVivos) {
		//Regla: Con menos de 2 vecinos vivos, muere(soledad). 
		//		Con 2 o 3 vecinos vivos, sobrevive.
		
		//Muere.
		if(vecinosVivos<NRO_DOS || vecinosVivos >NRO_TRES) {
			return new EstadoMuerto();
		}
		
		//Si sobrevive.Probabilidades de enfermarce 25%
		if(Math.random() < PROB_ENFER) {
			return new EstadoEnfermo();
		}
		
		//Si no murió ni se enfermó
		return this;
	}

}
