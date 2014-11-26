public class Train extends Thread{

	private Quai quai;
	private int arret;
	private int vitesse;
	private int capacite;
	private int nbPlaceLibre;
	private int nbVoyageur;
	
	/**
	 * Constructeur d'un train
	 * @param quai comme quai d'arriv�
	 * @param capacite comme capacit� maximal
	 * @param vitesse comme vitesse de transport
	 * @param arret comme temps d'arr�t
	 */
	public Train(Quai quai, int capacite, int vitesse, int arret) {
		this.quai = quai;
		this.capacite = capacite;
		this.vitesse = vitesse;
		this.arret = arret;
		this.setDaemon(true);
	}
	
	/**
	 * Get Capacit�
	 * @return capacit� du train
	 */
	public int getCapacite() {
		return this.capacite;
	}
	
	/**
	 * Get Place achet�
	 * @return le nombre de places achet�es pour ce train
	 */
	public int getPlaceAchetee() {
		return this.getCapacite() - this.getNbPlaceLibre();
	}
	
	/**
	 * Set nbPlaceLibre(int) le nombre de places libres du train
	 * @param nb nombre de places libres
	 */
	public void setNbPlaceLibre(int nb) {
		this.nbPlaceLibre = nb;
	}
	
	/**
	 * Get PlaceLibre
	 * @return le nombre de places libres pour ce train
	 */
	public int getNbPlaceLibre() {
		return this.nbPlaceLibre;
	}
	
	/**
	 * Set nbVoyageur(int) le nombre de voyageurs dans ce train
	 * @param nb nombre de voyageurs du train
	 */
	public void setNbVoyageur(int nb) {
		this.nbVoyageur = nb;
	}
	
	/**
	 * Get NbVoyageur
	 * @return le nombre de voyageurs pour ce train
	 */
	public int getNbVoyageur() {
		return this.nbVoyageur;
	}
	
	/**
	 * Plan d'execution du train
	 * 
	 * 1) arrive en gare en 10000 / VITESSE_TRAIN (en ms) > (comprit entre 50 et 300 km/h)
	 * 2) cherche a se garer sur une voie libre
	 * 3) s'arr�te pendant un temps ARRET_TRAIN
	 * 4) repart
	 */
	public void run() {
		//Permet de faire revenir les trains si tous les voyageurs ne sont pas encore mont�s dans des trains
		while(true) {
			//Initialise le nombre de places libres et le nombre de voyageurs dans le train
			this.quai.viderTrain(this);
			//attends que le train arrive en gare
			try {
				Thread.sleep(10000 / this.vitesse);
			} catch(InterruptedException e) {}
			//Indique au quai que le train est arriv�
			//Afin de pouvoir lui attribuer une voie disponible
			this.quai.arriverTrain(this);
			//Attend le temps d'arr�t d�fini pour le train
			try {
				Thread.sleep(this.arret);
			} catch(InterruptedException e) {}
			//Indique au quai que le train part
			//Afin de bloquer l'achat de billet et d'attendre les derniers voyageurs
			//Puis lib�re la voie
			this.quai.partirTrain(this);
		}
	}
}