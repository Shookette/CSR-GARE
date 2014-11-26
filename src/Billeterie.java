public class Billeterie {

	private final int IMPRESSION_TICKET = 5;
	private int nbGuichet;
	private int nbBilletDispo;
	private Train[] trains;
	
	/*
	 * Nombre de billet non borné
	 * imprimer un ticket met IMPRESSION_TICKET temps
	 */
	
	/**
	 * Constructeur de la billeterie
	 * @param nbGuichet
	 * @param NB_VOIES
	 */
	public Billeterie(int nbGuichet, int NB_VOIES) {
		this.nbGuichet = nbGuichet;
		this.nbBilletDispo = 0;
		this.trains = new Train[NB_VOIES];
	}
	
	/**
	 * Mise à jours du nombre de billet disponible
	 * @param train contient l'objet d'un train pour pouvoir récupérer ses attributs et communiquer avec
	 * @param voie le numéro de la voie sur lequel le train arrive ou repart, afin de pouvoir agir avec le tableau de voie
	 * @param arrive indique si le train arrive ou part (incrément / décrément)
	 */
	synchronized public void updateBilletDispo(Train train, int voie, boolean arrive) {
		//Si le train arrive, alors on ajoute son nombre de place libre au nombre de billet disponible
		//On ajoute aussi son train à notre tableau de voie
		if (arrive) {
			this.trains[voie] = train;
			this.nbBilletDispo += train.getNbPlaceLibre();
		} else {
		//Sinon on supprime notre train du tableau de voie
		//On diminue le nombre de place disponible par le nombre de place libre restante dans le train
			this.trains[voie] = null;
			this.nbBilletDispo -= train.getNbPlaceLibre();
		}
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
		//Recherche d'un train dont le nombre de place disponible est supérieur à 0
		int voie = 0;
		while(this.trains[voie] == null || this.trains[voie].getNbPlaceLibre() <= 0) {
			if (voie == this.trains.length - 1) {
				voie = 0;
			} else {
				voie++;
			}
		}
		//Achat du ticket, diminution du nombre de billet total et du nombre de place libre dans le trains
		this.trains[voie].setNbPlaceLibre(this.trains[voie].getNbPlaceLibre() -1);
		this.nbBilletDispo--;
		//Impression du ticket
		try {
			Thread.sleep(IMPRESSION_TICKET);
		} catch (InterruptedException e) {}
		//Rendu du guichet
		this.nbGuichet++;
		notifyAll();
		return voie;
	}
}
