import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class Persoana extends Thread{
	private int uid;
	private Integer buget = 2000;
	private Logger localLogger;
	private static Bursa bursa = Bursa.getInstance();
	private Random random;
	private Map<String,Oferta> oferte = new ConcurrentHashMap<String, Oferta>();
	
	public Persoana(int uid)
	{	this.localLogger=new Logger();
		this.uid=uid;
		random = new Random();
		creazaOferteRandom();
	}

	public int getUid() {
		return uid;
	}
	
	public void creazaOferteRandom()
	{	
		
	}
	
	public void cumparaOferta() 
	{	int i;
		Firma firma = bursa.getFirma();
		Oferta oferta = firma.getOfertaRandom();
		
		//test
		if(oferta==null)
		{
			//System.out.println("incearca sa cumpere de la el insusi");
			return;
		}
		
		bursa.proceseazaComanda(oferta, this);
	}
	
	public int getNumarActiuniLaFirma(String numeFirma)
	{	
		Oferta oferta = oferte.get(numeFirma);
		if(oferta == null)
		{	
			return 0;
		}
		else
		{	
			return oferta.getNumarActiuni();
		}
	}
}
