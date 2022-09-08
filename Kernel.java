import java.lang.Thread;
import java.io.*;
import java.util.*;
import Page;

public class Kernel extends Thread
{
  // The number of virtual pages must be fixed at 63 due to
  // dependencies in the GUI
    private static int virtPageNum = 63;

    private String output = null;
    private static final String lineSeparator = 
    System.getProperty("line.separator");
    private String command_file;
    private String config_file;
    private ControlPanel controlPanel ;
    private Vector memVector = new Vector();
    private Vector instructVector = new Vector();
    private String status;
    private boolean doStdoutLog = false;
    private boolean doFileLog = false;
    public int runs;
    public int runcycles;
    public long block = (int) Math.pow(2,12);
    public static byte addressradix = 10;

    public void init( String commands , String config )  
    {
        File f = new File( commands );
        command_file = commands;
        config_file = config;
        String line;
        String tmp = null;
        String command = "";
        byte R = 0;
        byte M = 0;
        int i = 0;
        int j = 0;
        int id = 0;
        int physical = 0;
        int physical_count = 0;
        int inMemTime = 0;
        int lastTouchTime = 0;
        int map_count = 0;
        double power = 14;
        long high = 0;
        long low = 0;
        long addr = 0;
        long address_limit = (block * virtPageNum+1)-1;
  
		//si el archivo memory.conf no es nulo, entonces...
        if ( config != null )
        {
            f = new File ( config );
			try 
			{
				DataInputStream in = new DataInputStream(new FileInputStream(f));
				//dentro del ciclo while mientras el renglon no sea nulo,
				//recorrera linea por linea el archivo memory.conf

				while ((line = in.readLine()) != null) 
				{
					//si el renglon empieza o contiene la cadena 'numpages'...
					if (line.startsWith("numpages")) 
					{ 
						//con StringTokenizer se divide el renglon (cadena) en dos partes usando los espacios
						//ej. 'numpages 64' -> 'numpages', '64'
						StringTokenizer st = new StringTokenizer(line);

						//mientras haya tokens (palabras)...
						while (st.hasMoreTokens()) 
						{
							//se guarda el token 'numpages' en tmp, dejando unicamente el token con 
							//el numero de paginas
							tmp = st.nextToken();
							
							//se guardan el numero de paginas
							virtPageNum = Common.s2i(st.nextToken()) - 1;

							//si el numero de paginas es 1 o mayor a 63, imprime un error y termina el programa
							if ( virtPageNum < 2 || virtPageNum > 63 )
							{
								System.out.println("MemoryManagement: numpages out of bounds.");
								System.exit(-1);
							}

							//aqui (supongo) que vuelve a calcular el limite de memoria virtual
							address_limit = (block * virtPageNum+1)-1;
						}
					}
				}

				//cierra el archivo
				in.close();
			} catch (IOException e) { /* Handle exceptions */ }

			//en este ciclo for se recorren todas las paginas virtuales (63)
			for (i = 0; i <= virtPageNum; i++) 
			{
				//high guarda el limite superior de la pagina 
				high = (block * (i + 1))-1;

				//low guarda el limite inferior de la pagina
				low = block * i;

				/*en el vector memVector se añade una nueva pagina
				la pagina se inicializa con 
				+ i - numero de pagina virtual
				+ numero de pagina fisica (-1 inicialmente ya que no se encuentra en memoria fisica)
				+ R (leida) (iniciando en 0)
				+ M (modificada) (iniciando en 0)
				+ inMemoryTime (tiempo en memoria) (iniciando en 0)
				+ lastTouchTime (ultima vez que ? )
				+ high - limite superior de la pagina virtual
				+ low - limite inferior de la pagina virtual*/
				memVector.addElement(new Page(i, -1, R, M, 0, 0, high, low));
			}

			try 
			{
				
				DataInputStream in = new DataInputStream(new FileInputStream(f));
				//dentro del ciclo while se recorrera el archivo memory.conf renglon por renglon
				//mientras el renglon no sea nulo...
				while ((line = in.readLine()) != null) 
				{
					//si el renglon empieza o contiene la palabra 'memset', entonces...
					if (line.startsWith("memset")) 
					{     
						//se descompone el renglon en varios tokens (palabras) separadas por espacios
						StringTokenizer st = new StringTokenizer(line);
						//desde aqui se omite el token 'memset' para comenzar con los otros tokens
						st.nextToken();

						//mientras tokens restantes
						while (st.hasMoreTokens()) 
						{ 
							//se guarda el numero de pagina virtual
							id = Common.s2i(st.nextToken());

							//temporalmente se guarda el numero de pagina fisica
							tmp = st.nextToken();

							//si el numero de pagina fisica (token) contiene o es igual a x ...
							if (tmp.startsWith("x")) 
							{
								//la direccion de memoria fisica se establece como -1
								physical = -1;
							} 
							else 
							{
								//se convierte el token (string) a entero, almacenando en
								//physical el numero de pagina en memoria fisica

								physical = Common.s2i(tmp);
							}

							//si el numero de pagina virtual es menor a 0 o es mayor al numero de paginas virtuales maximo (63)
							// o si el numero de pagina fisica es menor a -1 o si el numero de pagina fisica es mayor 
							//que el la mitad de paginas virtuales, imprimre un error y termina el programa
							if ((0 > id || id > virtPageNum) || (-1 > physical || physical > ((virtPageNum - 1) / 2)))
							{
								System.out.println("MemoryManagement: Invalid page value in " + config);
								System.exit(-1);
							}
							
							//convierte el siguiente token (string) (bit de Read) a byte
							R = Common.s2b(st.nextToken());

							//si el bit Read es menor a 0 o mayor a 1, imprime un error y termina el programa
							if (R < 0 || R > 1)
							{
								System.out.println("MemoryManagement: Invalid R value in " + config);
								System.exit(-1);
							}
							
							//convierte el siguiente token (string) (bit de Modified) a byte
							M = Common.s2b(st.nextToken());

							//si el bit Modified es menor a cero o mayor a 1, imprime un error y termina el programa
							if (M < 0 || M > 1)
							{
								System.out.println("MemoryManagement: Invalid M value in " + config);
								System.exit(-1);
							}

							//convierte el siguiente token (inMemoryTime) de string a entero
							inMemTime = Common.s2i(st.nextToken());

							//si inMemTime es menor a 0, imprime un error y termina el programa
							if (inMemTime < 0)
							{
								System.out.println("MemoryManagement: Invalid inMemTime in " + config);
								System.exit(-1);
							}

							//convierte el siguiente token (lastTouchTime) de string a entero
							lastTouchTime = Common.s2i(st.nextToken());

							//si lastTouchTime es menor a cero, imprime un error y termina el programa
							if (lastTouchTime < 0)
							{
								System.out.println("MemoryManagement: Invalid lastTouchTime in " + config);
								System.exit(-1);
							}

							//se ubica la pagina en memoria virtual con el id (numero de memoria virtual en memory.conf)
							//y se le asignan los valores anteriormente extraidos de memory.conf
							Page page = (Page) memVector.elementAt(id);
							page.physical = physical;
							page.R = R;
							page.M = M;
							page.inMemTime = inMemTime;
							page.lastTouchTime = lastTouchTime;
						}
					}


					if (line.startsWith("enable_logging")) 
					{ 
						StringTokenizer st = new StringTokenizer(line);
						while (st.hasMoreTokens()) 
						{
							if ( st.nextToken().startsWith( "true" ) )
							{
								doStdoutLog = true;
							}              
						}
					}

					if (line.startsWith("log_file")) 
					{ 
						StringTokenizer st = new StringTokenizer(line);
						while (st.hasMoreTokens()) 
						{
							tmp = st.nextToken();
						}
						if ( tmp.startsWith( "log_file" ) )
						{
							doFileLog = false;
							output = "tracefile";
						}              
						else
						{
							doFileLog = true;
							doStdoutLog = false;
							output = tmp;
						}
					}
						
					if (line.startsWith("pagesize")) 
					{
						//se separan en token el renglon que contiene la cadena "pagesize"
						StringTokenizer st = new StringTokenizer(line);

						//mientras haya tokens ...
						while (st.hasMoreTokens()) 
						{
							//se guarda temporalmente el token "pagesize"
							tmp = st.nextToken();

							//se guarda en tmp el token "<numero del tamano de paginas>"
							tmp = st.nextToken();

							//si el token empieza o contiene la cadena 'power'
							if ( tmp.startsWith( "power" ) )
							{
								//se convierte a entero el siguiente token, despues se convierte a double y se guarda
								//en power
								power = (double) Integer.parseInt(st.nextToken());

								//ahora el tamano del block se establece como 2 ^ power
								block = (int) Math.pow(2,power);
							}
							else
							{
								//si el token no contiene la palabra 'power' simplemente se convierte 
								//a long 
								//se reescribe el valor anterior de block 4096 por 16384 (valor en memory.conf)
								block = Long.parseLong(tmp,10);             
							}

							//ahora, el limite de memoria virtual se establece de 258,048 a 1,048,575
							address_limit = (block * virtPageNum+1)-1;
						}

						//si el tamano de la pagina es menor a 64 o mayor a 2^26, imprimira un error y terminara
						//el programa
						if ( block < 64 || block > Math.pow(2,26))
						{
							System.out.println("MemoryManagement: pagesize is out of bounds");
							System.exit(-1);
						}

						/*dentro del for, se recorre nuevamente todas las paginas en memoria virtual, 
						pero ahora se modifica el limite inferior y superior, ajustandose al nuevo 
						tamano del bloque (4096) -> 16384
						*/
						for (i = 0; i <= virtPageNum; i++) 
						{
							Page page = (Page) memVector.elementAt(i);
							page.high = (block * (i + 1))-1;
							page.low = block * i;
						}
					}

					if (line.startsWith("addressradix")) 
					{ 
						StringTokenizer st = new StringTokenizer(line);
						while (st.hasMoreTokens()) 
						{
							tmp = st.nextToken();
							tmp = st.nextToken();
							addressradix = Byte.parseByte(tmp);
							if ( addressradix < 0 || addressradix > 20 )
							{
								System.out.println("MemoryManagement: addressradix out of bounds.");
								System.exit(-1);
							}
						}
					}
				}

				in.close();
			} catch (IOException e) { /* Handle exceptions */ }
    	}

		//ahora se crea un archivo para recorrer commands
    	f = new File ( commands );
    	try 
    	{
      		DataInputStream in = new DataInputStream(new FileInputStream(f));
			//mientras la siguiente linea en commands no sea nula ... 
      		while ((line = in.readLine()) != null) 
      		{	
				//si el renglon empieza o contiene la palabra READ o WRITE
        		if (line.startsWith("READ") || line.startsWith("WRITE")) 
        		{
					//si empieza o contiene READ
          			if (line.startsWith("READ")) 
          			{
						//el comando se establece como READ
            			command = "READ";
          			}

					//si empieza o contiene WRITE
          			if (line.startsWith("WRITE")) 
          			{
						//el comando se establece como WRITE
            			command = "WRITE";
          			}

					//el renglon se separa en varios tokens
          			StringTokenizer st = new StringTokenizer(line);

					//temporalmente  se guarda el comando
					tmp = st.nextToken();

					//temporalmente se guarda el tipo de numero (binario, hexa, octa)
					tmp = st.nextToken();

					//si el token tipo de numero empieza o contiene 'random'
          			if (tmp.startsWith("random")) 
          			{
						//  ??????
            			instructVector.addElement(new Instruction(command,Common.randomLong( address_limit )));
          			} 
          			else 
          			{	
						//si el token no contiene random
						
						//si el tipo de numero es binario
						if ( tmp.startsWith( "bin" ) )
						{
							//convierte el siguiente token (direccion de memoria virtual) a decimal 
							//y lo guarda en addr
							addr = Long.parseLong(st.nextToken(),2);             
						}

						//si el tipo de numero es octal
						else if ( tmp.startsWith( "oct" ) )
						{
							//convierte el siguiente token a decimal y lo guarda en addr
							addr = Long.parseLong(st.nextToken(),8);
						}

						//si el tipo de numero es hexadecimal
						else if ( tmp.startsWith( "hex" ) )
						{
							//convierte el siguiente token a decimal y lo guarda en addr
							addr = Long.parseLong(st.nextToken(),16);
						}

						//si no especifica el tipo de numero
						else
						{
							//guarda en addr la direccion de memoria (en decimal supongo)
							addr = Long.parseLong(tmp);
						}

						//si la direccion es menor a 0 o la direccion es mayor al limite de direccionamiento 
						//virtual, imprime un error y termina el programa
						if (0 > addr || addr > address_limit)
						{
							System.out.println("MemoryManagement: " + addr + ", Address out of range in " + commands);
							System.exit(-1);
						}

						//se almacena en instructVector una nueva instruccion que tiene: 
						// + comando (READ o WRITE)
						// + direccion de memoria virtual
						instructVector.addElement(new Instruction(command,addr));
          			}
        		}
    		}
      		in.close();
    	} catch (IOException e) { /* Handle exceptions */ }

		//guarda en runcycles el numero de instrucciones
    	runcycles = instructVector.size();

		//si el numero de instrucciones es menor a 1, imprime un error y termina el programa
    	if ( runcycles < 1 )
    	{
      		System.out.println("MemoryManagement: no instructions present for execution.");
      		System.exit(-1);
    	}

		// ?
    	if ( doFileLog )
    	{
      		File trace = new File(output);
      		trace.delete();
		}


    	runs = 0;

		//dentro este ciclo, se recorren todas las paginas virtuales
    	for (i = 0; i < virtPageNum; i++) 
    	{
			//se almacena la pagina de memoria virtual con la direccion i
      		Page page = (Page) memVector.elementAt(i);

			//si la pagina virtal i esta en memoria fisica (page.physical diferente de -1)
      		if ( page.physical != -1 )
      		{
				//incrementa esta variable
        		map_count++;
     	 	}

			//recorre nuevamente todas las paginas virtuales
      		for (j = 0; j < virtPageNum; j++) 
      		{
				//se almacena la pagina de memoria virtual con la direccion j
        		Page tmp_page = (Page) memVector.elementAt(j);

				//si la direccion fisica de la pagina j es igual a la direccion fisica de la pagina i
				//y ademas se encuentra en memoria fisica (page.physical diferente a -1)
        		if (tmp_page.physical == page.physical && page.physical >= 0)
        		{
					//incrementa esta variable
         		 	physical_count++;
        		}
      		}
			//si el numero de veces que una pagina virtual se encuentra en memoria fisica es mayor a 1 
      		if (physical_count > 1)
      		{
        		System.out.println("MemoryManagement: Duplicate physical page's in " + config);
        		System.exit(-1);
      		}
      		physical_count = 0;
    	}

		//si el numero de paginas virtuales en memoria fisica es menor que el numero de paginas fisicas
		// map_count < 32
    	if ( map_count < ( virtPageNum +1 ) / 2 )
    	{
      		for (i = 0; i < virtPageNum; i++) 
      		{
				//se obtiene la pagina virtual i 
        		Page page = (Page) memVector.elementAt(i);

				//si la pagina virtual no esta en memoria fisica y ademas el numero de paginas virtuales en 
				//memoria fisica es menor a 32
        		if ( page.physical == -1 && map_count < ( virtPageNum + 1 ) / 2 )
        		{
					//se le asigna la pagina fisica i
          			page.physical = i;

					//aumenta el numero de paginas virtuales en memoria fisica
          			map_count++;
        		}
     	 	}
    	}

		
    	for (i = 0; i < virtPageNum; i++) 
    	{
			//se obtiene la pagina virtual i
     		Page page = (Page) memVector.elementAt(i);

			//si la pagina virtual i no esta en memoria fisica
      		if (page.physical == -1) 
      		{
				// ???
        		controlPanel.removePhysicalPage( i );
      		} 
			else
			{
				controlPanel.addPhysicalPage( i , page.physical );
			}
    	}

		//en este ciclo se recorren todas las instrucciones leidas desde commands
    	for (i = 0; i < instructVector.size(); i++) 
    	{
			//el limite de memoria virtual se calcula multiplicando el tamano de pagina * numero de paginas virtuales
      		high = block * virtPageNum;

			//se obtiene la instruccion i desde el vector 
      		Instruction instruct = ( Instruction ) instructVector.elementAt( i );

			//si la direccion de la instruccion es menor a 0 o mayor que el limite de memoria virtual
			//se imprime un error y termina el programa
      		if ( instruct.addr < 0 || instruct.addr > high )
      		{
        		System.out.println("MemoryManagement: Instruction (" + instruct.inst + " " + instruct.addr + ") out of bounds.");
        		System.exit(-1);
      		}
    	}
	} 

