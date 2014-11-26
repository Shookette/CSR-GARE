public class Billeterie {

	//Temps d'impression d'un ticket
	private final int IMPRESSION_TICKET = 5;
	//Nombre de guichets
	private int nbGuichet;
	
	/**
	 * Constructeur de la billeterie qui initialise son nombre de guichets
	 * @param nbGuichet int nombre de guichet dans la billeterie
	 */
	public Billeterie(int nbGuichet) {
		this.nbGuichet = nbGuichet;
	}
	
	/**
	 * Fonction vendreBillet, qui vend un billet puis l'imprime à un client
	 */
	synchronized public void vendreBillet() {
		//On verifie si un guichet est disponible, sinon on attend
		while (this.nbGuichet < 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//Si un guichet s'est libéré, on le prend
		this.nbGuichet--;
		System.out.println("VOYAGEUR :  "+
				Thread.currentThread().getName()
				+" achete un billet");
		//On imprime un ticket
		try {
			Thread.sleep(IMPRESSION_TICKET);
		} catch (InterruptedException e) {}
		//Le guichet est de nouveau libre
		this.nbGuichet++;
		//On previent les autres voyageurs qui attendent pour un guichet
		notifyAll();
	}
}