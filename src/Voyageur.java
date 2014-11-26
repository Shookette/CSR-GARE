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
		//Va acheter un billet puis r�cup�re la voie associ� au billet achet�
		int voie = billeterie.vendreBillet();
		//Monte dans le train sur la voie indiqu�e sur le billet
		quai.monterTrain(voie);
	}
	
}
