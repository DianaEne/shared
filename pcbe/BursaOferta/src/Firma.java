import java.util.Random;


public class Firma {

	private int nrOferteTip0;
	private int nrOferteTip1;
	private int idFirma;
	private Oferta oferteTip0[];
	private Oferta oferteTip1[];
	private boolean disponibilitateOferteTip0[]; //true daca oferta nu a fost atribuita inca
	private boolean disponibilitateOferteTip1[]; //false daca oferta a fost distribuita
	private int tip0 = 0;
	private int tip1 = 1;
	
	public Firma(int idFirma, int nrOferte)
	{
		this.nrOferteTip0 = nrOferte;
		this.nrOferteTip1 = nrOferte;
		this.idFirma = idFirma;
		oferteTip0 = new Oferta[this.nrOferteTip0];
		oferteTip1 = new Oferta[this.nrOferteTip1];
		disponibilitateOferteTip0 = new boolean[this.nrOferteTip0];
		disponibilitateOferteTip1 = new boolean[this.nrOferteTip1];
		int nrActiuni;
		int pretPerActiune;
		Random r = new Random();
		for(int i = 0; i < nrOferteTip0; i++)
		{
			nrActiuni = r.nextInt(100) + 1;
		    pretPerActiune = r.nextInt(10) + 1;
			oferteTip0[i] = new Oferta(tip0, nrActiuni, pretPerActiune, idFirma);
			oferteTip1[i] = new Oferta(tip1, nrActiuni, pretPerActiune, idFirma);
			disponibilitateOferteTip0[i] = true;
			disponibilitateOferteTip1[i] = true;
		}
	}
	
	public Oferta getOfertaRandom(int tip, int sumaDisponibila) 
	{		
		if(tip == 0) // oferta cumparare
		{
			for(int i = 0; i < nrOferteTip0; i++)
				if(disponibilitateOferteTip0[i] == true)
					if(oferteTip0[i].getValoare() <= sumaDisponibila)
					{
						disponibilitateOferteTip0[i] = false;
						return oferteTip0[i];
					}
		}
		else //oferta vanzare
		{
			for(int i = 0; i < nrOferteTip1; i++)
				if(disponibilitateOferteTip1[i] == true)
				{
					disponibilitateOferteTip1[i] = false;
					return oferteTip1[i];
				}
		}
		return null;
	}
	
	public Oferta CautaOferta(Oferta o)
	{
		if(o.getTip() == 0) //persoana cauta oferta de tip0 si are nevoie sa primeasca perechea ei de tip1 (oferte identice la care obligatoriu difera tipul)
		{
			synchronized (oferteTip1)
			{
				for(int i = 0; i < nrOferteTip1; i++)
					if(!disponibilitateOferteTip1[i]) // a fost deja alocata catre o peroana
					{
						System.out.println("zzzzzzzzz");
						if((oferteTip1[i].getNumarActiuni() == o.getNumarActiuni()) && (oferteTip1[i].getValoare() == o.getValoare()))
							if(oferteTip1[i].getStare())
							{
								oferteTip1[i].Dezactivare();
								return oferteTip1[i];
							}
					}
			}
		}
		else 
		{
			synchronized (oferteTip1){
			for(int i = 0; i < nrOferteTip0; i++)
				if(!disponibilitateOferteTip0[i]) 
					if((oferteTip0[i].getNumarActiuni() == o.getNumarActiuni()) && (oferteTip0[i].getValoare() == o.getValoare()))
						if(oferteTip0[i].getStare())
						{
							oferteTip0[i].Dezactivare();
							return oferteTip0[i];
						}
			}
		}
		
		return null;	
	}
	
	public boolean maiAre(int tip)
	{
		if(tip == 0)
		{
			for(int i = 0; i < nrOferteTip0; i++)
				if(disponibilitateOferteTip0[i])
					return true;
		}
		
		if(tip == 1)
		{
			for(int i = 0; i < nrOferteTip1; i++)
				if(disponibilitateOferteTip1[i])
					return true;
		}
		return false;
	}

}
