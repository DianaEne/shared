import java.util.concurrent.CyclicBarrier;


public class Main {
	public static Persoana persoane[];
	public static String firme[];
	public final static int nrActiuniperPersoana=10; //TREBUIE SA FIE <= DECAT NUMARUL DE FIRME
	public final static int nrPersoane=100;
	public static void main(String args[])
	{	int nrFirme=10;
		firme = new String[nrFirme];
		int initialNetValueSystem;
		int finalNetValueSystem;
		
		//creating CyclicBarrier with 3 parties i.e. 3 Threads needs to call await() �� � � 
			//CyclicBarrier cb = new CyclicBarrier(nrPersoane);
		
		Thread thread[]=new Thread[nrPersoane];
		persoane=new Persoana[nrPersoane];
		
		for (int i=0;i<nrFirme;i++)
		{	
			firme[i]=i+"";
		}
		long startTime = System.currentTimeMillis();
		for(int i=0;i<nrPersoane;i++)
		{	persoane[i]=new Persoana(i/*,cb*/);
			thread[i]=new Thread(persoane[i]);
			thread[i].start();
		}		
		for(int i=0;i<nrPersoane;i++)
		{
			try {
				thread[i].join();
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
	    System.out.println(elapsedTime+"ms");
		for(int i=0;i<nrPersoane;i++)
		{
			persoane[i].generareLogFile();
		}
		//finalNetValueSystem=computeSystemValue(persoane);
//		if(initialNetValueSystem==finalNetValueSystem)
//		{
//			System.out.println("The system may be correct");
//		}
//		else
//		{	System.out.println("The system has concurrency issues or other errors");
//		}
	}
	
//	static int computeSystemValue(Persoana persoane[])
//	{	int ret=0;
//		for(int i=0;i<persoane.length;i++)
//		{	ret+=persoane[i].getNetvalue()-persoane[i].getProfit();			
//		}
//		return ret;
//	}
	static String[] getFirme()
	{	return firme;
	}
	static Persoana[] getPersoane()
	{	return persoane;
	}
}
