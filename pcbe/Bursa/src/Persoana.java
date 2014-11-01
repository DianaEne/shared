import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
//import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.CyclicBarrier;


public class Persoana extends Thread implements Cumparator, Vanzator,Runnable {

	
	
	private int uid;
	private Integer balanta = 1000; 
	/*balanta sa fie int sa nu avem problema de precizie cand verificam la sfarsit invariantul de sistem
	 * (invariantul de sistem = toti banii de la inceputul sistemului sa fie = banii de la sfarsit - profituri )
	 */	
	private Logger localLogger;
	private static Procesator procesator=Procesator.getInstance();
	private Random random;
	private Map<String,Oferta> mapOferta = new ConcurrentHashMap<String, Oferta>();
	/* profit >0 daca mareste pretul la o actiune fata de cu cat a cumparat-o sau a avut-o inainte si o vinde
	 * profit <0 (e defapt deficit atunci cand micsoreaza pretul la o actiune si o vinde)
	 * la sfarsitul rularii se aduna balanta + sumad din (nr de actiuni * pretul fiecareia) - profit
	 * si trebuie sa iasa la fel ca la inceput => invariant
	 */
	//map firma actiuni
	private HashMap<String,Actiuni> mapActiuni= new HashMap<String,Actiuni>();
	/*private CyclicBarrier barrier;*/
	//private ArrayList<Actiuni> listaActiuni = new ArrayList<Actiuni>();

	/* genereaza balanta random
	 * profit=0
	 * actiuni random de la firme diferite
	 * invariant: persoana nu contine doua seturi de actiuni de la aceeasi firma
	 */
	//nu trebuie barrier, creaza actiuni si creaza oferte se apeleaza in constructor
	//constructorul nu are treaba cu thread-ul
	public Persoana(int uid/*, CyclicBarrier barrier*/)
	{	this.localLogger=new Logger();
		this.uid=uid;
		//this.barrier = barrier;
		random = new Random();
		creazaActiuniRandom();
		creazaOferteRandom();

	}	
	public int getUid()
	{	return uid;
	}
	public void creazaActiuniRandom()
	{	
		String firme[]=Main.getFirme();
		int numarActiuni,pretActiuni;
		boolean flag;
		for (int i=0;i<Main.nrActiuniperPersoana;i++)
		{	
			flag=true;
			pretActiuni=random.nextInt(10)+1; //intre [1,10]
			numarActiuni=random.nextInt(10)+1; //intre [1,10]
			while(flag)
			{	
				int ifirma=random.nextInt(firme.length);
				if(mapActiuni.containsKey(firme[ifirma])==false)
				{	
					mapActiuni.put(firme[ifirma], new Actiuni(firme[ifirma],numarActiuni,pretActiuni));
					flag=false;
				}				
			}			
		}
	}
	public void creazaOferteRandom()
	{	
		Oferta oferta;
		Iterator it = mapActiuni.values().iterator();
		while (it.hasNext())
		{	
			Actiuni a=(Actiuni)it.next();
			int numarActiuni=random.nextInt(a.getNumarActiuni())+1;
			oferta=new Oferta(UUID.randomUUID(),numarActiuni, a.getNumeFirma(), this, a.getPret(), a);
			publicaOferta(oferta);
			localLogger.evenimentPublicareOferta("User " + uid + " firma " 
					+ a.getNumeFirma() + " numar actiuni " + numarActiuni + " cu pretul " + a.getPret());
		}			
	}
	@Override
	public void modificaOferta() 
	{	//poate sa collide modificaOferta cu el insusi
		//lock mapOferta
		int ioferta=random.nextInt(mapOferta.size());
		Iterator it = mapOferta.entrySet().iterator();
		int i=0;
		Oferta oferta=null;
		while(it.hasNext())
		{
			oferta=(Oferta)it.next();

			if(i==ioferta)
			{ 
				break;
			}
			i++;
		}
		if(oferta==null)
		{
			//naspa rau
		}
		else
		{
			synchronized(oferta) {
				if(oferta != null)
					procesator.modificaOferta(oferta, random.nextInt(10)+1, random.nextInt(oferta.getActiuni().getNumarActiuni())+1);
				else
					System.out.println("Oferta e null in Persoana modifica Oferta");
			}

		}
	}

	/* 
	 *
	 */
	@Override
	public void publicaOferta(Oferta oferta) 
	{	
		procesator.adaugaOferta(oferta);
		/* adauga oferta local */
		synchronized (mapOferta) {	
			mapOferta.put(oferta.getNumeFirma(), oferta);
		}
	}

