public class Instruction 
{
	public String inst;
	public long addr;
	public int numPagLocated;
	public Page page;
	public int segment;

	//recibe el comando y la direccion
	public Instruction( String inst, long addr ) 
	{
		this.inst = inst;
		this.addr = addr;
	}
	
	public Instruction( String inst, long addr, int numPage) 
	{
		this.inst = inst;
		this.addr = addr;
		this.numPagLocated = numPage;
	} 	

	//establece el numero de pagina donde se encuentra la instruccion
	public void setNumVirtualPage(int numPage){
		numPagLocated = numPage;
	}

	//retorna el numero de pagina donde se encuentra
	public int getNumVirtualPage(){
		return numPagLocated;
	}

	public void setPage(Page p){
		this.page = p;
		ubicarLimitesSeg(p);
	}

	private void ubicarLimitesSeg(Page page){
		if (page.v1Low <= addr && addr <= page.v1High){
			segment =1;
			
		} else if(page.v2Low <= addr && addr <= page.v2High){
			segment = 2;
		}
		else if (page.v3Low <= addr && addr <= page.v3High){
			segment=3;			
		} else if(page.v4Low <= addr && addr <= page.v4High){
			segment = 4;
		}
	}

	public Page getPage(){
		return page;
	}


}
