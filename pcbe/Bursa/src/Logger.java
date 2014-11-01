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
	
	public ArrayList<String> getEvenimente(){
		return evenimente;
	}
	public void printLog() {
		for(String s:evenimente)
		{	System.out.println(s);
		}		
	}
	public void writeToFile()
	{	for(String s:evenimente)
		{	try {
			out.write(s+"\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	public void printToFile()
	{	
		
	}
	private void adaugaEveniment(String tip,String mesaj)
	{evenimente.add(tip+"| "+mesaj);
	}
	public void evenimentCumparare(String mesaj)
	{
		adaugaEveniment("cumparare ",mesaj);
	}
	public void evenimentVanzare(String mesaj)
	{
		adaugaEveniment("vanzare   ",mesaj);
	}
	public void evenimentPublicareOferta(String mesaj) {
		// TODO Auto-generated method stub
		adaugaEveniment("publicare ",mesaj);
	}
	public void evenimentBalantaInsuficienta(String mesaj)
	{
		adaugaEveniment("balanta insuficienta ",mesaj);
	}
	
}
