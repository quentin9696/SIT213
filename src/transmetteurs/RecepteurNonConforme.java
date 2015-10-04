package transmetteurs;

public class RecepteurNonConforme extends Exception {

	/**
	 * Exception liée à un recepteur analogique
	 */
	private static final long serialVersionUID = 6969L;
	
	public RecepteurNonConforme() {
        super();
     }
  
  
      public RecepteurNonConforme(String motif) {
        super(motif);
     }
  
	
}
