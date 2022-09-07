public class Common {
	//String to Long (s2i) - convierte un string a Long
	static public long s2l ( String s ) 
  	{
		long i = 0;

		try {

			//a la cadena que recibe como parametro se le quitan los espacios al inicio y al final de esta
			//ej. '  numero ' -> 'numero'
			//despues, se convierte la cadena en un numero tipo long
	  		i = Long.parseLong(s.trim());
		} catch (NumberFormatException nfe) {
	  		System.out.println("NumberFormatException: " + nfe.getMessage());
		}

		return i;
  	}

	//String to Int - convierte un string a entero 
  	static public int s2i ( String s ) 
  	{
		int i = 0;

		try {
			//a la cadena que recibe como parametro se le quitan los espacios al inicio y al final de esta
			//ej. '  numero ' -> 'numero'
			//despues, se convierte la cadena en un numero tipo entero
	  		i = Integer.parseInt(s.trim());
		} catch (NumberFormatException nfe) {
	  		System.out.println("NumberFormatException: " + nfe.getMessage());
		}

	return i;
  	}

	//String to Byte - convierte un string a byte
  	static public byte s2b ( String s ) 
  	{
		int i = 0;
		byte b = 0;

		try {
			//elimina los espacios de la cadena y despues la convierte en entero
	  		i = Integer.parseInt(s.trim());
		} catch (NumberFormatException nfe) {
	  		System.out.println("NumberFormatException: " + nfe.getMessage());
		}
		//se convierte el entero en byte
		b = (byte) i;
		return b;
  	}

	
	public static long randomLong( long MAX ) 
	{
		long i = -1;

		java.util.Random generator = new
		java.util.Random(System.currentTimeMillis());
		while (i > MAX || i < 0)
		{
			int intOne = generator.nextInt();
			int intTwo = generator.nextInt();
			i = (long) intOne + intTwo;
		}
		return i;
	}
}

