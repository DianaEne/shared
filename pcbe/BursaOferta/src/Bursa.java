
public class Bursa {
	private static Bursa instance;
	
	private Bursa(){
		
	}
	
	public static Bursa getInstance(){
		if(instance == null){
			instance = new Bursa();
		}
		return instance; 
	}

	public String randomFirma() {
		// TODO Auto-generated method stub
		return null;
	}

	public Firma getFirma() {
		// TODO Auto-generated method stub
		return null;
	}

	public void proceseazaComanda(Oferta oferta, Persoana persoana) {
		// TODO Auto-generated method stub
		
	}
}
