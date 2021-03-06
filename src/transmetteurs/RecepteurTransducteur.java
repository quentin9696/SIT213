package transmetteurs;

import java.util.ArrayList;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConforme;
/**
 * 
 * Décodage de source et correction de l'erreur  
 *
 */

public class RecepteurTransducteur extends Transmetteur<Boolean, Boolean> {
	/**
	 * Reception des bits à encoder et encodage
	 * @param information l'information à recevoir
	 */
	@Override
	public void recevoir(Information<Boolean> information)
			throws InformationNonConforme {
		// TODO Auto-generated method stub
		if(information == null) {
			throw new InformationNonConforme("L'information ne peut pas être nulle");
		}
		
		this.informationRecue = information;
		this.informationEmise = new Information<Boolean>(informationRecue.nbElements()/3);
		
		/*ArrayList<Boolean> decodage = new ArrayList<Boolean>(3);
		for (int i =0; i <3; i++)
		{
			decodage.add(false);
		}*/
		
		//On prend les bits 3 à 3
		for(int i = 0; i < information.nbElements(); i+=3) {
			if ((information.iemeElement(i) && !information.iemeElement(i+1)) 
					||(!information.iemeElement(i+1) && information.iemeElement(i+2))
					|| (information.iemeElement(i) && information.iemeElement(i+2))
)					{
						informationEmise.add(true);
					}
			else
				informationEmise.add(false);
		}
	}
/**
 * Emission des bits encodés
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
