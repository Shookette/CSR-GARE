import java.util.Random;


public class Quai {

	//Nombre de voies dans le quai
	private int nbVoies;
	//Nombre de voies encore libres
	private int nbVoiesLibre;
	//Tableau contenant les trains gar�s
	private Train[] trains;
	//Nombre de places disponibles dans les trains sur toutes les voies
	private int capaciteTotale;
	
	/**
	 * Constructeur du quai
	 * @param nbVoies int nombre de voies sur le quai
	 */
	public Quai(int nbVoies) {
		this.nbVoies = nbVoies;
		this.nbVoiesLibre = nbVoies;
		//Tableau du nombre de voies qui contiendra les trains gar�s sur chaque voie
		this.trains = new Train[this.nbVoies];
		//Nombre de places disponibles sur tous les trains � quai
		this.capaciteTotale = 0;
	}
	
	/**
	 * Fonction monterTrain qui permet aux voyageurs de monter dans un train
	 */
	synchronized public void monterTrain() {
		System.out.println("VOYAGEUR :  "+
				Thread.currentThread().getName()
				+" Regarde si il reste une place dans les trains");
		//S'il n'y a plus de place sur aucun train, le voyageur attend
		while(capaciteTotale <= 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		//Si un train est arriv� (et a augment� le nombre de places disponibles),
		//on verifie sur quel voie il est gar� avant de lui enlever une place
		int i = 0;
		while((this.trains[i] == null || this.trains[i].getNbPlaceLibre() <= 0) && i < this.trains.length) {
			
			if (i == this.trains.length - 1){
				i = 0;
			} else {
				i++;	
			}
		}
		System.out.println("VOYAGEUR :  "+
				Thread.currentThread().getName()
				+" Est mont� dans un train");
		
		//On diminue la capacit� sur tout le quai et celle du train de un
		capaciteTotale--;		
		this.trains[i].setNbPlaceLibre(this.trains[i].getNbPlaceLibre()-1);
	}
	
	/**
	 * Fonction viderTrain, intialise le nombre de si�ges libres du train
	 * @param train train � initialiser
	 */
	synchronized public void viderTrain(Train train) {
		//On vide le train quand il arrive en gare
		train.setNbPlaceLibre(train.getCapacite());
	}
	
	/**
	 * Fonction arriverTrain qui indique que le train arrive en quai et g�re la capacit� totale disponible
	 * @param train train qui arrive dans le quai
	 */
	synchronized public void arriverTrain(Train train) {
		//Si aucune voie n'est libre, le train attend qu'une se lib�re
		System.out.println("TRAIN :  "+
		Thread.currentThread().getName()
		+" entre en gare");
		while(nbVoiesLibre < 1) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//le train prend la voie
		nbVoiesLibre--;
		int i = 0;
		//le train cherche une voie qui est libre
		while (this.trains[i] != null && i < nbVoies) {
			if (i == nbVoies - 1){
				i = 0;
			} else {
				i++;	
			}
		}
		//Le train se gare
		this.trains[i] = train;
		//On ajoute la capacit� du train au nombre de places disponibles dans le quai
		this.capaciteTotale += train.getNbPlaceLibre();
		System.out.println("TRAIN :  "+
				Thread.currentThread().getName()
				+" est gar� sur une voie.");
		notifyAll();
	}
	
	/**
	 * Fonction partirTrain qui indique que le train part et r�initialise la capacit�Totale sans le train
	 * @param train train qui part de la gare
	 */
	synchronized public void partirTrain(Train train) {
		System.out.println("TRAIN :  "+
				Thread.currentThread().getName()
				+" va partir de la gare");
		int i = 0;
		//on cherche sur quelle voie le train est gar�
		while (this.trains[i] != train && i < this.trains.length) {
			if (i == this.trains.length - 1){
				i = 0;
			} else {
				i++;	
			}
		}
		//on retire le nombre de places libres restantes du train � la capacit� totale dans le quai
		this.capaciteTotale -= this.trains[i].getNbPlaceLibre();
		this.trains[i] = null;
		System.out.println("TRAIN :  "+
				Thread.currentThread().getName()
				+" est parti");
		//On libere la voie
		nbVoiesLibre++;
		//On pr�vient les autres trains qui attendent pour se garer qu'il y a une voie de libre
		notifyAll();
	}
}