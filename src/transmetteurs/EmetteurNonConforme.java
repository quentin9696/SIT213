package transmetteurs;

public class EmetteurNonConforme extends Exception {

	/**
	 * Exception lié à un emetteur analogique. 
	 */
	private static final long serialVersionUID = 6969L;
	
	public EmetteurNonConforme() {
        super();
     }
  
  
      public EmetteurNonConforme(String motif) {
        super(motif);
     }
  
	
}
