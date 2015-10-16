package transmetteurs;

import information.Information;
import information.InformationNonConforme;
import java.lang.Math;
import java.util.Iterator;

import destinations.*;

/**
 * Classe d'un emetteur parfait boolean vers double. Celui recois les informations binaire et les rend analogique par différents codage (NRZ, NRZT, RZ)
*/

public class EmetteurAnalogique extends Transmetteur<Boolean,Double> {

	
	/** Amplitude minimun pour l'echentillonage */
	private double min;
	/** Amplitude max pour l'echentillonage */
	private double max;
	/** type de codage */
	private String forme;
	/** Nombre d'enchentillons par symbole */
	private int nbEch;
	
	/**
	 * Constructeur qui initialise un Emetteur parfait
	 * @param min amplitude min du signal analagique généré
	 * @param max amplitude max du signal analagique généré
	 * @param forme type de codage du signal analogique (NRZ, NRZT ou RZ)
	 * @param nbEch nombre d'echentillon par symbole
	 * @throws EmetteurNonConforme Un des paramètres n'est pas conforme
	 */
	
	public EmetteurAnalogique(double min, double max, String forme, int nbEch) throws EmetteurNonConforme {
		super();
		
		if(min > max) {
			throw new EmetteurNonConforme("Amplitude min ne peut pas être supérieur à l'amplitude max");
		}
		
		if(forme==null) {
			throw new EmetteurNonConforme("La forme doit être spécifiée");
		}
		
		if(nbEch < 1) {
			throw new EmetteurNonConforme("Le nombre d'enchentillon doit être positif non nul");
		}
		
		// Si la forme ne match pas un de ce paramètres 
		if(!(forme.equalsIgnoreCase("NRZ") || forme.equalsIgnoreCase("NRZT") || forme.equalsIgnoreCase("RZ"))) {
			throw new EmetteurNonConforme("Le codage doit être NRZ, RZ ou NRZT");
		}
		
		this.min = min;
		this.max = max;
		this.forme = forme;
		this.nbEch = nbEch;
	}
	
	/**
	 * Méthode qui recois une information binaire, et la rend analogique en fonction des paramètre du constructeur
	 * @param information : L'information (de type boolean) recue de la source
	 * @throws InformationNonConforme : L'information n'est pas conforme (exemple : information null)
	 *
	*/
	
