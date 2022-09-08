import java.io.DataInputStream;
import java.util.StringTokenizer;
import java.io.*;

public class Page 
{
    public int id;
    public int physical;
    public byte R;
    public byte M;
    public int inMemTime;
    public int lastTouchTime;
    public long high;
    public long low;
	public int segmentSize = 4095;
	public byte base = 16;
	String segmento1 = null;
	String segmento2 = null;
	String segmento3 = null;
	String segmento4 = null;
	long v1High;
	long v2High;
	long v3High;
	long v4High;
	long v1Low;
	long v2Low;
	long v3Low;
	long v4Low;

  	public Page( int id, int physical, byte R, byte M, int inMemTime, int lastTouchTime, long high, long low ) 
  	{
		this.id = id;
		this.physical = physical;
		this.R = R;
		this.M = M;
		this.inMemTime = inMemTime;
		this.lastTouchTime = lastTouchTime;
		this.high = high;
		this.low = low;
		calcularSegmentos();

  	} 	

	private void calcularSegmentos(){
		v1High = (int)low + segmentSize;
		v2High= v1High + 1 + segmentSize;
		v3High = v2High + 1 + segmentSize;
		v4High = (int)high;

		v1Low = low;
		v2Low = v1High+1;
		v3Low = v2High+1;
		v4Low = v3High+1;

		segmento1 = Long.toString(v1Low, base)  + "  --  " + Long.toString(v1High, base) ;
		segmento2 = Long.toString(v2Low, base)  + "  --  " + Long.toString(v2High, base) ;
		segmento3 = Long.toString(v3Low, base)  + "  --  " + Long.toString(v3High, base) ;
		segmento4 = Long.toString(v4Low, base)  + "  --  " + Long.toString(v4High, base) ;
	}

}
