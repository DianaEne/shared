import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class Logger {
	private static BufferedWriter out=null;
	private ArrayList<String> evenimente = new ArrayList<String>();
	
	public Logger()
	{
		if(out==null)
		{
			FileWriter fstream=null;
			try {
				fstream = new FileWriter("log.txt");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			out = new BufferedWriter(fstream);
		}
	}

	public void printLog() 
	{
		for(String s:evenimente)
		{	
			System.out.println(s);
		}		
	}
	
	private void adaugaEveniment(String tip,String mesaj)
	{
		evenimente.add(tip+"| "+mesaj);
	}
	
	public void TranzactieCumparare(int uid, Oferta o)
	{
		String cumparator = new Integer(uid).toString();
		String vanzator = new Integer(o.getUserId()).toString();
		String nrActiuni = new Integer(o.getNumarActiuni()).toString();
		String valoare = new Integer(o.getValoare()).toString();
		String firma = new Integer(o.getFirma()).toString();
		
		String mesaj = cumparator + " a cumparat de la " + vanzator + " un numar de " 
		+ nrActiuni + " in valoare de " + valoare + " la firma " + firma;
		adaugaEveniment("Cumparare", mesaj);
		
	}

	public void TranzactieVanzare(int uid, Oferta o) {
		String cumparator = new Integer(uid).toString();
		String vanzator = new Integer(o.getUserId()).toString();
		String nrActiuni = new Integer(o.getNumarActiuni()).toString();
		String valoare = new Integer(o.getValoare()).toString();
		String firma = new Integer(o.getFirma()).toString();
		
		String mesaj = vanzator + " a vandut lui " + cumparator + " un numar de " 
		+ nrActiuni + " in valoare de " + valoare + " la firma " + firma;
		adaugaEveniment("Vanzare  ", mesaj);
		
	}

}
