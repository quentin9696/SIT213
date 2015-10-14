package transmetteurs;

import information.Information;
import information.InformationNonConforme;

import destinations.*;

/**
 * Classe d'un transmetteur parfait. Celui se contente de recevoir un message et le transmet à un tous les destinataire qui lui sont rattaché
*/

public class TransmetteurParfaitAnalogique extends Transmetteur<Double,Double> {

	/**
	 * Méthode permettant de revevoir une information  et le stocker dans l'attribut informationRecue
	 * @param information : L'information (de type boolean) recue de la source
	 * @throws InformationNonConforme : L'information n'est pas conforme (exemple : information null)
	 *
	*/
	@Override
	public void recevoir(Information<Double> information)
			throws InformationNonConforme {
		
		if(information == null) {
			throw new InformationNonConforme();
		}

		this.informationRecue = information;
		this.informationEmise = informationRecue;
	}
	
	/**
         * Méthode permettant d'émettre l'information recue sans la modifier
         * @throws InformationNonConforme : L'information n'est pas conforme
         *
        */
		
	@Override
	public void emettre() throws InformationNonConforme {
		
		for (DestinationInterface<Double> destinationConnectee : destinationsConnectees) {
            destinationConnectee.recevoir(informationEmise);
		}
	}
	
   	public void recyclerRAM() {
	}
}