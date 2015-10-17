package transmetteurs;

import java.awt.ItemSelectable;
import java.util.ArrayList;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConforme;

public class RecepteurAnalogiqueMultiTrajet extends Transmetteur<Double, Double> {


	private ArrayList<Integer> tau;
	private ArrayList<Double> alpha;
	/** 
	 * Constructeur initialisant un récépteur multitrajet
	 * @param tau tableau des décalages
	 * @param alpha tableau des atténuations
	 * @throws RecepteurAnalogiqueMultiTrajetNonConforme
	 */
	public RecepteurAnalogiqueMultiTrajet(ArrayList<Integer> tau, ArrayList<Double> alpha) throws RecepteurAnalogiqueMultiTrajetNonConforme {
			
		if(tau.size() < 1 || tau.size() > 5 || alpha.size() > 5 || alpha.size() < 1) {
			throw new RecepteurAnalogiqueMultiTrajetNonConforme("On ne peut avoir qu'entre 1 et 5 multitrajet max");	
		}
		
		if(tau.size() != alpha.size()) {
			throw new RecepteurAnalogiqueMultiTrajetNonConforme("On doit forcement avoir 1 tau associé à 1 alpha");
		}
		
		this.tau = tau;
		this.alpha = alpha;
		
		if(this.tauMin() < 1) {
			throw new RecepteurAnalogiqueMultiTrajetNonConforme("Le décalage ne peux pas être négatif !");
		}
		
		if(this.alphaMax() > 1 || this.alphaMin() < 0) {
			throw new RecepteurAnalogiqueMultiTrajetNonConforme("L'atténuation ne peut pas être négatif ou supérieure à 1");
		}
	}
	
	/**
	 * Méthode permettant de recevoir une information d'enlever du multi-trajet et de transmettre 
	 * @param information : L'information (de type double) recue
	 * @throws InformationNonConforme : L'information n'est pas conforme (exemple : information null)
	*/
	
	@Override
	public void recevoir(Information<Double> information)
			throws InformationNonConforme {
		// TODO Auto-generated method stub 
		if(information == null || information.nbElements() == 0) {
			throw new InformationNonConforme();

		}
		
		int nbEch = information.nbElements() - tauMax(); //Calcul du nombre d'échantillons utiles
		informationRecue = information;
		informationEmise = new Information <Double>(nbEch);


		Information<Double> infoTemp = new Information<Double>();
		
		//  NE PAS TOUCHER ! ET UTILISER infoTemp !! MERCI POUR MES NEURONNES :D 
		
		
		for(double val : informationRecue) {
			infoTemp.add(val);
		}
		
		for (int i = 0; i < nbEch; i++) //parcours des échantillons significatifs
		{
			double temp = infoTemp.iemeElement(i);
			for (int j = 0; j < tau.size(); j++)
			{
				int delta = tau.get(j);
				if (i>= delta)
				{
					temp -= alpha.get(j)*infoTemp.iemeElement(i-delta); //Si superposition de plusieurs trajets, on soustrait les multitrajets
					
				}
			}
			infoTemp.setIemeElement(i, temp); //Valeur "nettoyée" des multi trajets
		}
		
		
				for(int k = 0; k < nbEch; k++)
				{
					informationEmise.add(infoTemp.iemeElement(k)); // Construction de l'info émise
				}
				
				// On libère l'information temporaire (utile ou pas ?)
				infoTemp.vider();
				
			}

	/**
     * Méthode permettant d'émettre l'information emise (modifier au préalable par la methode recevoir)
     * @throws InformationNonConforme : L'information n'est pas conforme
     *
    */
	
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
		informationRecue.vider();
	}
	
	//Quelques methodes utiles pour connaitre les valeures min et max des listes 
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
