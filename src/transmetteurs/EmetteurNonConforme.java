package transmetteurs;

public class EmetteurNonConforme extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6969L;
	
	public EmetteurNonConforme() {
        super();
     }
  
  
      public EmetteurNonConforme(String motif) {
        super(motif);
     }
  
	
}
