package transmetteurs;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConforme;

public class EmetteurTransducteur extends Transmetteur<Boolean, Boolean> {

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
			if(b) {
				informationEmise.add(true);
				informationEmise.add(false);
				informationEmise.add(true);
			}
			else {
				informationEmise.add(false);
				informationEmise.add(true);
				informationEmise.add(false);
			}
		}
		
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
