public class Quai {

	//Variables
	private int nbVoies;
	private int nbVoiesLibre;
	private Train[] trains;
	private Billeterie billeterie;
	
	/*
	 * 1 voie = 1 train
	 * 1) Si une voie est libre alors un train peut entrer en gare. 
	 */
	
	/**
	 * Constructeur du quai
	 * @param nbVoies définie le nombre de voie disponible dans le quai
	 * @param billeterie Associe la billeterie au quai
	 */
	public Quai(int nbVoies, Billeterie billeterie) {
		this.nbVoies = nbVoies;
		this.nbVoiesLibre = nbVoies;
		this.trains = new Train[this.nbVoies];
		this.billeterie = billeterie;
	}
	
	/**
	 * Fonction monterTrain appelé par un voyageur pour monter dans un train explicite
	 * @param voie indique la voie sur lequel se trouve le train du voyageur
	 */
	synchronized public void monterTrain(int voie) {
		//Incrémente le nombre de voyageur dans le train
		this.trains[voie].setNbVoyageur(this.trains[voie].getNbVoyageur()+1);
		//Notifie le train qu'un voyageur est monté dans le train
		notifyAll();
	}
	
	/**
	 * Fonction viderTrain appelé dans le run du train afin d'initialiser 
	 * son nombre de place libre disponible et son nombre de voyageyr
	 * @param train sur lequel les initialisation seront faite.
	 */
	synchronized public void viderTrain(Train train) {
		//Défini le nombre de place libre à la capacité totale du train
		train.setNbPlaceLibre(train.getCapacite());
		//Défini le nombre de voyageur à 0 (vide le train)
		train.setNbVoyageur(0);
	}
	
	/**
	 * Fonction arriverTrain appelé dans le run du train pour indiquer et provoquer son arrivé dans le quai
	 * @param train indique quel train arrive dans la gare
	 */
	synchronized public void arriverTrain(Train train) {
		//Attente pour une voie libre
		while(nbVoiesLibre < 1) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//Indique que le train à prit une voie
		nbVoiesLibre--;
		//Vérifie et attribut une voie libre au train
		int i = 0;
		while (this.trains[i] != null && i < nbVoies) {
			if (i == nbVoies - 1){
				i = 0;
			} else {
				i++;	
			}
		}
		//Ajoute a notre tableau de voie/train notre train à la voie indiqué
		this.trains[i] = train;		
		//Indique à la billeterie qu'un train est arrivé afin de mettre à jour le nombre de billet disponible
		this.billeterie.updateBilletDispo(train, i, true);
	}
	
	/**
	 * Fonction partirTrain appelé lorsqu'un train est sur le point de partir du quai
	 * Le train va libérer sa voie, bloquer l'achat d'un billet pour son train et attendre les derniers voyageurs
	 * @param train indique quel train part du quai
	 */
	synchronized public void partirTrain(Train train) {
		//Récupère la voie du train passé en paramètre
		int i = 0;
		while (this.trains[i] != train && i < this.trains.length) {
			if (i == this.trains.length - 1){
				i = 0;
			} else {
				i++;	
			}
		}
		//Cloture l'achat de billet pour se train
		this.billeterie.updateBilletDispo(train, i, false);	
		//Attend que tout les voyageurs soient montés dans le train
		while (this.trains[i].getNbVoyageur() < this.trains[i].getPlaceAchetee()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//Met à null la voie du train dans notre tableau de train pour indiquer que la voie est disponible
		this.trains[i] = null;
		nbVoiesLibre++;
		//Notifie les trains qui attendent qu'une voie s'est libérée
		notifyAll();
	}
}
