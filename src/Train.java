public class Train extends Thread{

	private Quai quai;
	private int arret;
	private int vitesse;
	private int capacite;
	private int nbPlaceLibre;
	private int nbVoyageur;
	
	/**
	 * Constructeur d'un train
	 * @param quai comme quai d'arrivé
	 * @param capacite comme capacité maximal
	 * @param vitesse comme vitesse de transport
	 * @param arret comme temps d'arrêt
	 */
	public Train(Quai quai, int capacite, int vitesse, int arret) {
		this.quai = quai;
		this.capacite = capacite;
		this.vitesse = vitesse;
		this.arret = arret;
		this.setDaemon(true);
	}
	
	/**
	 * Get Capacité
	 * @return capacité du train
	 */
	public int getCapacite() {
		return this.capacite;
	}
	
	/**
	 * Get Place acheté
	 * @return le nombre de place acheté pour ce train
	 */
	public int getPlaceAchetee() {
		return this.getCapacite() - this.getNbPlaceLibre();
	}
	
	/**
	 * Set nbPlaceLibre(int) le nombre de place libre du train
	 * @param nb nombre de place libre
	 */
	public void setNbPlaceLibre(int nb) {
		this.nbPlaceLibre = nb;
	}
	
	/**
	 * Get PlaceLibre
	 * @return le nombre de place libre pour ce train
	 */
	public int getNbPlaceLibre() {
		return this.nbPlaceLibre;
	}
	
	/**
	 * Set nbVoyageur(int) le nombre de voyageur dans ce train
	 * @param nb nombre de voyageur du train
	 */
	public void setNbVoyageur(int nb) {
		this.nbVoyageur = nb;
	}
	
	/**
	 * Get NbVoyageur
	 * @return le nombre de voyageur pour ce train
	 */
	public int getNbVoyageur() {
		return this.nbVoyageur;
	}
	
	/**
	 * Plan d'execution du train
	 * 
	 * 1) arrive en gare en 10000 / VITESSE_TRAIN (en ms) > (comprit entre 50 et 300 km/h)
	 * 2) cherche a se garer sur une voie libre
	 * 3) s'arrête pendant un temps ARRET_TRAIN
	 * 4) repart
	 */
	public void run() {
		//Permet de faire revenir les trains si tout les voyageurs ne sont pas encore partie
		while(true) {
			//Initialise le nombre de place libre et le nombre de voyageur dans le train
			this.quai.viderTrain(this);
			//Attente le temps d'arriver du train dans la gare
			try {
				Thread.sleep(10000 / this.vitesse);
			} catch(InterruptedException e) {}
			//Indique au quai que le train est arrivé
			//Afin de pouvoir lui attribuer une voie disponible
			this.quai.arriverTrain(this);
			//Attend le temps d'arrêt définie pour le train
			try {
				Thread.sleep(this.arret);
			} catch(InterruptedException e) {}
			//Indique au quai que le train part
			//Afin de bloquer l'achat de billet et d'attendre les derniers voyageurs
			//Puis libère la voie
			this.quai.partirTrain(this);
		}
	}
}
