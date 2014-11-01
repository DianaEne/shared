import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class Procesator {
	//invariant pentru mapFirme: sa nu se stearga vreodata o firma, devine complicat
	//trebuie lock de citire scriere pe map fiindca daca se adauga un element in map si se redminesioneaza
	//se poate ca pozitia altor elemente sa se modifice fata de cum erau inainte
	private Map<String,ListaOferteFirma> mapFirme = new HashMap<String,ListaOferteFirma>();
	private ReentrantReadWriteLock rw;
	//private ArrayList<ListaOferteFirma> listaFirme = new ArrayList<ListaOferteFirma>();
	private Random random=new Random();

	private static Procesator procesator;

	private Procesator(){
		rw=new ReentrantReadWriteLock();
	}

	public static Procesator getInstance(){
		if(procesator == null)
			procesator = new Procesator();
		return procesator;
	}
	public String randomFirma()
	{	//trebuie sincronizare, sa nu se adauge alta firma si sa strice hash-ul in timp ce il citim
		//ar trebui si la asta read lock in lock de synchronized;
		String retFirma=null;
		//synchronized(mapFirme)
		
		rw.readLock().lock();
		int len=mapFirme.size();
		int ifirma=random.nextInt(len);
		try
		{	retFirma=mapFirme.keySet().toArray(new String[0])[ifirma];
		}
		catch(Exception e)
		{ 	System.out.println("randomFirma error");
			e.printStackTrace();
		}
		rw.readLock().unlock();
		return retFirma;
	}

	/* adauga firma daca nu exista
	 * returneaza firma si daca exista deja si daca nu exista
	 */

	public ListaOferteFirma adaugaFirma(String numeFirma){
		ListaOferteFirma listaOferteFirma;
		
		//synchronized (mapFirme)
		rw.writeLock().lock();
		
		listaOferteFirma= mapFirme.get(numeFirma);
		if(listaOferteFirma==null)
		{ /* nu exista deci putem sa adaugam */
			listaOferteFirma=new ListaOferteFirma(numeFirma);
			mapFirme.put(numeFirma,listaOferteFirma);
		}
	
		rw.writeLock().unlock();
		return listaOferteFirma;
	}

	public void adaugaOferta(Oferta oferta){	
		/*adaugam firma (o sa adauge numai daca nu exista, deci mai bine sa fim siguri ca exista)
		 * daca exista sau nu exista firma returneaza listaofertefirma
		 */
		//lock read pe map
		
		ListaOferteFirma listaOferteFirma=adaugaFirma(oferta.getNumeFirma());
		//unlock read pe map
		listaOferteFirma.adaugaOferta(oferta);

	}
	/*
	public ArrayList<ListaOferteFirma> getListaFirme(){

		return listaFirme;
	}*/
	public ListaOferteFirma getListaOferteFirma(String numeFirma)
	{	ListaOferteFirma listaOferte=null;
		
		rw.readLock().lock();
		listaOferte=mapFirme.get(numeFirma);
		rw.readLock().unlock();
		
		return listaOferte;
	}

	//functia asta ar trebui mutata in persoana si acolo pus lock pe balanta, si getlocal la balanta nu la distanta
	//thread-ul este cel al cumparatorului
	public void proceseazaComanda(Oferta oferta,Cumparator cumparator)
	{
		//lock(oferta)
		//{
		ListaOferteFirma listaOferte=procesator.getListaOferteFirma(oferta.getNumeFirma());
		
		synchronized (listaOferte) {
			//trebuie verificat daca oferta mai e in mapOferte, null nu are cum sa fie ca ai referinta la ea
			if(getFirma(oferta.getNumeFirma()).containsOferta(oferta) != false)	//cumparator != null   //cumparatorul nu are cum sa fie null		
			{
				//vanzator -> persoana
				synchronized(oferta)
				{
					Vanzator owner = oferta.getOwner();
					String numeFirma = oferta.getNumeFirma();
					int numarActiuni = oferta.getNumarActiuni();
					
					//nu trebuie lock pe balanta
					//aici balanta cumparatorului poate numai sa creasca daca nu avem sincronizare, deci if-ul ar trebui sa fie in regula daca avem race
					//threadul este cel al cumparatorului, deci asta e singurul thread care poate sa micsoreze balanta cumparatorului
					//daca avem race condition balanta numai se mareste, fiindca poate sa vanda la unul sau mai multi in acelas timp pe threadurile cumparatorilor
					//deci balanta se face numai mai mare, nu se face mai mica daca avem race condition
					//conditia de la if nu are cum sa se invalideze odata ce a trecut si apoi avem race condition
					//avem invariant in proceseazaComanda ca balantaCumparatorului poate numai sa creasca la race
					if(((Persoana)cumparator).getBalanta() < oferta.getPretActiune()*oferta.getNumarActiuni() )
					{
						/* nu afisam ca o sa avem bottlneck la afisare */
						//System.out.println("Eroare de bani client");
						return;
					}
	
					if(owner.getNumarActiuniFirma(numeFirma) - numarActiuni < 0)
					{
						//are din vreun motiv mai putine actiuni decat sunt in oferta
						//System.out.println("Eroare de minus pentru actiuni");
						return;
					}
	
					owner.scadeNumarActiuni(numarActiuni, 
							numeFirma,oferta.getPretActiune());
	
					//aici nu trebuie sincronizare ca poate avea loc o singura cumparare in acelas timp
					cumparator.adaugaActiuni(numarActiuni, numeFirma, (int)oferta.getPretActiune());
	
	
					getFirma(numeFirma).stergeOferta(oferta);
				}
				//end lock(oferta)
			}
		}
	}


	/* lock de write pe listaofertafirma
	 * atunci cand se elibereaza lock-ul de write si se activeaza vreun read
	 * orice iterator la lista de oferte se strica
	 */
	public void stergeOferta(Oferta oferta)
	{	//lock de read pe map
		rw.readLock().lock();
		ListaOferteFirma listaOferteFirma= mapFirme.get(oferta.getNumeFirma());
		rw.readLock().unlock();
		//unlock pe read pe map
		
		//lock-ul pe element din map e in functie
		if(listaOferteFirma!=null)
			listaOferteFirma.stergeOferta(oferta);		
	}

	private ListaOferteFirma getFirma(String numeFirma)
	{	//lock de read pe map
		rw.readLock().lock();
		ListaOferteFirma listaOferteFirma= mapFirme.get(numeFirma);
		rw.readLock().unlock();
		//unlock read pe map
		//se poate elibera lock-ul fiindca mapul daca se modifica nu ne influenteaza pointerul cu nimic, numai pozitia lui in map

	
		return listaOferteFirma;
		//avem lock in lock asta e naspa?
	}

	public void modificaOferta(Oferta oferta, int pret, int numarActiuni) {
		rw.readLock().lock();
		ListaOferteFirma listaOferteFirma = mapFirme.get(oferta.getNumeFirma());
		rw.writeLock().unlock();
		
		listaOferteFirma.modificaOferta(oferta,pret,numarActiuni);
	}



}
