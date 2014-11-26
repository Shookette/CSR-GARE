public class Voyageur extends Thread{

	//quai ou les voyageurs vont attendre un train
	private Quai quai;
	//Billeterie qui vend les billets aux voyageurs
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
	
	/**
	 * Fonction d'execution d'un client.
	 */
	public void run() {
		//le voyageur va acheter un billet dans la billeterie
		billeterie.vendreBillet();
		//Il monte dans un train qui a encore des places sur le quai
		quai.monterTrain();
	}	
}