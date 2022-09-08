import java.util.Vector;

public class Virtual2Physical
{
	// memaddr:	direccion de la instruccion
	// numpages: numero de paginas virtuales
	// tamano de la pagina 
	
  	public static int pageNum ( long memaddr , int numpages , long block )
  	{
		int i = 0;
		long high = 0;
		long low = 0;
		
		//recorre todas las paginas virtuales
		for (i = 0; i <= numpages; i++)
		{
			//se guarda en low el limite inferior de la pagina
			low = block * i;
			
			//se guarda en high el limite superior de la pagina
			high = block * ( i + 1 );

			// si el limite inferior de la pagina es menor o igual que la direccion de la instruccion
			// y la direccion de la instruccion es menor que el limite superior de la pagina
			// (si la direccion de la instruccion se encuentra entre el limite inferior y superior de la pagina) 
			if ( low <= memaddr && memaddr < high )
			{
				//retorna el numero de la pagina donde se ubica la instruccion
				return i;
			}
		}

		return -1;
 	}
}
