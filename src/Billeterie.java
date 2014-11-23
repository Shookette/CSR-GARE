public class Billeterie {

	private final int IMPRESSION_TICKET = 5;
	private int nbGuichet;
	
	/*
	 * Nombre de billet non borné
	 * imprimer un ticket met IMPRESSION_TICKET temps
	 */
	
	/**
	 * Constructeur de la billeterie qui initialise son nombre de guichet
	 * @param nbGuichet
	 */
	public Billeterie(int nbGuichet) {
		this.nbGuichet = nbGuichet;
	}
	
	/**
	 * Fonction vendreBillet, qui vend un billet puis l'imprime à un client
	 */
	synchronized public void vendreBillet() {
		this.nbGuichet--;
		while (this.nbGuichet < 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			Thread.sleep(IMPRESSION_TICKET);
		} catch (InterruptedException e) {}
		this.nbGuichet++;
		notifyAll();
	}
}
