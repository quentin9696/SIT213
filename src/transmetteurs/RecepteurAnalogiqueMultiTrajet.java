package transmetteurs;

import java.util.ArrayList;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConforme;

public class RecepteurAnalogiqueMultiTrajet extends Transmetteur<Double, Double>{


	private ArrayList<Integer> tau;
	private ArrayList<Double> alpha;
	
	public RecepteurAnalogiqueMultiTrajet(ArrayList<Integer> tau, ArrayList<Double> alpha) throws RecepteurAnalogiqueMultiTrajetNonConforme {
			
		if(tau.size() < 1 || tau.size() > 5 || alpha.size() > 5 || alpha.size() < 1) {
			throw new RecepteurAnalogiqueMultiTrajetNonConforme("On ne peut avoir qu'entre 1 et 5 multitrajet max");	
		}
		
		if(tau.size() != alpha.size()) {
			throw new RecepteurAnalogiqueMultiTrajetNonConforme("On doit forcement avoir 1 tau associé à 1 alpha");
		}
		
		this.tau = tau;
		this.alpha = alpha;
		
		if(this.tauMin() < 0) {
			throw new RecepteurAnalogiqueMultiTrajetNonConforme("Le décalage ne peux pas être négatif !");
		}
		
		if(this.alphaMax() > 1 || this.alphaMin() < 0) {
			throw new RecepteurAnalogiqueMultiTrajetNonConforme("L'atténuation ne peut pas être négatif ou supérieure à 1");
		}
	}
	@Override
	public void recevoir(Information<Double> information)
			throws InformationNonConforme {
		// TODO Auto-generated method stub
		int nbEch = information.nbElements() - tauMax();
		if(information == null || information.nbElements() == 0) {
			throw new InformationNonConforme();
		}
		Information<Double> signalRecu = new Information<Double>(nbEch);
		for (int i = 0; i < nbEch; i++)
		{
			Double temp = information.iemeElement(i);
			for (int j=0; j< tau.size(); j++)
			{
				int retard = tau.get(j);
				if (i>retard && i < retard + nbEch)
				{
					temp -= information.iemeElement(i-retard) * alpha.get(j);
					
				}
				
			}
			informationEmise.add(temp);
		}
		this.emettre();
		
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
