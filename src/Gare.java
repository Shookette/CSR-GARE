import java.util.Random;

public class Gare {
   /*
	* Constantes
	*/
	//Nombre de trains à créer
	static final int NB_TRAINS = 5;
	//Nombre de voyageurs à créer
	static final int NB_VOYAGEURS = 1000;
	//Nombre de voies sur le quai
	static final int NB_VOIES = 2;
	//Nombre de guichets dans la billeterie
	static final int NB_GUICHET = 2;
	//Capacité maximum du plus grand train
	static final int CAPACITE_TRAIN = 100;
	//Temps d'arret en gare des trains
	static final int ARRET_TRAIN = 1000;
	
	/*
	* Variables
	*/
	//Tableau contenant tous les trains créés
	private Train[] trains = new Train[NB_TRAINS];
	//Tableau contenant tous les voyageurs créés
	private Voyageur[] voyageurs = new Voyageur[NB_VOYAGEURS];
	//Création d'une billeterie pour la gare
	private Billeterie billeterie;
	//Création d'un quai pour la gare
	private Quai quai;
	//Nombre de voyageurs créés
	private int nbVoyageurs = 0;
	//Nombre de trains créés
	private int nbTrains = 0;
	
	/**
	 * Création d'un voyageur
	 * @return true si la création s'est bien passée
	 */
	private boolean nouveauVoyageur() {
		
		//Si le nombre de voyageurs a atteint le nombre total de voyageurs alors on arrete d'en créer
		if(nbVoyageurs == NB_VOYAGEURS) {
			System.out.println("Le nombre maximum de voyageurs est atteint.");
			return false;
		}
		
		//Sinon on créé un nouveau voyageur
		voyageurs[nbVoyageurs] = new Voyageur(quai, billeterie);
		nbVoyageurs++;
		return true;
	}
	
	/**
	 * Création d'un train
	 * @return true si la création s'est bien passée
	 */
	private boolean nouveauTrain() {
		
		//Si le nombre de trains a atteint le nombre total de trains alors on arrete d'en créer
		if(nbTrains == NB_TRAINS) {
			System.out.println("Le nombre maximum de trains est atteint.");
			return false;
		}
		
		//Sinon on créé un nouveau train
		//Avec une capacité et une vitesse de transport aléatoires
		Random rand = new Random();
		int vitesse = Math.abs(rand.nextInt((300 - 50) + 1) + 50);
		int capacite = Math.abs(rand.nextInt(CAPACITE_TRAIN));
		trains[nbTrains] = new Train(quai, capacite, vitesse, ARRET_TRAIN);
		nbTrains++;
		return true;
	}
	
	/**
	 * 	Initialisation de la gare
	 */
	public Gare() {

		//Création billeterie (int nbguichet)
		this.billeterie = new Billeterie(NB_GUICHET, NB_VOIES);
		
		//Création quai (int nbvoie)
		this.quai = new Quai(NB_VOIES, this.billeterie);
	
		//Création de tous les voyageurs
		while(nouveauVoyageur());
		
		//Création de tous les trains
		while(nouveauTrain());
			
		//Démarage de tous les threads de trains
		for(int i = 0;i < nbTrains; i++) {
			trains[i].start();
		}
		
		//Démarage de tous les threads voyageurs avec un temps d'attente de 10ms entre chaque voyageur
		for(int i = 0;i < nbVoyageurs; i++){
			voyageurs[i].start();
			try{
				Thread.sleep(10);
			} catch(InterruptedException e){}
		}
	}
	
	//Fonction de lancement d'une gare (main).
	public static void main(String[] args) {
		new Gare();		
	}
}