	//apelat de scade numar actiuni
	@Override
	public void stergeOferta(Oferta oferta) {
		//procesator.stergeOferta(oferta); il apeleaza procesatorul
		//threadul procesatorului pe obiectul this persoana

		//lock write mapOferta
		synchronized(mapOferta)
		{
			if(mapOferta.containsKey(oferta))
			{
				mapOferta.remove(oferta);
			}
		}
		//unlock write mapOferta
	}


	@Override
	public void cumparaOferta() 
	{	int i;
		
		String numeFirma=procesator.randomFirma();
		//firmele numai se adauga la bursa, nu se si scot, numeFirma nu are cum sa devina invalid, nu trebuie lock in continuare
		if(numeFirma==null)
		{	System.out.println("naspa cumpara oferta");
			return;
		}
		ListaOferteFirma listaOferteFirma=procesator.getListaOferteFirma(numeFirma);
		Oferta oferta=listaOferteFirma.getOfertaRandom(this);
		if(oferta==null)
		{ //a incercat sa se cumpere singur
			//System.out.println("incearca sa cumpere de la el insusi");
			return;
		}
		//int nrActiuni = random.nextInt(oferta.getNumarActiuni()+1);
		//le cumpara toate care is
		procesator.proceseazaComanda(oferta, this);
	}

	//
	public int getNumarActiuniFirma(String numeFirma)
	{	
		Actiuni a=mapActiuni.get(numeFirma);
		if(a==null)
		{	
			return 0;
		}
		else
		{	
			return a.getNumarActiuni();
		}
	}

	//trebuie gandite lockurile ca pot sa fie mai multe scade NumarActiuni in acelas timp
	@Override
	public synchronized boolean scadeNumarActiuni(int nrActiuni, String numeFirma, int pret) 
	{	
		Actiuni actiuni=mapActiuni.get(numeFirma);
		if(actiuni!=null)
		{	
			int actiuniRamase=actiuni.getNumarActiuni()-nrActiuni;
			if(actiuniRamase>0)
			{	
				actiuni.setNumarActiuni(actiuni.getNumarActiuni()-nrActiuni);
				//daca asta e alt thread decat threadul persoanei care detine balanta atunci o sa se strice treaba cu getbalanta din proceseazacomanda
				//ar trebui sincronizare pe balanta
				//trebuie sincronizare pe balanta fiindca poti sa vinzi la mai multi in acelas timp 
				
				balanta += nrActiuni * pret;
				
				return true;
			}
			localLogger.evenimentVanzare("User " + uid + " actiunile la firma " + numeFirma + 
					" numarul de actiuni " + nrActiuni);
			
			return false;
		}

		return false;
	}


	public Integer getBalanta(){
		//synchronized(balanta) e complet degeaba ca se desincronizeaza dupa ce returneaza si ce ai facut?
		//synchronized (balanta) {
			return balanta;
		//}
	}
	/* netvalue() returneaza balanta + suma(nr_actiuni*pret_actiune)
	 * returneaza int (toate componentele sumei sunt int) sa nu avem problema de precizie la verificarea invariantului de sistem
	 * e apelat cand NU MAI RULEAZA CA THREAD
	 */
	public int getNetvalue()
	{ 	int ret=balanta;
	Iterator it = mapActiuni.values().iterator();
	while (it.hasNext())
	{	
		Actiuni a=(Actiuni)it.next();
		ret+=a.getNumarActiuni()*a.getPret();
	}
	return ret;
	}

	public void run(){

		
		for(int i=0;i<100;i++)
		{	/* fa mai multe chestii random
		 	*/
			switch(random.nextInt(3))
			{	case 0:
				this.cumparaOferta();
				break;	

			case 1:
				this.creazaOferteRandom();
				break;

				//				case 2:
				//				//modifica pret la ofertele la o oferta de a ta
				//				break;
			}
			try {
				sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	}
	@Override
	public void adaugaActiuni(int numarActiuni, String numeFirma, int pret) 
	{

		synchronized (mapActiuni) {
			
			Actiuni actiuni = mapActiuni.get(numeFirma);
			
			if(actiuni == null)
				mapActiuni.put(numeFirma, new Actiuni(numeFirma, numarActiuni, pret));
			else
				actiuni.setNumarActiuni(mapActiuni.get(numeFirma).getNumarActiuni() + numarActiuni);
		}
		
		synchronized (balanta) {
			
			balanta -= pret * numarActiuni;
			localLogger.evenimentCumparare("User " + uid + " actiunile la firma " + numeFirma + 
					" numarul de actiuni " + numarActiuni);
		}

		
	}
	public void afisareLocalLogger() {
		localLogger.printLog();		
	}
	public void generareLogFile() {
		localLogger.writeToFile();
		
	}


}
