import java.util.Random;


public class Bursa {
	private static Bursa instance = null;
	private Random r = new Random();
	private Firma firme[];
	private int nrFirme;
	private int nrOfertePerFirma;
	
	
	private Bursa(int nrFirme, int nrOfertePerFirma) //cand se creeaza bursa iau nastere si firmele care contin la randul lor ofertele
	{
		this.nrFirme = nrFirme;
		this.nrOfertePerFirma = nrOfertePerFirma;
		firme = new Firma[nrFirme];
		for(int i = 0; i < nrFirme; i++)
			firme[i] = new Firma(i, this.nrOfertePerFirma);
	}
	
	public static Bursa getInstance(int nrFirme, int nrOfertePerFirma)
	{
		if(instance == null)
			instance = new Bursa(nrFirme, nrOfertePerFirma);
		return instance; 
	}

	public Firma getFirmaRandom(int tip) 
	{		
		Firma f = firme[r.nextInt(nrFirme)];
		if(f.maiAre(tip))
		{
			return firme[r.nextInt(nrFirme)];
		}
		return getFirmaRandom(tip);
	}

	public Firma getFirmaById(int id) 
	{
		return firme[id];
	}

}
