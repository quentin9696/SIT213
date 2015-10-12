package transmetteurs;

public class TransmetteurAnalogiqueMultiTrajetNonConforme extends Exception {

	/**
	 * Exception liée à un transmetteur analogique bruité analogique
	 */
	private static final long serialVersionUID = 6969L;
	
	public TransmetteurAnalogiqueMultiTrajetNonConforme() {
        super();
     }
  
  
      public TransmetteurAnalogiqueMultiTrajetNonConforme(String motif) {
        super(motif);
     }
  
	
}
