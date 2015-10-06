package transmetteurs;

public class TransmetteurAnalogiqueBruiteNonConforme extends Exception {

	/**
	 * Exception liée à un transmetteur analogique bruité analogique
	 */
	private static final long serialVersionUID = 6969L;
	
	public TransmetteurAnalogiqueBruiteNonConforme() {
        super();
     }
  
  
      public TransmetteurAnalogiqueBruiteNonConforme(String motif) {
        super(motif);
     }
  
	
}
