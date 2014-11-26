public class Quai {

	//Nombre de voies dans le quai
	private int nbVoies;
	//Nombre de voies encore libres
	private int nbVoiesLibre;
	//Tableau contenant les trains gar�s
	private Train[] trains;
	//billeterie li�e au quai	
	private Billeterie billeterie;
	
	/**
	 * Constructeur du quai
	 * @param nbVoies d�finit le nombre de voies disponibles dans le quai
	 * @param billeterie Associe la billeterie au quai
	 */
	public Quai(int nbVoies, Billeterie billeterie) {
		this.nbVoies = nbVoies;
		this.nbVoiesLibre = nbVoies;
		this.trains = new Train[this.nbVoies];
		this.billeterie = billeterie;
	}
	
	/**
	 * Fonction monterTrain appel�e par un voyageur pour monter dans un train d�t�rmin� par le billet
	 * @param voie indique la voie sur laquelle se trouve le train du voyageur
	 */
	synchronized public void monterTrain(int voie) {
		System.out.println("VOYAGEUR : "+
				Thread.currentThread().getName()
				+" monte dans le train sur la voie "+voie);
		//Incr�mente le nombre de voyageurs dans le train
		this.trains[voie].setNbVoyageur(this.trains[voie].getNbVoyageur()+1);
		//Notifie le train qu'un voyageur est mont�
		notifyAll();
	}
	
	/**
	 * Fonction viderTrain appel�e dans le run du train afin d'initialiser 
	 * son nombre de places libres disponibles et son nombre de voyageurs
	 * @param train sur lequel les initialisations seront faites.
	 */
	synchronized public void viderTrain(Train train) {
		//D�finit le nombre de places libres � la capacit� totale du train
		train.setNbPlaceLibre(train.getCapacite());
		//D�finit le nombre de voyageurs � 0 (vide le train)
		train.setNbVoyageur(0);
	}
	
	/**
	 * Fonction arriverTrain appel�e dans le run du train pour indiquer et provoquer son arriv�e dans le quai
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
		//V�rifie et attribue une voie libre au train
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
				+" est gar� voie "+i);
		//Ajoute a notre tableau de voies/trains notre train � la voie indiqu�e
		this.trains[i] = train;		
		//Indique � la billeterie qu'un train est arriv� afin de mettre � jour le nombre de billets disponibles
		this.billeterie.updateBilletDispo(train, i, true);
	}
	
	/**
	 * Fonction partirTrain appel� lorsqu'un train est sur le point de partir du quai
	 * Le train va lib�rer sa voie, bloquer l'achat d'un billet pour son train et attendre les derniers voyageurs
	 * @param train indique quel train part du quai
	 */
	synchronized public void partirTrain(Train train) {
		System.out.println("TRAIN : "+
		Thread.currentThread().getName()
		+" va partir de la gare");
		//R�cup�re la voie du train pass� en param�tre
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
		//Attend que tous les voyageurs soient mont�s dans le train
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
		//Met � null la voie du train dans notre tableau de train pour indiquer que la voie est disponible
		this.trains[i] = null;
		nbVoiesLibre++;
		System.out.println("TRAIN : "+
				Thread.currentThread().getName()
				+" est parti");
		//Notifie les trains qui attendent qu'une voie s'est lib�r�e
		notifyAll();
	}
}