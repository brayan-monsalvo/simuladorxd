/* It is in this file, specifically the replacePage function that will
be called by MemoryManagement when there is a page fault.  The 
users of this program should rewrite PageFault to implement the 
page replacement algorithm.
*/

// This PageFault file is an example of the FIFO Page Replacement 
// Algorithm as described in the Memory Management section.

import java.util.*;
import Page;

public class PageFault {

    /**
     * The page replacement algorithm for the memory management sumulator.
     * This method gets called whenever a page needs to be replaced.
     * <p>
     * The page replacement algorithm included with the simulator is 
     * FIFO (first-in first-out).  A while or for loop should be used 
     * to search through the current memory contents for a canidate 
     * replacement page.  In the case of FIFO the while loop is used 
     * to find the proper page while making sure that virtPageNum is 
     * not exceeded.
     * <pre>
     *   Page page = ( Page ) mem.elementAt( oldestPage )
     * </pre>
     * This line brings the contents of the Page at oldestPage (a 
     * specified integer) from the mem vector into the page object.  
     * Next recall the contents of the target page, replacePageNum.  
     * Set the physical memory address of the page to be added equal 
     * to the page to be removed.
     * <pre>
     *   controlPanel.removePhysicalPage( oldestPage )
     * </pre>
     * Once a page is removed from memory it must also be reflected 
     * graphically.  This line does so by removing the physical page 
     * at the oldestPage value.  The page which will be added into 
     * memory must also be displayed through the addPhysicalPage 
     * function call.  One must also remember to reset the values of 
     * the page which has just been removed from memory.
     *
     * @param mem is the vector which contains the contents of the pages 
     *   in memory being simulated.  mem should be searched to find the 
     *   proper page to remove, and modified to reflect any changes.  
     * @param virtPageNum is the number of virtual pages in the 
     *   simulator (set in Kernel.java).  
     * @param replacePageNum is the requested page which caused the 
     *   page fault.  
     * @param controlPanel represents the graphical element of the 
     *   simulator, and allows one to modify the current display.
     */

	// + recibe el vector con todas las paginas virtuales Vector mem
	// + recibe el numero de paginas virtuales
	// + recibe el numero de pagina virtual donde se encuentra la instruccion con el comando READ
	// + recibe el ControlPanel
  	public static void replacePage ( Vector mem , int virtPageNum , int replacePageNum , ControlPanel controlPanel ) 
  	{
		int count = 0;
		int oldestPage = -1;
		int oldestTime = 0;
		int firstPage = -1;
		int map_count = 0;
		boolean mapped = false;

		//mientras no se haya mapeado toda la memoria virtual o 
		//mientras el contador sea diferente al numero de paginas virtuales
		while ( ! (mapped) || count != virtPageNum ) 
		{
			//se obtiene la pagina count (iniciando en 0)
			Page page = ( Page ) mem.elementAt( count );

			//si la pagina virtual tiene pagina fisica, 
			//esto quiere decir que la primera pagina que este en memoria principal
			//sera reemplazada (FIFO)
			if ( page.physical != -1 ) 
			{
				//si es la primer pagina virtual y ademas es la primera pagina
				if (firstPage == -1) {
					
					//la ubicacion de la primer pagina virtual se guarda en firstPage
					firstPage = count;
				}

				//si la primera pagina obtenida de la lista de paginas virtuales se encuentra en memoria virtual, 
				//y ademas ya tiene tiempo en memoria principal
				if (page.inMemTime > oldestTime) {

					//se almacena el tiempo que ha pasado en memoria principal la pagina
					oldestTime = page.inMemTime;

					//se guarda el numero de pagina mas antigua 
					oldestPage = count;

					mapped = true;
				}
			}
			
			//el numero de pagina avanza
			count++;

			//si el numero de pagina es igual al total de paginas virtuales
			if ( count == virtPageNum ) 
			{
				mapped = true;
			}
		}

		
		if (oldestPage == -1) 
		{
			oldestPage = firstPage;
		}
		
		Page page = ( Page ) mem.elementAt( oldestPage );

		//se obtiene la pagina virtual donde se encuentra la instruccion
		Page nextpage = ( Page ) mem.elementAt( replacePageNum );

		//se remueve la pagina
		controlPanel.removePhysicalPage( oldestPage );

		//se establece la direccion fisica de la pagina recien removida a la pagina 
		//con la instruccion
		nextpage.physical = page.physical;

		//se anade esta pagina 
		controlPanel.addPhysicalPage( nextpage.physical , replacePageNum );

		/*la pagina que fue removida se coloca su tiempo memoria  en 0, 
		 * el ultimo momento en que fue referencia tambien a 0,
		 * READ y Modifed 0 
		 * ademas se coloca que no tiene pagina fisica 
		*/
		page.inMemTime = 0;
		page.lastTouchTime = 0;
		page.R = 0;
		page.M = 0;
		page.physical = -1;
  }
}
