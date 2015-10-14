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
		if(information == null || information.nbElements() == 0) {
			throw new InformationNonConforme();

		}
		
		int nbEch = information.nbElements() - tauMax();
		informationEmise = new Information <Double>(nbEch);


		System.out.println(nbEch);
		
		for (int i = 0; i < nbEch; i++)
		{
			double temp = information.iemeElement(i);
			//System.out.println("before : " + temp);
			for (int j = 0; j < tau.size(); j++)
			{
				int delta = tau.get(j);
				if (i>= delta && delta < nbEch)
				{
					//System.out.println(alpha.get(j) + "val" + information.iemeElement(i-delta));
					temp -= alpha.get(j)*information.iemeElement(i-delta);
					
				}
				//System.out.println(temp);
			}
			information.setIemeElement(i, temp);
		}
		
		/*for (int i = 0; i <tau.size(); i++) 
		{
			
				int delta = tau.get(i);
				double att = alpha.get(i);
				if (delta < nbEch)
				{
					for (int j = delta; j < nbEch; j++)
					{
					information.setIemeElement(j, information.iemeElement(j) - att*information.iemeElement(j-delta));	
		
						
					}
	/*				for (int j = 0; j < nbEch; j++)
					{
						if (j>=delta)
						information.setIemeElement(j, information.iemeElement(j) - att*information.iemeElement(j-delta));	
					}
					
				}*/
				for(int k = 0; k < nbEch; k++)
				{
					informationEmise.add(information.iemeElement(k));
				}
				
		
		this.emettre();
		
			}

	@Override
	public void emettre() throws InformationNonConforme {
		// TODO Auto-generated method stub
		System.out.println(informationEmise.nbElements());

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
