package transmetteurs;

import java.util.ArrayList;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConforme;

public class TransmetteurAnalogiqueMultiTrajet extends Transmetteur<Double, Double>{


	private ArrayList<Integer> tau;
	private ArrayList<Double> alpha;
	
	public TransmetteurAnalogiqueMultiTrajet(ArrayList<Integer> tau, ArrayList<Double> alpha) throws TransmetteurAnalogiqueMultiTrajetNonConforme {
			
		if(tau.size() < 1 || tau.size() > 5 || alpha.size() > 5 || alpha.size() < 1) {
			throw new TransmetteurAnalogiqueMultiTrajetNonConforme("On ne peut avoir qu'entre 1 et 5 multitrajet max");	
		}
		
		if(tau.size() != alpha.size()) {
			throw new TransmetteurAnalogiqueMultiTrajetNonConforme("On doit forcement avoir 1 tau associé à 1 alpha");
		}
		
		this.tau = tau;
		this.alpha = alpha;
		
		if(this.tauMin() < 0) {
			throw new TransmetteurAnalogiqueMultiTrajetNonConforme("Le décalage ne peux pas être négatif !");
		}
		
		if(this.alphaMax() > 1 || this.alphaMin() < 0) {
			throw new TransmetteurAnalogiqueMultiTrajetNonConforme("L'atténuation ne peut pas être négatif ou supérieure à 1");
		}
	}
	@Override
	public void recevoir(Information<Double> information)
			throws InformationNonConforme {
		// TODO Auto-generated method stub
		
		if(information == null || information.nbElements() == 0) {
			throw new InformationNonConforme();
		}
		
		int tauMax = this.tauMax();
		
		informationRecue = information;
		informationEmise = new Information<Double>(informationRecue.nbElements() + tauMax);
		
		for(int i = 0; i<(informationRecue.nbElements() + tauMax); i++) {
			if(i<informationRecue.nbElements()) {
				informationEmise.add(informationRecue.iemeElement(i));
			}
			else {
				informationEmise.add(0.);
			}
		}
		
		/*System.out.println("Nb recu : " + informationRecue.nbElements());
		System.out.println("Nb emis : " + informationEmise.nbElements() );*/
		
		int j=0;
		for(int val : tau) {
			
			for(int i=0; i<=informationEmise.nbElements(); i++) {	
				if(i>=val && i<informationRecue.nbElements() + val) {
					informationEmise.setIemeElement(i, (informationEmise.iemeElement(i) + (alpha.get(j)*informationRecue.iemeElement(i-val))));
				}
			}
			
			j++;
		}
	}

	@Override
	public void emettre() throws InformationNonConforme {
		// TODO Auto-generated method stub
		for (DestinationInterface<Double> destinationConnectee : destinationsConnectees) {
			destinationConnectee.recevoir(informationEmise);
		}
	}

	@Override
	public void recyclerRAM() {
		// TODO Auto-generated method stub
	}
	
	private int tauMax() {
		int max = tau.get(0);
		
		for(int val : tau) {
			if(val > max) {
				max = val;
			}
		}
		
		return max;
	}

	private int tauMin() {
		int min = tau.get(0);
		
		for(int val : tau) {
			if(val < min) {
				min = val;
			}
		}
		
		return min;
	}
	
	private double alphaMax() {
		double max = alpha.get(0);
		
		for(double val : alpha) {
			if(val > max) {
				max = val;
			}
		}
		
		return max;
	}
	
	private double alphaMin() {
		double min = alpha.get(0);
		
		for(double val : alpha) {
			if(val < min) {
				min = val;
			}
		}
		
		return min;
	}
}
