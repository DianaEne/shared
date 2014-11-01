
public class Actiuni {

	private int numar;
	private int pret;
	private String numeFirma;
	/* actiunile ar trebui sa aiba pret ca sa se poata calcula netvalue al unei persoane
	 * 
	 */
	public Actiuni(String numeFirma, int numar, int pret){
		this.numeFirma = numeFirma;
		this.numar = numar;
		this.pret = pret;

	}
	
	public int getNumarActiuni() {
		return numar;
	}
	public void setNumarActiuni(int numar) {
		this.numar = numar;
		/*trebuie modificat profitul */
	}
	public void setPret(int pret) //nu are trebuie sa fie public
	{	this.pret= pret;
	}
	public int getPret()
	{	return pret;
	}
	public String getNumeFirma() {
		return numeFirma;
	}

	
	
	
}
