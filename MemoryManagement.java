// The main MemoryManagement program, created by Alexander Reeder, 2000 Nov 19

import java.applet.*;
import java.awt.*;
import java.io.*;
import java.util.*;
/*import ControlPanel;
import PageFault;
import Virtual2Physical;
import Common;
import Page;*/

public class MemoryManagement 
{
	public static void main(String[] args) 
	{
		ControlPanel controlPanel;
		Kernel kernel;
		
		// si el numero de argumentos es menor a 1 o mayor a 2: terminara el programa.
		if ( args.length < 1 || args.length > 2 ) 
		{
			System.out.println( "Usage: 'java MemoryManagement <COMMAND FILE> <PROPERTIES FILE>'" );
			System.exit( -1 );
		} 

		// se crea un archivo con el nombre del archivo commands
		File f = new File( args[0] );

		// si el archivo no existe, muestra un error y termina el programa.
		if ( ! ( f.exists() ) ) 
		{
			System.out.println( "MemoryM: error, file '" + f.getName() + "' does not exist." );
			System.exit( -1 );
		}

		//si no se puede leer el archivo, muestra un error termina el programa.
		if ( ! ( f.canRead() ) ) 
		{
			System.out.println( "MemoryM: error, read of " + f.getName() + " failed." );
			System.exit( -1 );
		}

		//si el numero de argumentos es igual a 2
		if ( args.length == 2 ) 
		{
				//se crea un archivo con el nombre memory.conf
				f = new File( args[1] );

				//si no existe
				if ( ! ( f.exists() ) ) 
				{
						System.out.println( "MemoryM: error, file '" + f.getName() + "' does not exist." );
						System.exit( -1 );
				}
				
				//si no se puede leer
				if ( ! ( f.canRead() ) ) 
				{
						System.out.println( "MemoryM: error, read of " + f.getName() + " failed." );
						System.exit( -1 );
				}

		}
		
		//si el o los archivos pasados como argumento existen y ademas se pueden leer, entonces
		
		//se crea un nuevo kernel
		kernel = new Kernel();

		//se crea un nuevo ControlPanel con el titulo "Memory Management"
		controlPanel = new ControlPanel( "Memory Management" );

		//si solamente hay un argumento 
		if ( args.length == 1 ) 
		{
			 //inicia el ControlPanel, se le pasa solamente el archivo commands como parametro, memory.conf es nulo 
				controlPanel.init( kernel , args[0] , null );
		}
		else
		{
				//inicia el ControlPanel, se le pasa ambos archivos (commands y memory.conf) como parametro
				controlPanel.init( kernel , args[0] , args[1] );
		}
	}
}
