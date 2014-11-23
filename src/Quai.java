public class Quai {

	private int nbVoies;
	private int nbVoiesLibre;
	private Train[] trains;
	private Billeterie billeterie;
	
	/*
	 * 1 voie = 1 train
	 * 1) Si une voie est libre alors un train peut entrer en gare. 
	 */
	
	public Quai(int nbVoies, Billeterie billeterie) {
		this.nbVoies = nbVoies;
		this.nbVoiesLibre = nbVoies;
		this.trains = new Train[this.nbVoies];
		this.billeterie = billeterie;
	}
	
	synchronized public void monterTrain(int voie) {
		System.out.println("VOYAGEUR :  "+
				Thread.currentThread().getName()
				+" monte dans le train sur la voie "+voie);
		this.trains[voie].setNbPlaceLibre(this.trains[voie].getNbPlaceLibre()-1);
		this.trains[voie].setNbVoyageur(this.trains[voie].getNbVoyageur()+1);
		notifyAll();
	}
	
	synchronized public void viderTrain(Train train) {
		train.setNbPlaceLibre(train.getCapacite());
		train.setNbVoyageur(0);
	}
	
	synchronized public void arriverTrain(Train train) {
//		System.out.println("TRAIN :  "+
//				Thread.currentThread().getName()
//				+" va entrer en gare dans 2sec\nIl y a : "+nbVoiesLibre+" voie libre");
		while(nbVoiesLibre < 1) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		nbVoiesLibre--;
		System.out.println("TRAIN :  "+
				Thread.currentThread().getName()
				+" essaye de se garer");
		int i = 0;
		while (this.trains[i] != null && i < nbVoies) {
			if (i == nbVoies - 1){
				i = 0;
			} else {
				i++;	
			}
		}
		this.trains[i] = train;
		this.billeterie.updateBilletDispo(train,i);
		notifyAll();
	}
	
	synchronized public void partirTrain(Train train) {
		System.out.println("TRAIN :  "+
				Thread.currentThread().getName()
				+" s'apprete a partir");
		int i = 0;
		while (this.trains[i] != train && i < this.trains.length) {
			if (i == this.trains.length - 1){
				i = 0;
			} else {
				i++;	
			}
		}
		while (this.trains[i].getNbVoyageur() < this.billeterie.getPlaceAchetee(i)) {
			try {
				System.out.println("TRAIN :  "+
						Thread.currentThread().getName()
						+" attend "+(this.billeterie.getPlaceAchetee(i)-this.trains[i].getNbVoyageur())+" voyageurs");
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("TRAIN :  "+
				Thread.currentThread().getName()
				+" part");
		this.trains[i] = null;
		this.billeterie.updateBilletDispo(null, i);
		nbVoiesLibre++;
		notifyAll();
	}
}
