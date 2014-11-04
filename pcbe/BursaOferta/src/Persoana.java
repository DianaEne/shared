import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class Persoana extends Thread{
	private int uid;
	private Logger localLogger;
	private Bursa bursa; 
	private Random random;
	private Map<String,Oferta> oferte = new ConcurrentHashMap<String, Oferta>();
	private int ocupatie; // 0=cumparator 1=vanzator
	private int buget;
	private Oferta ofertaCurenta;
	
	public Persoana(int uid)
	{	
		bursa = Bursa.getInstance(10, 10);
		if(bursa == null)
			System.out.println("Error bursa == null");
		this.localLogger = new Logger();
		this.uid = uid;
		this.ocupatie = new Random().nextInt(2);
		this.buget = 2000;
		ofertaCurenta = this.CreeazaOferta();
		ofertaCurenta.setUserId(uid);
	}

	private Oferta CreeazaOferta()
	{
		Firma firma = bursa.getFirmaRandom(ocupatie);
		return firma.getOfertaRandom(ocupatie, buget);
	}
	
	
	public void cumpara(Oferta o)
	{
		buget = buget - o.getValoare();
		localLogger.TranzactieCumparare(uid, o);
	}
	
	public void vinde(Oferta o)
	{
		buget = buget + o.getValoare();
		localLogger.TranzactieVanzare(uid, o);
	}
	
	public int getUid() 
	{
		return uid;
	}
	
	public void run() 
	{	
		Firma firma = bursa.getFirmaById(ofertaCurenta.getFirma());
		if(firma == null)
			System.out.println("firma null");
		Oferta ofertaCautata = firma.CautaOferta(ofertaCurenta);
		if(ofertaCautata == null)
			System.out.println("ofertaCautata null");
		
		//test
		if(ofertaCautata==null)
		{
			System.out.println("Error! Tranzactie esuata!");
			return;
		}
		
		if(ofertaCurenta.getStare())
		{
			ofertaCurenta.Dezactivare();
			if(ocupatie == 0) //cumparator
				this.cumpara(ofertaCautata);
			if(ocupatie == 1) //vanzator
				this.vinde(ofertaCautata);
		}
		//else
			//run();
	}
	
	public void afisareLocalLogger() 
	{
		localLogger.printLog();		
	}

}