  	public void setControlPanel(ControlPanel newControlPanel) 
  	{
    	controlPanel = newControlPanel ;
  	}

	//retorna la pagina virtual con el indice pageNum
  	public void getPage(int pageNum) 
  	{
    	Page page = ( Page ) memVector.elementAt( pageNum );
    	controlPanel.paintPage( page );
  	}
	
	private void printLogFile(String message)
  	{
    	String line;
    	String temp = "";

    	File trace = new File(output);
    	if (trace.exists()) 
    	{
      		try 
      		{
        		DataInputStream in = new DataInputStream( new FileInputStream( output ) );
        		while ((line = in.readLine()) != null) 
				{
          			temp = temp + line + lineSeparator;
        		}
        		in.close();
      		} catch ( IOException e ) 
      		{
        		/* Do nothing */
      		}
    	}
    	try 
    	{
      		PrintStream out = new PrintStream( new FileOutputStream( output ) );
     	 	out.print( temp );
      		out.print( message );
      		out.close();
    	} catch (IOException e) 
    	{
      		/* Do nothing */ 
    	}
  	}

  	public void run()
  	{
    	step();
    	while (runs != runcycles) 
    	{
      		try 
     	 	{
        		Thread.sleep(2000);
      		} catch(InterruptedException e) 
      		{  
        		/* Do nothing */ 
      		}
      		step();
    	}  
  	}

