public class Voyageur extends Thread{

	private Quai quai;
	private Billeterie billeterie;
	
	public Voyageur(Quai quai, Billeterie billeterie) {
		this.quai = quai;
		this.billeterie = billeterie;
	}
	

	/*
	 * Voyageur
	 * 1) Achete un billet
	 * 2) Monte dans un train NON PLEINS des que possible
	 */
	public void run() {
		int voie = 0;
		System.out.println("VOYAGEUR :  "+
				Thread.currentThread().getName()
				+" va acheter un billet");
		voie = billeterie.vendreBillet();
		System.out.println("VOYAGEUR :  "+
				Thread.currentThread().getName()
				+" se dirige pour monter dans son train");
		quai.monterTrain(voie);
		System.out.println("VOYAGEUR :  "+
				Thread.currentThread().getName()
				+" est monté dans le train");
	}
	
}
