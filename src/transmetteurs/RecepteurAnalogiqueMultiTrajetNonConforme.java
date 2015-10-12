package transmetteurs;

public class RecepteurAnalogiqueMultiTrajetNonConforme extends Exception {

	/**
	 * Exception liée à un transmetteur analogique bruité analogique
	 */
	private static final long serialVersionUID = 6969L;
	
	public RecepteurAnalogiqueMultiTrajetNonConforme() {
        super();
     }
  
  
      public RecepteurAnalogiqueMultiTrajetNonConforme(String motif) {
        super(motif);
     }
  
	
}
