package transmetteurs;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConforme;

/**
 * 
 * Classe pemettant d'emettre avec un codage de source (ici les 1 deviennent "101" et 0 devient "010")
 *
 */

public class EmetteurTransducteur extends Transmetteur<Boolean, Boolean> {

	/**
	 * Recoit une information et la transcode
	 * @param information l'information reçu
	 */
	@Override
	public void recevoir(Information<Boolean> information)
			throws InformationNonConforme {
		// TODO Auto-generated method stub
		if(information == null) {
			throw new InformationNonConforme("L'information ne peut pas être nulle");
		}
		
		this.informationRecue = information;
		this.informationEmise = new Information<Boolean>(3*informationRecue.nbElements());
		
		for(Boolean b : informationRecue) {
			//Si le bit est "1"
			if(b) {
				informationEmise.add(true);
				informationEmise.add(false);
				informationEmise.add(true);
			}
			//Si le bit est "0"
			else {
				informationEmise.add(false);
				informationEmise.add(true);
				informationEmise.add(false);
			}
		}
		
	}

	/**
	 * Methode emettre : emet l'information transcodé 
	 */
	@Override
	public void emettre() throws InformationNonConforme {
		// TODO Auto-generated method stub
		for (DestinationInterface<Boolean> destinationConnectee : destinationsConnectees) {
            destinationConnectee.recevoir(informationEmise);
         }
	}

	@Override
	public void recyclerRAM() {
		// TODO Auto-generated method stub
		
	}

}
