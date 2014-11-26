public class Billeterie {

	//Temps d'impression d'un ticket
	private final int IMPRESSION_TICKET = 5;
	//Nombre de guichets
	private int nbGuichet;
	//Nombre de billets disponibles (nombre de places dans les trains sur le quai)
	private int nbBilletDispo;
	//Tableau contenant les trains sur le quain
	private Train[] trains;
	
	/**
	 * Constructeur de la billeterie
	 * @param nbGuichet nombre de guichets dans la billeterie 
	 * @param NB_VOIES nombre de voies sur le quai
	 */
	public Billeterie(int nbGuichet, int NB_VOIES) {
		this.nbGuichet = nbGuichet;
		this.nbBilletDispo = 0;
		//tableau de train initialisé au nombre de voies sur le quai
		this.trains = new Train[NB_VOIES];
	}
	
	/**
	 * Mise à jour du nombre de billets disponibles
	 * @param train contient l'objet d'un train pour pouvoir récupérer ses attributs et communiquer avec
	 * @param voie le numéro de la voie sur lequel le train arrive ou repart, afin de pouvoir agir avec le tableau de voies
	 * @param arrive indique si le train arrive ou part (incrémente / décrémente le nombre de billets dispo)
	 */
	synchronized public void updateBilletDispo(Train train, int voie, boolean arrive) {
		//Si le train arrive, alors on ajoute son nombre de places libres au nombre de billets disponibles
		//On ajoute aussi son train à notre tableau de voies
		if (arrive) {
			this.trains[voie] = train;
			this.nbBilletDispo += train.getNbPlaceLibre();
		} else {
		//Sinon on supprime notre train du tableau de voie
		//On diminue le nombre de billets disponibles par le nombre de places libres restantes dans le train
			this.trains[voie] = null;
			this.nbBilletDispo -= train.getNbPlaceLibre();
		}
		//on previent les guichets du nombre de billets qu'ils peuvent vendre
		notifyAll();
	}
	
	/**
	 * Action de vendre un billet
	 * @return la voie du train correspondant aux billet acheté
	 */
	synchronized public int vendreBillet() {
		//Attente pour un guichet libre
		while (this.nbGuichet < 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.nbGuichet--;
		//Attente pour un billet disponible
		while(this.nbBilletDispo <= 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//Recherche d'un train dont le nombre de places disponibles est supérieur à 0
		int voie = 0;
		while(this.trains[voie] == null || this.trains[voie].getNbPlaceLibre() <= 0) {
			if (voie == this.trains.length - 1) {
				voie = 0;
			} else {
				voie++;
			}
		}
		System.out.println("VOYAGEUR : "+
				Thread.currentThread().getName()
				+" achete un billet pour le train sur la voie "+voie);
		//Achat du ticket, diminution du nombre de billets total et du nombre de places libres dans le train
		this.trains[voie].setNbPlaceLibre(this.trains[voie].getNbPlaceLibre() -1);
		this.nbBilletDispo--;
		//Impression du ticket
		try {
			Thread.sleep(IMPRESSION_TICKET);
		} catch (InterruptedException e) {}
		//Rendu du guichet
		this.nbGuichet++;
		//On previent les autres voyageurs qu'un guichet s'est libéré
		notifyAll();
		return voie;
	}
}