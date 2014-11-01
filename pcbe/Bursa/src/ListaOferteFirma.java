import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class ListaOferteFirma {
	//ar trebui hash tree in loc de arraylist dar devine complicat
	private Map<Persoana,Oferta> mapOferte;
	private String nume; //numefirma
	private ReentrantReadWriteLock rw;
	private Random random=new Random();

	public ListaOferteFirma(String nume) {
		this.nume = nume;
		this.mapOferte = new HashMap<Persoana,Oferta>();
		this.rw=new ReentrantReadWriteLock();
		
	}
	

	public void adaugaOferta(Oferta oferta) {
		//oferta se modifica?
		//lock de write pe lista oferte
		rw.writeLock().lock();
		mapOferte.put(oferta.getOwner(),oferta);
		//unlock write pe mapOferte
		rw.writeLock().unlock();
	}


	public void stergeOferta(Oferta oferta) 
	{	
		rw.writeLock().lock();
		mapOferte.remove(oferta);
		rw.writeLock().unlock();
	}

	public String getNume() {
		return nume;
	}

	//sa returneze o oferta random a unei persoane diferite de owner

	public Oferta getOfertaRandom(Persoana owner)
	{	//trebuie neaparat readlock pe toata bucata asta, altfel se strica iteratorul
		
		rw.readLock().lock();
		Iterator it=mapOferte.values().iterator();
		int ioferta=random.nextInt(mapOferte.size());
		int i=0;
		while(it.hasNext())
		{
			Oferta oferta=(Oferta)it.next();
			if(i==ioferta)
			{	
				if(oferta.getOwner()!=owner)
				{	
					rw.readLock().unlock();
					return oferta;	//poate sa se buseasca oferta si ghinion a cumparat-o altcineva
					                //dar trebuie sa testam ca e diferita de null la procesare
				}
	
			//cumpara de la el insusi
				break;
			}
			i++;
		}
		rw.readLock().unlock();
		return null;
	}


	public void modificaOferta(Oferta oferta, int pret, int numarActiuni) {

		rw.writeLock().lock();
		
		mapOferte.get(oferta.getOwner()).setNumarActiuni(numarActiuni);
		mapOferte.get(oferta.getOwner()).setPretActiune(pret);
		
		rw.writeLock().unlock();
		
	}

	//fishy
	public boolean containsOferta(Oferta oferta) {
		
		if(mapOferte.containsValue(oferta))
			return true;
		else 
			return false;

	}
	
}
