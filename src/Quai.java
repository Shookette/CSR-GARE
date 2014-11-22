import java.util.Random;


public class Quai {

	private int nbVoies;
	private int nbVoiesLibre;
	private Train[] trains;
	private int capaciteTotale;
	
	/*
	 * 1 voie = 1 train
	 * 1) Si une voie est libre alors un train peut entrer en gare. 
	 */
	
	public Quai(int nbVoies) {
		this.nbVoies = nbVoies;
		this.nbVoiesLibre = nbVoies;
		this.trains = new Train[this.nbVoies];
		this.capaciteTotale = 0;
	}
	
	synchronized public void monterTrain() {
		System.out.println("VOYAGEUR :  "+
				Thread.currentThread().getName()
				+" Regarde si il reste une place dans les trains");
		while(capaciteTotale <= 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Capacité totale : "+capaciteTotale);
		//trouver train
		
		int i = 0;
		while((this.trains[i] == null || this.trains[i].getNbPlaceLibre() <= 0) && i < this.trains.length) {
//			System.out.println("this.train "+i+" : "+this.trains[i].getNbPlaceLibre()); 
		
			System.out.println("VOYAGEUR :  "+
					Thread.currentThread().getName()
					+" Essaye de monter dans un train");
			
			if (i == this.trains.length - 1){
				i = 0;
			} else {
				i++;	
			}
		}
		capaciteTotale--;
		
		this.trains[i].setNbPlaceLibre(this.trains[i].getNbPlaceLibre()-1);
	}
	
	synchronized public void viderTrain(Train train) {
//		System.out.println("TRAIN :  "+
//				Thread.currentThread().getName()
//				+" vide ses places");
		//rand new capacité du train
		Random rand = new Random();
		int nbPlace = rand.nextInt(train.getCapacite());
		train.setNbPlaceLibre(nbPlace);
//		System.out.println("TRAIN :  "+
//				Thread.currentThread().getName()
//				+" est en direction de la gare");
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
//			System.out.println("TRAIN :  "+
//					Thread.currentThread().getName()
//					+" bloqué pour se garer");
		}
		this.trains[i] = train;
		this.capaciteTotale += train.getCapacite();
		System.out.println("TRAIN :  "+
				Thread.currentThread().getName()
				+" est arrivé en gare. \nLa nouvelle capacité est de : "+capaciteTotale);
		notifyAll();
	}
	
	synchronized public void partirTrain(Train train) {
//		System.out.println("TRAIN :  "+
//				Thread.currentThread().getName()
//				+" part");
		int i = 0;
		while (this.trains[i] != train && i < this.trains.length) {
			if (i == this.trains.length - 1){
				i = 0;
			} else {
				i++;	
			}
		}
		this.capaciteTotale -= this.trains[i].getCapacite();
		System.out.println("La capacité total du quai est maintenant de : "+capaciteTotale);
		this.trains[i] = null;
		nbVoiesLibre++;
		notifyAll();
	}
}
