package modelo;

public interface Estado {
	boolean isViva();//viva = true; Muerta=false;
	char getRepresentacion(); //Presenta las distintas representaciones de los estados de las celdas, como X, . , O, etc.
	Estado SigEstado(int vecinosVivos); //Cadad clase que im,plemente esta interface determinará su comportamientio.
}
