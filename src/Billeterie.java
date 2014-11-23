public class Billeterie {

	private final int IMPRESSION_TICKET = 5;
	private int nbGuichet;
	private int nbBilletDispo;
	private Train[] trains;
	
	/*
	 * Nombre de billet non borné
	 * imprimer un ticket met IMPRESSION_TICKET temps
	 */
	
	public Billeterie(int nbGuichet, int NB_VOIES) {
		this.nbGuichet = nbGuichet;
		this.nbBilletDispo = 0;
		this.trains = new Train[NB_VOIES];
	}
	
	synchronized public void updateBilletDispo(Train train, int voie) {
		this.nbBilletDispo = 0;
		this.trains[voie] = train;
		for(int i =0;i <this.trains.length;i++) {
			if(this.trains[i] != null){
				this.nbBilletDispo += this.trains[i].getNbPlaceLibre();
			}		
		}
	}
	
	synchronized public int getPlaceAchetee(int voie) {
		return this.trains[voie].getCapacite() - this.trains[voie].getNbPlaceLibre();
	}
	
	synchronized public int vendreBillet() {
		System.out.println("Entre aux guichet");
		this.nbGuichet--;
		while (this.nbGuichet < 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("vérifie qu'il y a une place libre");
		while(this.nbBilletDispo <= 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		int voie = 0;
		System.out.println("Cherche pour quelle voie la place est libre et nbplace = "+this.nbBilletDispo);
		while(this.trains[voie] == null || this.trains[voie].getNbPlaceLibre() == 0) {
			if (voie == this.trains.length - 1) {
				voie = 0;
			} else {
				voie++;
			}
			System.out.println("num voie : "+voie);
		}
		System.out.println("VOYAGEUR :  "+
				Thread.currentThread().getName()
				+" achete une place du train sur la voie "+voie);
		
		this.trains[voie].setNbPlaceLibre(this.trains[voie].getNbPlaceLibre() -1);
		try {
			Thread.sleep(IMPRESSION_TICKET);
		} catch (InterruptedException e) {}
		this.nbGuichet++;
		notifyAll();
		return voie;
	}
}
