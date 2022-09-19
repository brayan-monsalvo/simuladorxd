/* It is in this file, specifically the replacePage function that will
be called by MemoryManagement when there is a page fault.  The 
users of this program should rewrite PageFault to implement the 
page replacement algorithm.
*/

// This PageFault file is an example of the FIFO Page Replacement 
// Algorithm as described in the Memory Management section.

import java.util.*;

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
	// + recibe el numero de pagina virtual donde se encuentra la instruccion
	// + recibe el ControlPanel
	// + recibe la cola de las paginas fisicas
  	public static void replacePage ( Vector mem , int virtPageNum , int replacePageNum , ControlPanel controlPanel, Queue <Page> memPhysical) 
  	{
		int cont = 1;
		int oldestPage = -1;
		int oldestTime = 0;
		int firstPage = -1;
		int map_count = 0;
		boolean mapped = false;

		Page pagina = memPhysical.elementAt(cont);

		do{
			if(pagina.bitReferencia == 1){
				System.out.println("pagina "+pagina.id+" con bit de referencia = "+pagina.bitReferencia);
				pagina.bitReferencia = 0;
				cont += 1;
				pagina = memPhysical.elementAt(cont);
				System.out.println(" now : pagina "+pagina.id+" con bit de referencia = "+pagina.bitReferencia);
			}
		}while(pagina.bitReferencia == 1);
		
		Page page = memPhysical.pollAt(cont);

		//se obtiene la pagina virtual donde se encuentra la instruccion
		Page nextpage = ( Page ) mem.elementAt( replacePageNum );

		//se remueve la pagina
		controlPanel.removePhysicalPage( page.id );

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