  	public void step()
  	{
    	int i = 0;

		//se obtiene una instruccion del vector de instrucciones
		Instruction instruct = ( Instruction ) instructVector.elementAt( runs );

		//
		controlPanel.addressValueLabel.setText( Long.toString( instruct.addr , addressradix ) );
		getPage( Virtual2Physical.pageNum( instruct.addr , virtPageNum , block ) );
		if ( controlPanel.pageFaultValueLabel.getText() == "YES" ) 
		{
			controlPanel.pageFaultValueLabel.setText( "NO" );
		}
		if ( instruct.inst.startsWith( "READ" ) ) 
		{
			Page page = ( Page ) memVector.elementAt( Virtual2Physical.pageNum( instruct.addr , virtPageNum , block ) );
			if ( page.physical == -1 ) 
			{
				if ( doFileLog )
				{
					printLogFile( "READ " + Long.toString(instruct.addr , addressradix) + " ... page fault" );
				}
				if ( doStdoutLog )
				{
					System.out.println( "READ " + Long.toString(instruct.addr , addressradix) + " ... page fault" );
				}
				PageFault.replacePage( memVector , virtPageNum , Virtual2Physical.pageNum( instruct.addr , virtPageNum , block ) , controlPanel );
				controlPanel.pageFaultValueLabel.setText( "YES" );
			} 
			else 
			{
				page.R = 1;
				page.lastTouchTime = 0;   
				if ( doFileLog )
				{
					printLogFile( "READ " + Long.toString( instruct.addr , addressradix ) + " ... okay" );
				}
				if ( doStdoutLog )
				{
					System.out.println( "READ " + Long.toString( instruct.addr , addressradix ) + " ... okay" );
				}
			}
		}

		if ( instruct.inst.startsWith( "WRITE" ) ) 
		{
			Page page = ( Page ) memVector.elementAt( Virtual2Physical.pageNum( instruct.addr , virtPageNum , block ) );
			if ( page.physical == -1 ) 
			{
				if ( doFileLog )
				{
					printLogFile( "WRITE " + Long.toString(instruct.addr , addressradix) + " ... page fault" );
				}
				if ( doStdoutLog )
				{
					System.out.println( "WRITE " + Long.toString(instruct.addr , addressradix) + " ... page fault" );
				}
				PageFault.replacePage( memVector , virtPageNum , Virtual2Physical.pageNum( instruct.addr , virtPageNum , block ) , controlPanel );          controlPanel.pageFaultValueLabel.setText( "YES" );
			} 
			else 
			{
				page.M = 1;
				page.lastTouchTime = 0;
				if ( doFileLog )
				{
					printLogFile( "WRITE " + Long.toString(instruct.addr , addressradix) + " ... okay" );
				}
				if ( doStdoutLog )
				{
					System.out.println( "WRITE " + Long.toString(instruct.addr , addressradix) + " ... okay" );
				}
			}
		}
		for ( i = 0; i < virtPageNum; i++ ) 
		{
			Page page = ( Page ) memVector.elementAt( i );
			if ( page.R == 1 && page.lastTouchTime == 10 ) 
			{
				page.R = 0;
			}
			if ( page.physical != -1 ) 
			{
				page.inMemTime = page.inMemTime + 10;
				page.lastTouchTime = page.lastTouchTime + 10;
			}
		}
		runs++;
		controlPanel.timeValueLabel.setText( Integer.toString( runs*10 ) + " (ns)" );
  	}

 	public void reset() {
		memVector.removeAllElements();
		instructVector.removeAllElements();
		controlPanel.statusValueLabel.setText( "STOP" ) ;
		controlPanel.timeValueLabel.setText( "0" ) ;
		controlPanel.instructionValueLabel.setText( "NONE" ) ;
		controlPanel.addressValueLabel.setText( "NULL" ) ;
		controlPanel.pageFaultValueLabel.setText( "NO" ) ;
		controlPanel.virtualPageValueLabel.setText( "x" ) ;
		controlPanel.physicalPageValueLabel.setText( "0" ) ;
		controlPanel.RValueLabel.setText( "0" ) ;
		controlPanel.MValueLabel.setText( "0" ) ;
		controlPanel.inMemTimeValueLabel.setText( "0" ) ;
		controlPanel.lastTouchTimeValueLabel.setText( "0" ) ;
		controlPanel.lowValueLabel.setText( "0" ) ;
		controlPanel.highValueLabel.setText( "0" ) ;
		init( command_file , config_file );
  	}
}
