import java.util.Random;

public class Gare {
	
	//Constantes
	static final int NB_TRAINS = 5;
	static final int NB_VOYAGEURS = 1000;
	static final int NB_VOIES = 2;
	static final int NB_GUICHET = 1;
	static final int CAPACITE_TRAIN = 100;
	static final int ARRET_TRAIN = 1000;
	
	//Variables
	private Train[] trains = new Train[NB_TRAINS];
	private Voyageur[] voyageurs = new Voyageur[NB_VOYAGEURS];
	private Billeterie billeterie;
	private Quai quai;
	private int nbVoyageurs = 0;
	private int nbTrains = 0;
	
	/**
	 * Fonction de création d'un voyageur.
	 * @return true si la création s'est bien passé
	 */
	private boolean nouveauVoyageur() {
		
		if(nbVoyageurs == NB_VOYAGEURS) {
//			System.out.println("Le nombre maximum de voyageurs est atteint.");
			return false;
		}
		
		voyageurs[nbVoyageurs] = new Voyageur(quai, billeterie);
//		System.out.println("voyageur : "+nbVoyageurs+" init");
		
		nbVoyageurs++;
		return true;
	}
	
	/**
	 * Fonction de création d'un train
	 * @return true si la création s'est bien passé
	 */
	private boolean nouveauTrain() {
		
		if(nbTrains == NB_TRAINS) {
//			System.out.println("Le nombre maximum de trains est atteint");
			return false;
		}
		
//		Gère la vitesse et la capacité avec un random
		Random rand = new Random();
		int vitesse = Math.abs(rand.nextInt((300 - 50) + 1) + 50);
		int capacite = Math.abs(rand.nextInt(CAPACITE_TRAIN));
		trains[nbTrains] = new Train(quai, capacite, vitesse, ARRET_TRAIN);
		
		nbTrains++;
		return true;
	}
	
	/**
	 * Constructeur d'une gare (initialise la billeterie, le quai, les trains et les voyageurs
	 */
	public Gare() {
		
//		Création quai (int nbvoie)
		this.quai = new Quai(NB_VOIES);
//		Création billeterie (int nbguichet)
		this.billeterie = new Billeterie(NB_GUICHET);
	
		int i = 0;
		
		while(nouveauVoyageur());
		
		while(nouveauTrain());
			
		for(i = 0;i < nbTrains; i++) {
			trains[i].start();
		}
		
		i = 0;
		for(i = 0;i < nbVoyageurs; i++){
			voyageurs[i].start();
			try{
				Thread.sleep(10);
			} catch(InterruptedException e){}
		}
	}
	
	/**
	 * Fonction main du programme, créer et execute une gare.
	 * @param args
	 */
	public static void main(String[] args) {

		new Gare();
		
	}
}
