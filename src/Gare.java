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
	static final int NB_GUICHET = 1;
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
	 * Fonction de création d'un voyageur.
	 * @return true si la création s'est bien passée
	 */
	private boolean nouveauVoyageur() {		
		//On continue de créer des voyageurs seulement si l'on n'a pas atteint le nombre de voyageurs voulu
		if(nbVoyageurs == NB_VOYAGEURS) {
			System.out.println("Le nombre maximum de voyageurs est atteint.");
			return false;
		}
		
		//Initialisation du voyageur
		voyageurs[nbVoyageurs] = new Voyageur(quai, billeterie);
		
		nbVoyageurs++;
		return true;
	}
	
	/**
	 * Fonction de création d'un train
	 * @return true si la création s'est bien passée
	 */
	private boolean nouveauTrain() {
		//On continue de créer des trains seulement si l'on n'a pas atteint le nombre de trains voulu
		if(nbTrains == NB_TRAINS) {
			System.out.println("Le nombre maximum de trains est atteint");
			return false;
		}
		
		//Gère la vitesse et la capacité du train avec un random
		Random rand = new Random();
		int vitesse = Math.abs(rand.nextInt((300 - 50) + 1) + 50);
		int capacite = Math.abs(rand.nextInt(CAPACITE_TRAIN));
		trains[nbTrains] = new Train(quai, capacite, vitesse, ARRET_TRAIN);
		
		nbTrains++;
		return true;
	}
	
	/**
	 * Constructeur d'une gare (initialise la billeterie, le quai, les trains et les voyageurs)
	 */
	public Gare() {
		
		//Création quai (avec NB_VOIES voies)
		this.quai = new Quai(NB_VOIES);
		//Création billeterie (avec NB_GUICHET guichets)
		this.billeterie = new Billeterie(NB_GUICHET);
		
		//On créé les voyageurs
		while(nouveauVoyageur());
		//On créé les trains
		while(nouveauTrain());
		

		//On lance les trains
		int i = 0;
		for(i = 0;i < nbTrains; i++) {
			trains[i].start();
		}

		//On lance les voyageurs toutes les 10 ms
		i = 0;
		for(i = 0;i < nbVoyageurs; i++){
			voyageurs[i].start();
			try{
				Thread.sleep(10);
			} catch(InterruptedException e){}
		}
	}
	
	/**
	 * Fonction main du programme, créé et execute une gare.
	 * @param args
	 */
	public static void main(String[] args) {
		new Gare();		
	}
}