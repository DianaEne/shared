public class Main {
	public static Persoana persoane[];
	private static int nrPersoane = 100;
	private static Bursa b;
	private static int nrFirme = 10;
	private static int nrOfertePerFirma = 10;
	
	public static void main(String args[])
	{
		
		b = Bursa.getInstance(nrFirme, nrOfertePerFirma);
		Thread thread[] = new Thread[nrPersoane ];
		persoane = new Persoana[nrPersoane];
		for(int i = 0; i < nrPersoane; i++)
		{	
			System.out.println("aaaaaaaa");
			persoane[i] = new Persoana(i);
			System.out.println("bbb");
			thread[i] = new Thread(persoane[i]);
			//thread[i].start();
		}	
		
		for(int i = 0; i < 100; i++)
			thread[i].start();
		
		//for(int i = 0; i < nrPersoane; i++)
		//{
//			try {
//				//thread[i].join();
//				
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		//}
		
		for(int i = 0; i < nrPersoane; i++)
			persoane[i].afisareLocalLogger();
	}

}
