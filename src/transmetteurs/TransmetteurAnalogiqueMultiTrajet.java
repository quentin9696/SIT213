package transmetteurs;

import information.Information;
import information.InformationNonConforme;

public class TransmetteurAnalogiqueMultiTrajet extends Transmetteur<Double, Double>{


	private int tau = 0;
	private double alpha;
	
	public TransmetteurAnalogiqueMultiTrajet(int tau, double alpha) throws TransmetteurAnalogiqueMultiTrajetNonConforme {
		if(tau < 0) {
			throw new TransmetteurAnalogiqueMultiTrajetNonConforme("Le décalage ne peux pas être négatif !");
		}	
	
		this.tau = tau;
		
		if(alpha < 0 || alpha > 1) {
			throw new TransmetteurAnalogiqueMultiTrajetNonConforme("L'atténuation ne peut pas être nulle ou supérieure à 1");
		}
		
		this.alpha = alpha;
	}
	@Override
	public void recevoir(Information<Double> information)
			throws InformationNonConforme {
		// TODO Auto-generated method stub
		
		if(information == null || information.nbElements() == 0) {
			throw new InformationNonConforme();
		}
		
		informationRecue = information;
		informationEmise = new Information<Double>(informationRecue.nbElements() + tau);
		for(int i=0; i<informationRecue.nbElements()+tau; i++) {
			if(i<tau) {
				informationEmise.add(informationRecue.iemeElement(i));
			}
			else if(i>tau && i<informationRecue.nbElements()) {
				informationEmise.add(informationRecue.iemeElement(i) + (alpha*informationRecue.iemeElement(i-tau)));
			}
			else {
				informationEmise.add(informationRecue.iemeElement(i-tau));
			}
		}
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
