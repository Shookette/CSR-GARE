public class Train extends Thread{

	//le quai de la gare ou se dirige le train
	private Quai quai;
	//temps d'arret du train
	private int arret;
	//vitesse du train
	private int vitesse;
	//Nombre de places totales possibles dans le train
	private int capacite;
	//nombre de places encore libres dans le train
	private int nbPlaceLibre;
		
	/**
	 * Constructeur d'un train initialise son quai, sa capacit�, sa vitesse et son temps d'arret et lui indique qu'il est un daemon
	 * @param quai Quai avec lequel le train est li�
	 * @param capacite Capacit� maximum du train (d�finit si le train est petit ou grand/ nombre de si�ges du train)
	 * @param vitesse Vitesse du train
	 * @param arret Temps d'arr�t du train
	 */
	public Train(Quai quai, int capacite, int vitesse, int arret) {
		this.quai = quai;
		this.capacite = capacite;
		this.vitesse = vitesse;
		this.arret = arret;
		//Initialise train comme un thread daemon, le programme s'arrete donc quand il n'y a plus de thread ou plus que des threads daemon
		this.setDaemon(true);
	}
	
	/**
	 * Getter de la capacit� maximum du train
	 * @return capacite du train
	 */
	public int getCapacite() {
		return this.capacite;
	}
	
	/**
	 * Setter du nombre de si�ge libre dans le train
	 * @param nb nombre de places libres restantes dans le train
	 */
	public void setNbPlaceLibre(int nb) {
		this.nbPlaceLibre = nb;
	}
	
	/**
	 * Getter du nombre de si�ge libre dans le train
	 * @return nbplaceLibre nombre de places libres restantes
	 */
	public int getNbPlaceLibre() {
		return this.nbPlaceLibre;
	}
	
	/**
	 * Execution du train avec son entr�e et sa sortie en gare / quai
	 */
	public void run() {
		//Le train arrive et repart de la gare en boucle (fait des aller-retours entre deux gares)
		while(true) {
			//initialise le nombre de places libres du train � sa capacit� 
			//quand un train arrive dans la gare, tout le monde descend
			this.quai.viderTrain(this);
			//Le train met un certain temps pour arriver � la gare
			try {
				Thread.sleep(10000 / this.vitesse);
			} catch(InterruptedException e) {}
			//Le train va chercher une voie ou se gar�
			this.quai.arriverTrain(this);
			//il attend un certain temps en gare (pour laisser les voyageurs monter)
			try {
				Thread.sleep(this.arret);
			} catch(InterruptedException e) {}
			//Le train repart de la gare
			this.quai.partirTrain(this);
		}
	}
}