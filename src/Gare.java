import java.util.Random;

public class Gare {
	
	//Constantes
	static final int NB_TRAINS = 2;
	static final int NB_VOYAGEURS = 10;
	static final int NB_VOIES = 1;
	static final int NB_GUICHET = 5;
	static final int CAPACITE_TRAIN = 50;
	static final int ARRET_TRAIN = 1000;
	
	//Variables
	private Train[] trains = new Train[NB_TRAINS];
	private Voyageur[] voyageurs = new Voyageur[NB_VOYAGEURS];
	private Billeterie billeterie;
	private Quai quai;
	private int nbVoyageurs = 0;
	private int nbTrains = 0;
	
	
	private boolean nouveauVoyageur() {
		
		if(nbVoyageurs == NB_VOYAGEURS) {
			System.out.println("Le nombre maximum de voyageurs est atteint.");
			return false;
		}
		
		voyageurs[nbVoyageurs] = new Voyageur(quai, billeterie);
		nbVoyageurs++;
		return true;
	}
	
	private boolean nouveauTrain() {
		
		if(nbTrains == NB_TRAINS) {
			System.out.println("Le nombre maximum de trains est atteint.");
			return false;
		}
		
		Random rand = new Random();
		int vitesse = Math.abs(rand.nextInt((300 - 50) + 1) + 50);
		int capacite = Math.abs(rand.nextInt(CAPACITE_TRAIN));
		trains[nbTrains] = new Train(quai, capacite, vitesse, ARRET_TRAIN);
		nbTrains++;
		return true;
	}
	
	public Gare() {

		//Création billeterie (int nbguichet)
		this.billeterie = new Billeterie(NB_GUICHET, NB_VOIES);
		
		//Création quai (int nbvoie)
		this.quai = new Quai(NB_VOIES, this.billeterie);
	
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
	
	public static void main(String[] args) {

		new Gare();
		
	}
}
