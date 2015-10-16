package transmetteurs;

import java.util.ArrayList;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConforme;

public class TransmetteurAnalogiqueMultiTrajet extends Transmetteur<Double, Double>{

	/** liste des décalages */
	private ArrayList<Integer> tau;
	/** liste des atténuations*/
	private ArrayList<Double> alpha;
	
	/**
	 * Constructeur pour l'ajout de multi-trajet dans une information reçue
	 * @param tau la liste décalages des multi-trajets
	 * @param alpha la liste amplitudes des multi-trajets
	 * @throws TransmetteurAnalogiqueMultiTrajetNonConforme les paramètres ne sont pas valide : les deux tableaux doivent 
	 * être compris entre 1 et 5 éléments, ils doivent être de taille identique et la tau doit être supérieur à 1. Alpha compris entre 0 et 1
	 */
	public TransmetteurAnalogiqueMultiTrajet(ArrayList<Integer> tau, ArrayList<Double> alpha) throws TransmetteurAnalogiqueMultiTrajetNonConforme {
			
		if(tau.size() < 1 || tau.size() > 5 || alpha.size() > 5 || alpha.size() < 1) {
			throw new TransmetteurAnalogiqueMultiTrajetNonConforme("On ne peut avoir qu'entre 1 et 5 multitrajet max");	
		}
		
		if(tau.size() != alpha.size()) {
			throw new TransmetteurAnalogiqueMultiTrajetNonConforme("On doit forcement avoir 1 tau associé à 1 alpha");
		}
		
		this.tau = tau;
		this.alpha = alpha;
		
		if(this.tauMin() < 1) {
			throw new TransmetteurAnalogiqueMultiTrajetNonConforme("Le décalage ne peux pas être négatif !");
		}
		
		if(this.alphaMax() > 1 || this.alphaMin() < 0) {
			throw new TransmetteurAnalogiqueMultiTrajetNonConforme("L'atténuation ne peut pas être négatif ou supérieure à 1");
		}
	}
	
	/**
	 * Méthode permettant de recevoir une information d'ajouter du multi-trajet et de transmettre 
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
		
		//Calcul du tau max !
		int tauMax = this.tauMax();
		
		informationRecue = information;
		informationEmise = new Information<Double>(informationRecue.nbElements() + tauMax);
		
		// Initialisation de l'info qu'on va emettre 
		for(int i = 0; i<(informationRecue.nbElements() + tauMax); i++) {
			if(i<informationRecue.nbElements()) {
				informationEmise.add(informationRecue.iemeElement(i));
			}
			else {
				informationEmise.add(0.);
			}
		}
		
		int j=0;
		
		// Pour chaque valeur de tau, on va rajouter le multi-trajet (décaler de tau)
		for(int val : tau) {
			
			for(int i=0; i<=informationEmise.nbElements(); i++) {	
				if(i>=val && i<informationRecue.nbElements() + val) {
					
					double valeurPrec = informationEmise.iemeElement(i);
					double bruitMultiTrajet = informationRecue.iemeElement(i-val);
					bruitMultiTrajet *= alpha.get(j);
					
					valeurPrec += bruitMultiTrajet;
					
					// Et on remplace dans l'information à emettre 
					informationEmise.setIemeElement(i, valeurPrec);
				}
			}
			
			j++;
		}
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
	}
	
	
	// Quelques methodes utiles pour connaitre les valeures min et max des listes 
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
