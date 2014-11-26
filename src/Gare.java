import java.util.Random;

public class Gare {
   /*
	* Constantes
	*/
	//Nombre de trains � cr�er
	static final int NB_TRAINS = 5;
	//Nombre de voyageurs � cr�er
	static final int NB_VOYAGEURS = 1000;
	//Nombre de voies sur le quai
	static final int NB_VOIES = 2;
	//Nombre de guichets dans la billeterie
	static final int NB_GUICHET = 2;
	//Capacit� maximum du plus grand train
	static final int CAPACITE_TRAIN = 100;
	//Temps d'arret en gare des trains
	static final int ARRET_TRAIN = 1000;
	
	/*
	* Variables
	*/
	//Tableau contenant tous les trains cr��s
	private Train[] trains = new Train[NB_TRAINS];
	//Tableau contenant tous les voyageurs cr��s
	private Voyageur[] voyageurs = new Voyageur[NB_VOYAGEURS];
	//Cr�ation d'une billeterie pour la gare
	private Billeterie billeterie;
	//Cr�ation d'un quai pour la gare
	private Quai quai;
	//Nombre de voyageurs cr��s
	private int nbVoyageurs = 0;
	//Nombre de trains cr��s
	private int nbTrains = 0;
	
	/**
	 * Cr�ation d'un voyageur
	 * @return true si la cr�ation s'est bien pass�e
	 */
	private boolean nouveauVoyageur() {
		
		//Si le nombre de voyageurs a atteint le nombre total de voyageurs alors on arrete d'en cr�er
		if(nbVoyageurs == NB_VOYAGEURS) {
			System.out.println("Le nombre maximum de voyageurs est atteint.");
			return false;
		}
		
		//Sinon on cr�� un nouveau voyageur
		voyageurs[nbVoyageurs] = new Voyageur(quai, billeterie);
		nbVoyageurs++;
		return true;
	}
	
	/**
	 * Cr�ation d'un train
	 * @return true si la cr�ation s'est bien pass�e
	 */
	private boolean nouveauTrain() {
		
		//Si le nombre de trains a atteint le nombre total de trains alors on arrete d'en cr�er
		if(nbTrains == NB_TRAINS) {
			System.out.println("Le nombre maximum de trains est atteint.");
			return false;
		}
		
		//Sinon on cr�� un nouveau train
		//Avec une capacit� et une vitesse de transport al�atoires
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

		//Cr�ation billeterie (int nbguichet)
		this.billeterie = new Billeterie(NB_GUICHET, NB_VOIES);
		
		//Cr�ation quai (int nbvoie)
		this.quai = new Quai(NB_VOIES, this.billeterie);
	
		//Cr�ation de tous les voyageurs
		while(nouveauVoyageur());
		
		//Cr�ation de tous les trains
		while(nouveauTrain());
			
		//D�marage de tous les threads de trains
		for(int i = 0;i < nbTrains; i++) {
			trains[i].start();
		}
		
		//D�marage de tous les threads voyageurs avec un temps d'attente de 10ms entre chaque voyageur
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