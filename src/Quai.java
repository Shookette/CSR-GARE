public class Quai {

	//Nombre de voies dans le quai
	private int nbVoies;
	//Nombre de voies encore libres
	private int nbVoiesLibre;
	//Tableau contenant les trains garés
	private Train[] trains;
	//billeterie liée au quai	
	private Billeterie billeterie;
	
	/**
	 * Constructeur du quai
	 * @param nbVoies définit le nombre de voies disponibles dans le quai
	 * @param billeterie Associe la billeterie au quai
	 */
	public Quai(int nbVoies, Billeterie billeterie) {
		this.nbVoies = nbVoies;
		this.nbVoiesLibre = nbVoies;
		this.trains = new Train[this.nbVoies];
		this.billeterie = billeterie;
	}
	
	/**
	 * Fonction monterTrain appelée par un voyageur pour monter dans un train détérminé par le billet
	 * @param voie indique la voie sur laquelle se trouve le train du voyageur
	 */
	synchronized public void monterTrain(int voie) {
		System.out.println("VOYAGEUR : "+
				Thread.currentThread().getName()
				+" monte dans le train sur la voie "+voie);
		//Incrémente le nombre de voyageurs dans le train
		this.trains[voie].setNbVoyageur(this.trains[voie].getNbVoyageur()+1);
		//Notifie le train qu'un voyageur est monté
		notifyAll();
	}
	
	/**
	 * Fonction viderTrain appelée dans le run du train afin d'initialiser 
	 * son nombre de places libres disponibles et son nombre de voyageurs
	 * @param train sur lequel les initialisations seront faites.
	 */
	synchronized public void viderTrain(Train train) {
		//Définit le nombre de places libres à la capacité totale du train
		train.setNbPlaceLibre(train.getCapacite());
		//Définit le nombre de voyageurs à 0 (vide le train)
		train.setNbVoyageur(0);
	}
	
	/**
	 * Fonction arriverTrain appelée dans le run du train pour indiquer et provoquer son arrivée dans le quai
	 * @param train indique quel train arrive dans la gare
	 */
	synchronized public void arriverTrain(Train train) {
		System.out.println("TRAIN : "+
				Thread.currentThread().getName()
				+" entre en gare");
		//Attente pour une voie libre
		while(nbVoiesLibre < 1) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//Indique que le train a prit une voie
		nbVoiesLibre--;
		//Vérifie et attribue une voie libre au train
		int i = 0;
		while (this.trains[i] != null && i < nbVoies) {
			if (i == nbVoies - 1){
				i = 0;
			} else {
				i++;	
			}
		}
		System.out.println("TRAIN : "+
				Thread.currentThread().getName()
				+" est garé voie "+i);
		//Ajoute a notre tableau de voies/trains notre train à la voie indiquée
		this.trains[i] = train;		
		//Indique à la billeterie qu'un train est arrivé afin de mettre à jour le nombre de billets disponibles
		this.billeterie.updateBilletDispo(train, i, true);
	}
	
	/**
	 * Fonction partirTrain appelé lorsqu'un train est sur le point de partir du quai
	 * Le train va libérer sa voie, bloquer l'achat d'un billet pour son train et attendre les derniers voyageurs
	 * @param train indique quel train part du quai
	 */
	synchronized public void partirTrain(Train train) {
		System.out.println("TRAIN : "+
		Thread.currentThread().getName()
		+" va partir de la gare");
		//Récupère la voie du train passé en paramètre
		int i = 0;
		while (this.trains[i] != train && i < this.trains.length) {
			if (i == this.trains.length - 1){
				i = 0;
			} else {
				i++;	
			}
		}
		//Cloture l'achat de billet pour ce train
		this.billeterie.updateBilletDispo(train, i, false);	
		//Attend que tous les voyageurs soient montés dans le train
		System.out.println("TRAIN : "+
		Thread.currentThread().getName()
		+" attend ses voyageurs");
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
		System.out.println("TRAIN : "+
				Thread.currentThread().getName()
				+" est parti");
		//Notifie les trains qui attendent qu'une voie s'est libérée
		notifyAll();
	}
}