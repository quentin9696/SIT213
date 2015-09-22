package destinations;

import information.Information;
import information.InformationNonConforme;

/**
 * Classe de la dernière destination de la chaine de transmission. Cette classe permet de récupérer le signal sans modification du module auquel il est rattaché.
 * @author Quentin Vallin, Yannick Omnès 
 *
*/
public class DestinationFinale extends Destination<Boolean>{

	/**
	 * Cette méthode permet de recevoir l'information et stock l'information reçu dans son attribut informationRecue. 
	 * L'information n'est pas modifiée
	 * @param information L'information à recevoir 
	 */
	
	@Override
	public void recevoir(Information<Boolean> information) throws InformationNonConforme {
		// TODO Auto-generated method stub
		informationRecue = information;
	}

}
