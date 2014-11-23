public class Train extends Thread{

	private Quai quai;
	private int arret;
	private int vitesse;
	private int capacite;
	private int nbPlaceLibre;
	private int nbVoyageur;
	
	/*
	 * 1) arrive en gare en 10000 / VITESSE_TRAIN (en ms) > (comprit entre 50 et 300 km/h)
	 * 2) cherche a se garer sur une voie libre
	 * 3) s'arrête pendant un temps ARRET_TRAIN
	 * 4) repart
	 */
	
	public Train(Quai quai, int capacite, int vitesse, int arret) {
		this.quai = quai;
		this.capacite = capacite;
		this.vitesse = vitesse;
		this.arret = arret;
		this.setDaemon(true);
	}
	
	public int getCapacite() {
		return this.capacite;
	}
	
	public void setNbPlaceLibre(int nb) {
		this.nbPlaceLibre = nb;
	}
	
	public int getNbPlaceLibre() {
		return this.nbPlaceLibre;
	}
	
	public void setNbVoyageur(int nb) {
		this.nbVoyageur = nb;
	}
	
	public int getNbVoyageur() {
		return this.nbVoyageur;
	}
	
	public void run() {
		while(true) {
			this.quai.viderTrain(this);
			try {
				Thread.sleep(10000 / this.vitesse);
			} catch(InterruptedException e) {}
//			System.out.println("TRAIN :  "+
//					Thread.currentThread().getName()
//					+" arrive");
			this.quai.arriverTrain(this);
//			System.out.println("TRAIN :  "+
//					Thread.currentThread().getName()
//					+" se prépare à partir");
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
