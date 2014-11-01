
public interface Vanzator {
	public void modificaOferta();
	public void publicaOferta(Oferta oferta);
	public void stergeOferta(Oferta oferta);
	public int getNumarActiuniFirma(String numeFirma);
	public boolean scadeNumarActiuni(int nrActiuni,String numeFirma,int pret);
}
