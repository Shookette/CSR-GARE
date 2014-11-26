import java.util.Random;

public class Gare {
	
	//Constantes
	static final int NB_TRAINS = 3;
	static final int NB_VOYAGEURS = 25;
	static final int NB_VOIES = 2;
	static final int NB_GUICHET = 2;
	static final int CAPACITE_TRAIN = 20;
	static final int ARRET_TRAIN = 1000;
	
	//Variables
	private Train[] trains = new Train[NB_TRAINS];
	private Voyageur[] voyageurs = new Voyageur[NB_VOYAGEURS];
	private Billeterie billeterie;
	private Quai quai;
	private int nbVoyageurs = 0;
	private int nbTrains = 0;
	
	/**
	 * Cr�ation d'un voyageur
	 * @return
	 */
	private boolean nouveauVoyageur() {
		
		//Si le nombre de voyageurs � atteint le nombre total de voyageurs alors on n'en cr�er pas de nouveau
		if(nbVoyageurs == NB_VOYAGEURS) {
			System.out.println("Le nombre maximum de voyageurs est atteint.");
			return false;
		}
		
		//Sinon onn cr�er un nouveau voyageur
		voyageurs[nbVoyageurs] = new Voyageur(quai, billeterie);
		nbVoyageurs++;
		return true;
	}
	
	/**
	 * Cr�ation d'un train
	 * @return
	 */
	private boolean nouveauTrain() {
		
		//Si le nombre de trains � atteint le nombre total de trains alors on n'en cr�er pas de nouveau
		if(nbTrains == NB_TRAINS) {
			System.out.println("Le nombre maximum de trains est atteint.");
			return false;
		}
		
		//Sinon on cr�er un nouveau train
		//Avec une capacit� et une vitesse de transport al�atoire
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
		
		//D�marage de tous les threads voyageurs avec un temps d'attente de 10s entre chaque voyageur
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
