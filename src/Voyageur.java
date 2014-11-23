public class Voyageur extends Thread{

	private Quai quai;
	private Billeterie billeterie;
	
	/**
	 * Constructeur d'un voyageur qui initialise son quai et la billeterie
	 * @param quai
	 * @param billeterie
	 */
	public Voyageur(Quai quai, Billeterie billeterie) {
		this.quai = quai;
		this.billeterie = billeterie;
	}
	

	/*
	 * Voyageur
	 * 1) Achete un billet
	 * 2) Monte dans un train NON PLEINS des que possible
	 */
	/**
	 * Fonction d'execution d'un client.
	 */
	public void run() {
		System.out.println("VOYAGEUR :  "+
				Thread.currentThread().getName()
				+" va acheter un billet");
		billeterie.vendreBillet();
		System.out.println("VOYAGEUR :  "+
				Thread.currentThread().getName()
				+" se dirige pour monter dans son train");
		quai.monterTrain();
		System.out.println("VOYAGEUR :  "+
				Thread.currentThread().getName()
				+" est mont� dans le train");
	}
	
}
