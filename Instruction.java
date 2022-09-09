public class Instruction 
{
	public String inst;
	public long addr;
	public long addrFinal;
	public int numPagLocated;
	public int numPageLocatedFinal;
	public Page page;
	public Page pageFinal;
	public int segment;
	public int segmentFinal;

	//recibe el comando y la direccion
	public Instruction( String inst, long addr, long addrF) 
	{
		this.inst = inst;
		this.addr = addr;
		this.addrFinal = addrF;
	}


	//establece el numero de pagina donde se encuentra la instruccion
	public void setNumVirtualPage(int numPage){
		numPagLocated = numPage;
	}

	//establece el numero de pagina donde se encuentra la instruccion
	public void setNumVirtualPageFinal(int n){
		numPageLocatedFinal = n;
	}


	//retorna el numero de pagina donde se encuentra
	public int getNumVirtualPage(){
		return numPagLocated;
	}

	public void setPage(Page p){
		this.page = p;
		ubicarLimitesSeg(p);
	}

	public void setPageFinal(Page p){
		this.pageFinal = p;
		ubicarLimitesSegFinal(p);
	}

	private void ubicarLimitesSeg(Page page){
		if (page.v1Low <= addr && addr <= page.v1High){
			segment = 1;
			
		} else if(page.v2Low <= addr && addr <= page.v2High){
			segment = 2;
		}
		else if (page.v3Low <= addr && addr <= page.v3High){
			segment = 3;			
		} else if(page.v4Low <= addr && addr <= page.v4High){
			segment = 4;
		}
	}

	private void ubicarLimitesSegFinal(Page page){
		if (page.v1Low <= addrFinal && addrFinal <= page.v1High){
			segmentFinal =1;
			
		} else if(page.v2Low <= addrFinal && addrFinal <= page.v2High){
			segmentFinal = 2;
		}
		else if (page.v3Low <= addrFinal && addrFinal <= page.v3High){
			segmentFinal=3;			
		} else if(page.v4Low <= addrFinal && addrFinal <= page.v4High){
			segmentFinal = 4;
			System.out.println("entro");
		}
	}

	public Page getPage(){
		return page;
	}


}
