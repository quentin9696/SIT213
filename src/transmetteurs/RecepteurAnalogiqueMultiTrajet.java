package transmetteurs;

import information.Information;
import information.InformationNonConforme;

public class RecepteurAnalogiqueMultiTrajet extends Transmetteur<Double, Double>{

	public int taux = 0;
	
	public RecepteurAnalogiqueMultiTrajet(int taux) throws TransmetteurAnalogiqueMultiTrajetNonConforme {
		if(taux < 0) {
			throw new TransmetteurAnalogiqueMultiTrajetNonConforme("Le décalage ne peux pas être négatif !");
		}
	}
	@Override
	public void recevoir(Information<Double> information)
			throws InformationNonConforme {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void emettre() throws InformationNonConforme {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void recyclerRAM() {
		// TODO Auto-generated method stub
		
	}

}
