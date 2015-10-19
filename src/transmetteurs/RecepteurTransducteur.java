package transmetteurs;

import java.util.ArrayList;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConforme;

public class RecepteurTransducteur extends Transmetteur<Boolean, Boolean> {

	@Override
	public void recevoir(Information<Boolean> information)
			throws InformationNonConforme {
		// TODO Auto-generated method stub
		if(information == null) {
			throw new InformationNonConforme("L'information ne peut pas être nulle");
		}
		
		this.informationRecue = information;
		this.informationEmise = new Information<Boolean>(informationRecue.nbElements()/3);
		ArrayList<Boolean> decodage = new ArrayList<Boolean>(3);
		for(int i = 0; i < information.nbElements(); i++) {
			
		}
		
		informationEmise.add(true);
	}

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
