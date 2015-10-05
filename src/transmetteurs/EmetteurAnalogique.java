package transmetteurs;

import information.Information;
import information.InformationNonConforme;
import java.lang.Math;

import destinations.*;

/**
 * Classe d'un emetteur parfait boolean vers float. Celui recois les informations binaire et les rend analogique par différents codage (NRZ, NRZT, RZ)
*/

public class EmetteurAnalogique extends Transmetteur<Boolean,Float> {

	
	/** Amplitude minimun pour l'echentillonage */
	private float min;
	/** Amplitude max pour l'echentillonage */
	private float max;
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
	
	public EmetteurAnalogique(float min, float max, String forme, int nbEch) throws EmetteurNonConforme {
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
		
		informationEmise = new Information<Float>();
		
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
				
				float prec = 0.0f;
				
				if(informationRecue.iemeElement(0)) { // Si l'unique bit est 1
					for(int i=0; i<nbEch; i++) {
						if(i<nbEch * 1/3) {
							prec += (3*max/nbEch);
							informationEmise.add(prec);
						}
						if(i>nbEch * 1/3 && i<nbEch*2/3) {
							prec = max;
							informationEmise.add(max);
						}
						if(i> nbEch * 2/3) {
							prec -= (3*max/nbEch);
							informationEmise.add(prec);
						}
					}
				}
				else // Sinon l'unique bit est à 0
				{
					for(int i=0; i<nbEch; i++) {
						if(i<nbEch * 1/3) {
							prec += (3*min/nbEch);
							informationEmise.add(prec);
						}
						if(i>nbEch * 1/3 && i<nbEch*2/3) {
							prec = min;
							informationEmise.add(min);
						}
						if(i> nbEch * 2/3) {
							prec -= (3*min/nbEch);
							informationEmise.add(prec);
						}
					}
				}
			}
			else {

				// Prend le bit precedent et suivant (si le nombres de bits est supérieur à 1
				
				boolean bitPrec = informationRecue.iemeElement(0);
				boolean bitSuivant = informationRecue.iemeElement(1);
				
				float prec; 
				
				float moyenne = (max+min)/2;
				float coefMax = max - moyenne;
				float coefMin = moyenne - min;
				
				for(int i=0; i<informationRecue.nbElements(); i++) {
					boolean b = informationRecue.iemeElement(i);
					
					if(i<(informationRecue.nbElements()-1)) {
						bitSuivant = informationRecue.iemeElement(i+1);
					}
					
					prec = moyenne;
					
					if(i == 0) {
						 prec = 0.0f;
					 }
					
					for(int j=0;j<nbEch; j++) {
						
						if(b) { // Si le bit est 1
							
							// Si premier bit on le démarre de 0
							if(i == 0) {
								if(j<nbEch/3) {
									prec += (3*max/nbEch);
									informationEmise.add(prec);
								}
								if(j>= nbEch/3 && j<nbEch*5/6) {
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
									prec -= 3*max/nbEch;
									informationEmise.add(prec);
								}
							}
						}
						else { // Sinon le bit est 0
	
							// Si premier bit on le démarre de 0
							if(i == 0) {
								if(j<nbEch/3) {
									if(min > 0) {
										prec += 3*Math.abs(min)/nbEch;
									}
									else {
										prec -= 3*Math.abs(min)/nbEch;
									}
									
									informationEmise.add(prec);
								}
								if(j>= nbEch/3 && j<nbEch*5/6) {
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
									if(min > 0) {
										prec -= 3*Math.abs(min)/nbEch;
									}
									else {
										prec += 3*Math.abs(min)/nbEch;
									}
									informationEmise.add(prec);
								}
							}
						}
					}
					bitPrec = b;
				}
			}
		}
		else if(forme.equalsIgnoreCase("RZ")) {
			float prec = 0.0f;

			for(Boolean b : informationRecue) {
				prec = 0.0f;
				
				for(int i=0;i<nbEch; i++) {
					if(b) { // Si le bit est 1 
						if(i< nbEch * 1/3) { // Reste à 0 de 0 à 1/3
							informationEmise.add(0.0f);
						}
						if(i>= nbEch* 1/3 && i < nbEch * 5/12) { // On monte 1/3 à 5/12
							prec += (12*max/nbEch);
							
							/*if(prec > max) {
								prec = max;
							}*/
							
							informationEmise.add(prec);
						}
						if(i >= nbEch * 5/12 && i < nbEch* 7/12 ) { // On reste au max de 5/12 à 7/12
							informationEmise.add(max);
							prec = max;
						}
						if(i>= nbEch * 7/12 && i< nbEch * 2/3) { // On descend de 7/12 à 2/3
							prec-= (12*max/(nbEch));
							
							/*/if(prec < 0) {
								prec = 0.0f;
							}*/
							informationEmise.add(prec);
						}
						if(i >= (nbEch * 2/3)) { //Reste à 0 de 2/3 à 1
							informationEmise.add(0.0f);
						}
			
					}
					else { //si le bit est 0
						if(i< nbEch * 1/3) { //on rste à 0 sur 0 à 1/3
							informationEmise.add(0.0f);
						}
						
						if(i>= nbEch* 1/3 && i < nbEch * 5/12) { // On monte de 1/3 à 5/12
							prec -= Math.abs(12*min/nbEch);
							
							/*if(prec < min) {
								prec = min;
							}*/
							
							informationEmise.add(prec);
						}
						if(i >= nbEch * 5/12 && i < nbEch* 7/12 ) { // On bloque de 5/12 à 7/12
							informationEmise.add(min);
							prec = min;
						}
						if(i>= nbEch * 7/12 && i< nbEch * 2/3) { // On redescend de 7/12 à 2/3
							prec += Math.abs(12*min/(nbEch));
							
							/*if(prec > 0) {
								prec = 0.0f;
							}*/
							
							informationEmise.add(prec);
						}
						if(i >= (nbEch * 2/3)) { // On reste à 0 sur 2/3 à 1
							informationEmise.add(0.0f);
						}
						
					}
				}
			}
		}
		
		this.emettre();
	}
	
	/**
         * Méthode permettant d'émettre l'information analogique généré sans la modifier
         * @throws InformationNonConforme : L'information n'est pas conforme
         *
        */
		
	@Override
	public void emettre() throws InformationNonConforme {
		
		for (DestinationInterface<Float> destinationConnectee : destinationsConnectees) {
            destinationConnectee.recevoir(informationEmise);
         }
	}

}
