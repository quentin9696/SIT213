package transmetteurs;

import information.Information;
import information.InformationNonConforme;

import destinations.*;

/**
 * Classe d'un transmetteur parfait. Celui se contente de recevoir un message et le transmet à un tous les destinataire qui lui sont rattaché
*/

public class RecepteurAnalogique extends Transmetteur<Float,Boolean> {

	/**
	 * Méthode permettant de revevoir une information  et le stocker dans l'attribut informationRecue
	 * @param information : L'information (de type boolean) recue de la source
	 * @throws InformationNonConforme : L'information n'est pas conforme (exemple : information null)
	 *
	*/
	
	private float min;
	private float max;
	private String forme;
	private int nbEch;
	
	/**
	 * Constructeur
	 * @param min 
	 * @param max
	 * @param forme
	 * @param nbEch
	 */
	
	public RecepteurAnalogique(float min, float max, String forme, int nbEch) {
		super();
		
		this.min = min;
		this.max = max;
		this.forme = forme;
		this.nbEch = nbEch;
	}
	
	@Override
	public void recevoir(Information<Float> information)
			throws InformationNonConforme {
		
		if(information == null) {
			throw new InformationNonConforme();
		}

		this.informationRecue = information;
		this.informationEmise = new Information<Boolean>(); 
		
		if(forme.equalsIgnoreCase("NRZ")) {
			informationEmise.add(true);
		}
		else if(forme.equalsIgnoreCase("NRZT")) {
			
		}
		else if(forme.equalsIgnoreCase("RZ")) {
			
		}
				
		this.emettre();
		
	}
	
	/**
         * Méthode permettant d'émettre l'information recue sans la modifier
         * @throws InformationNonConforme : L'information n'est pas conforme
         *
        */
		
	@Override
	public void emettre() throws InformationNonConforme {
		
		for (DestinationInterface <Boolean> destinationConnectee : destinationsConnectees) {
            destinationConnectee.recevoir(informationEmise);
         }
	}

}