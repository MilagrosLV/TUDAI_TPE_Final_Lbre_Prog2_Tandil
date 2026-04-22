package modelo;

public class EstadoMuertos implements Estado {

	@Override
	public boolean isViva() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public char getRepresentacion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Estado SigEstado(int vecinosVivos) {
		// TODO Auto-generated method stub
		return null;
	}

}
