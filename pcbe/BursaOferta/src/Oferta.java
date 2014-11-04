
public class Oferta {

	private int userId;
	private int tip; //0=cumparare 1=vanzare
	private int nrActiuni;
	private int pretPerActiune;
	private int valoareOferta;
	private int firmaId;
	private boolean activa;
	
	public Oferta(int tip, int nrActiuni, int pretPerActiune, int firmaId)
	{
		this.tip = tip;
		this.nrActiuni = nrActiuni;
		this.pretPerActiune = pretPerActiune;
		this.valoareOferta = nrActiuni * pretPerActiune;
		this.firmaId = firmaId;
		this.activa = false;
	}
	
	public int getNumarActiuni() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setUserId(int userId)
	{
		this.userId = userId;
		this.Activare();
	}
	
	public void Activare()
	{
		this.activa = true;
	}
	
	public void Dezactivare()
	{
		this.activa = false;
	}
	
	public int getValoare()
	{
		return valoareOferta;
	}
	
	public boolean getStare()
	{
		return activa;
	}
	
	public int getTip()
	{
		return tip;
	}
	
	public int getFirma()
	{
		return this.firmaId;
	}
	
	public int getUserId()
	{
		return userId;
	}
}
