package modelo;

public class EstadoEnfermo implements Estado {

	@Override
	public boolean isViva() {
		//Viva = true
		return true;
	}

	@Override
	public char getRepresentacion() {
		//Se representa con la E mayuscula, por Enfermedad
		return 'E';
	}

	@Override
	public Estado SigEstado(int vecinosVivos) {
		//Regla: Una celda enferma, en la suguiente generación morirá
		return new EstadoMuerto();
	}

}
