public class Voyageur extends Thread{

	private Quai quai;
	private Billeterie billeterie;
	
	/**
	 * Constructeur du voyageur
	 * @param quai
	 * @param billeterie
	 */
	public Voyageur(Quai quai, Billeterie billeterie) {
		this.quai = quai;
		this.billeterie = billeterie;
	}

	/**
	 * Plan d'execution
	 * Voyageur
	 * 1) Achete un billet
	 * 2) Monte dans un train NON PLEINS des que possible
	 */
	public void run() {
		//Va acheter un billet puis récupére la voie associé au billet acheté
		int voie = billeterie.vendreBillet();
		//Monte dans le train sur la voie indiquée sur le billet
		quai.monterTrain(voie);
	}
	
}
