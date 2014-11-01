import java.util.UUID;

/* trebuie lock sa nu se modifice pretul si cineva sa cumpere in acelas timp, trebuie un mutex */

public class Oferta {
	public UUID idOferta;
	private int numarActiuni;
	private String numeFirma;
	private Persoana owner;
	private int pretActiune;
	private Actiuni actiuni;
	
	public Oferta(UUID idOferta, int numarActiuni, String numeFirma, Persoana owner, int pretActiune,Actiuni actiuni) {
		this.idOferta = idOferta;
		this.numarActiuni = numarActiuni;
		this.numeFirma = numeFirma;
		this.owner = owner;
		this.pretActiune = pretActiune;
		this.actiuni=actiuni;
	}

	public UUID getIdOferta() {
		return idOferta;
	}

	public void setIdOferta(UUID idOferta) {
		this.idOferta = idOferta;
	}
	public Actiuni getActiuni()
	{	return actiuni;
	}
	public int getNumarActiuni() {
		return numarActiuni;
	}

	public void setNumarActiuni(int numarActiuni) {
		this.numarActiuni = numarActiuni;
	}

	public String getNumeFirma() {
		return numeFirma;
	}

	public void setNumeFirma(String numeFirma) {
		this.numeFirma = numeFirma;
	}

	public Persoana getOwner() {
		return owner;
	}

	public void setOwner(Persoana owner) {
		this.owner = owner;
	}

	public int getPretActiune() {
		return pretActiune;
	}

	public void setPretActiune(int pretActiune) {
		this.pretActiune = pretActiune;
	}
	
	
	
}
