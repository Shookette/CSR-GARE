public class Train extends Thread{

	private Quai quai;
	private int arret;
	private int vitesse;
	private int capacite;
	private int nbPlaceLibre;
	
	/*
	 * 1) arrive en gare en 10000 / VITESSE_TRAIN (en ms) > (comprit entre 50 et 300 km/h)
	 * 2) cherche a se garer sur une voie libre
	 * 3) s'arr�te pendant un temps ARRET_TRAIN
	 * 4) repart
	 */
	
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
		this.setDaemon(true);
	}
	
	/**
	 * Getter de la capacit� maximum du train
	 * @return
	 */
	public int getCapacite() {
		return this.capacite;
	}
	
	/**
	 * Setter du nombre de si�ge libre dans le train
	 * @param nb
	 */
	public void setNbPlaceLibre(int nb) {
		this.nbPlaceLibre = nb;
	}
	
	/**
	 * Getter du nombre de si�ge libre dans le train
	 * @return
	 */
	public int getNbPlaceLibre() {
		return this.nbPlaceLibre;
	}
	
	/**
	 * Execution du train avec son entr�e et sa sortie en gare / quai
	 */
	public void run() {
		while(true) {
			this.quai.viderTrain(this);
//			System.out.println("Temps d'arriv�e : "+10000 / this.vitesse +" le train roule � : "+this.vitesse);
			try {
				Thread.sleep(10000 / this.vitesse);
			} catch(InterruptedException e) {}
//			System.out.println("TRAIN :  "+
//					Thread.currentThread().getName()
//					+" arrive");
			this.quai.arriverTrain(this);
//			System.out.println("TRAIN :  "+
//					Thread.currentThread().getName()
//					+" se pr�pare � partir");
			try {
				Thread.sleep(this.arret);
			} catch(InterruptedException e) {}
//			System.out.println("TRAIN :  "+
//					Thread.currentThread().getName()
//					+" va partir");
			this.quai.partirTrain(this);
		}
	}
}