	@Override
	public void recevoir(Information<Boolean> information) throws InformationNonConforme {
		
		if(information == null) {
			throw new InformationNonConforme();
		}
		this.informationRecue = information;
		
		informationEmise = new Information<Double>(informationRecue.nbElements());
		
		if(forme.equalsIgnoreCase("NRZ")) {
			
			for(Boolean b : informationRecue) {
				for(int i=0;i<nbEch; i++) {
					if(b) {
						informationEmise.add(max);
					}
					else {
						informationEmise.add(min);
					}
				}
			}
		}
		else if(forme.equalsIgnoreCase("NRZT")) {
			
			if(informationRecue.nbElements()<2) {
				
				double moyenne = (max+min)/2;
				double coefMax = max - moyenne;
				double coefMin = moyenne - min;
				double prec = moyenne;
				
				if(informationRecue.iemeElement(0)) { // Si l'unique bit est 1
					for(int i=0; i<nbEch; i++) {
						/*if(i<nbEch * 1/3) {
							prec += (3*coefMax/nbEch);
							informationEmise.add(prec);
						}*/
						if(i<nbEch*2/3) {
							prec = max;
							informationEmise.add(max);
						}
						if(i> nbEch * 2/3) {
							prec -= (3*coefMax/nbEch);
							informationEmise.add(prec);
						}
					}
				}
				else // Sinon l'unique bit est à 0
				{
					for(int i=0; i<nbEch; i++) {
						/*if(i<nbEch * 1/3) {
							prec -= (3*Math.abs(coefMin)/nbEch);
							informationEmise.add(prec);
						}*/
						if(i<nbEch*2/3) {
							prec = min;
							informationEmise.add(min);
						}
						if(i> nbEch * 2/3) {
							prec += (3*Math.abs(coefMin)/nbEch);
							informationEmise.add(prec);
						}
					}
				}
			}
			else {

				// Prend le bit precedent et suivant (si le nombres de bits est supérieur à 1
				
				boolean bitPrec = informationRecue.iemeElement(0);
				boolean bitSuivant = informationRecue.iemeElement(1);
				
				double prec; 
				
				double moyenne = (max+min)/2;
				double coefMax = max - moyenne;
				double coefMin = moyenne - min;
				
				int i = 0;
				
				Iterator<Boolean> iterator = informationRecue.iterator();
				iterator.next();
				
				for(boolean b : informationRecue) {
					
					if(iterator.hasNext()) {
						bitSuivant = iterator.next();
					}
					
					prec = moyenne;

					for(int j=0;j<nbEch; j++) {
						
						if(b) { // Si le bit est 1
							
							// Si premier bit on le démarre de 0
							if(i == 0) {
								/*if(j<nbEch/3) {
									prec += (3*coefMax/nbEch);
									informationEmise.add(prec);
								}*/
								if(j<nbEch*5/6) {
									informationEmise.add(max);	
									prec = max;
								}
								
								// Si le suivant est un 1 on retourne pas à 0
								if(bitSuivant) {
									if(j>=nbEch*5/6) {
										informationEmise.add(max);
										prec = max;
									}
								}
								
								// On descend à 5/6 
								else {
									if(j>=nbEch*5/6) {
										prec -= (6*coefMax/nbEch);
										informationEmise.add(prec);
									}
								}
							}
							
							// Si on est entre le premier et dernier bit
							if(i>0 && i<(informationRecue.nbElements()-1)) {
								
								// Si le bit prec est à 1 on reste au max pendant 5/6 de temps
								if(bitPrec) {
									if(j<nbEch*5/6) {
										informationEmise.add(max);
										prec = max;
									}
									
									// Le prechain est à 1 on reste au max
									if(bitSuivant) {
										if(j>= nbEch*5/6) {
											informationEmise.add(max);
											prec = max;
										}
									}
									
									//Si le bit suivant est à 0 on descend vers 0
									if(!bitSuivant) {
										if(j>= nbEch*5/6) {
											prec -= (6*coefMax/nbEch);
											informationEmise.add(prec);
										}
									}
								}
								
								// Si le bit prec était 0, on monte de 0 à 1/6
								if(!bitPrec){
									if(j<nbEch*1/6) {
										prec += (6*coefMax/nbEch);
										informationEmise.add(prec);
									}
									
									// De 1/6 à 5/6 on reste au max
									if(j>= nbEch*1/6 && j<nbEch*5/6) {
										informationEmise.add(max);
										prec = max;
									}
									
									// Si le bit suivant est à 1 on reste au max
									if(bitSuivant) {
										if(j>=nbEch*5/6) {
											informationEmise.add(max);
											prec = max;
										}
									}
									
									//Sinon on redescend
									if(!bitSuivant) {
										if(j>= nbEch*5/6) {
											prec -= 6*coefMax/nbEch;
											informationEmise.add(prec);
										}
									}
									
								}
							}
							if(i == (informationRecue.nbElements()-1) ) { // Le dernier bit
								if(!bitPrec) { // Si le bit prec est 0
									if(j<nbEch*1/6) { // On remonte de 0 à 1/6
										prec += 6*coefMax/nbEch;
										informationEmise.add(prec);
									}
									if(j>=nbEch*1/6 && j<nbEch * 2/3) { // On reste au max jusqu'à 2/3
										informationEmise.add(max);
										prec = max;
									}
								}
								if(bitPrec) { // Si le bit prec est 1
									if(j<nbEch * 2/3) { // On reste au max de 0 à 2/3
										informationEmise.add(max);
										prec = max;
									}
								}
								if(j>=nbEch * 2/3) { // On descend de 2/3 à 1
									//prec -= 3*coefMax/nbEch;
									//informationEmise.add(prec);
									informationEmise.add(max);
								}
							}
						}
						else { // Sinon le bit est 0
	
							// Si premier bit on le démarre de 0
							if(i == 0) {
								
								/*if(j<nbEch/3) {
									if(min > 0) {
										prec += 3*Math.abs(coefMin)/nbEch;
									}
									else {
										prec -= 3*Math.abs(coefMin)/nbEch;
									}
									
									informationEmise.add(prec);
								}*/
								if(j<nbEch*5/6) {
									informationEmise.add(min);
									prec = min;
								}
								
								// Si le suivant est un 0 on retourne pas à 0
								if(!bitSuivant) {
									if(j>=nbEch*5/6) {
										informationEmise.add(min);
										prec = min;
									}
								}
								
								// On remonte à 5/6 
								else {
									if(j>=nbEch*5/6) {
										prec += 6*coefMin/nbEch;
										informationEmise.add(prec);
									}
								}
							}
							
							// Si on est entre le premier et dernier bit
							if(i>0 && i<(informationRecue.nbElements()-1)) {
								
								// Si le bit prec est à 0 on reste au min pendant 5/6 de temps
								if(!bitPrec) {
									if(j<nbEch*5/6) {
										informationEmise.add(min);
										prec = min;
									}
									
									// Le prechain est à 0 on reste au min
									if(!bitSuivant) {
										if(j>= nbEch*5/6) {
											informationEmise.add(min);
											prec = min;
										}
									}
									
									//Si le bit suivant est à 1 on remonte vers 0
									if(bitSuivant) {
										if(j>= nbEch*5/6) {
											prec += 6*coefMin/nbEch;
											informationEmise.add(prec);
										}
									}
								}
								
								// Si le bit prec était 1, on monte de 0 à 1/6
								if(bitPrec){
									if(j<nbEch*1/6) {
										prec -= 6*coefMin/nbEch;
										informationEmise.add(prec);
									}
									
									// De 1/6 à 5/6 on reste au min
									if(j>= nbEch*1/6 && j<nbEch*5/6) {
										informationEmise.add(min);
										prec = min;
									}
									
									// Si le bit suivant est à 0 on reste au max
									if(!bitSuivant) {
										if(j>=nbEch*5/6) {
											informationEmise.add(min);
											prec = min;
										}
									}
									
									//Sinon on remonte
									if(bitSuivant) {
										if(j>= nbEch*5/6) {
											prec += 6*coefMin/nbEch;
											informationEmise.add(prec);
										}
									}
									
								}
							}
							if(i == (informationRecue.nbElements()-1) ) { // Le dernier bit
								if(bitPrec) { // Si le bit prec est 1
									if(j<nbEch*1/6) { // On descend de 0 à 1/6
										prec -= 6*coefMin/nbEch;
										informationEmise.add(prec);
									}
									if(j>=nbEch*1/6 && j<nbEch * 2/3) { // On reste au min jusqu'à 2/3
										informationEmise.add(min);
										prec = min;
									}
								}
								if(!bitPrec) { // Si le bit prec est 0
									if(j<nbEch * 2/3) { // On reste au min de 0 à 2/3
										informationEmise.add(min);
										prec = min;
									}	
								}
								
								if(j>=nbEch * 2/3) { // de 2/3 à 1 on remonte 
									/*if(min > 0) {
										prec -= 3*Math.abs(coefMin)/nbEch;
									}
									else {
										prec += 3*Math.abs(coefMin)/nbEch;
									}
									informationEmise.add(prec);*/
									informationEmise.add(min);
								}
							}
						}
					}
					bitPrec = b;
					i++;
				}
			}
		}
		else if(forme.equalsIgnoreCase("RZ")) {

			// Constante pour le calcul de la densité de proba de la gaussienne pour optimiser les calculs ...
			Double sigma = (double) (nbEch/18);
			Double csteMax = 1/(sigma*Math.sqrt(2*Math.PI)) * max;
			Double csteMin = 1/(sigma*Math.sqrt(2*Math.PI)) * min;
			
			Double moyenneSurSigma = nbEch/(2*sigma);
			
			for(Boolean b : informationRecue) {
				
				for(int i=0;i<nbEch; i++) {
					if(b) { // Si le bit est 1 
						
						//Gaussienne pour faire une impultion 
						Double valeur = csteMax * Math.exp(-0.5 * Math.pow(((i/sigma)-moyenneSurSigma), 2));
						informationEmise.add(valeur);
			
					}
					else { //si le bit est 0
						
						//Gaussienne pour faire une impultion 
						Double valeur = csteMin * Math.exp(-0.5 * Math.pow(((i/sigma)-moyenneSurSigma), 2));
						informationEmise.add(valeur);
						
					}
				}
			}
		}
	}
	
	/**
         * Méthode permettant d'émettre l'information analogique généré sans la modifier
         * @throws InformationNonConforme : L'information n'est pas conforme
         *
        */
		
	@Override
	public void emettre() throws InformationNonConforme {
		for (DestinationInterface<Double> destinationConnectee : destinationsConnectees) {
            destinationConnectee.recevoir(informationEmise);
         }
	}
	
	/**
     * recycle la RAM en libérant les information stockée 
     */
    
	   	public void recyclerRAM() {
			this.informationEmise.vider();
		}
}
