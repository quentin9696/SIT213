package transmetteurs;

import information.Information;
import information.InformationNonConforme;
import java.lang.Math;

import destinations.*;

/**
 * Classe d'un transmetteur parfait. Celui se contente de recevoir un message et le transmet à un tous les destinataire qui lui sont rattaché
*/

public class EmetteurAnalogique extends Transmetteur<Boolean,Float> {

	/**
	 * Méthode permettant de revevoir une information  et le stocker dans l'attribut informationRecue
	 * @param information : L'information (de type boolean) recue de la source
	 * @throws InformationNonConforme : L'information n'est pas conforme (exemple : information null)
	 *
	*/
	
	private float min;
	private float max;
	private String forme;
	private int nbEch;
	
	/**
	 * Constructeur
	 * @param min 
	 * @param max
	 * @param forme
	 * @param nbEch
	 */
	
	public EmetteurAnalogique(float min, float max, String forme, int nbEch) {
		super();
		
		this.min = min;
		this.max = max;
		this.forme = forme;
		this.nbEch = nbEch;
	}
	
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
			for(Boolean b : informationRecue) {
				float prec = 0.0f; 
				for(int i=0;i<nbEch; i++) {
					if(b) { // Si le bit est 1
						if(i<nbEch/3) {
							prec += (3*max/nbEch);
							informationEmise.add(prec);
						}
						if(i>= nbEch/3 && i<nbEch*2/3) {
							informationEmise.add(max);	
						}
						if(i>=nbEch*2/3) {
							prec -= (3*max/nbEch);
							informationEmise.add(prec);
						}
					}
					else { // Sinon le bit est 0
						if(i<nbEch/3) {
							prec -= Math.abs(3*min/nbEch);
							informationEmise.add(prec);
						}
						if(i>= nbEch/3 && i<nbEch*2/3) {
							informationEmise.add(min);
						}
						if(i>=nbEch*2/3) {
							prec += Math.abs(3*min/nbEch);
							informationEmise.add(prec);
						}
					}
				}
			}
		}
		else if(forme.equalsIgnoreCase("RZ")) {
			float prec = 0.0f;

			for(Boolean b : informationRecue) {
				prec = 0.0f;
				for(int i=0;i<nbEch; i++) {
					if(b) { // Si le bit est 1 
						if(i< nbEch/6) {
							prec += (6*max/nbEch);
							informationEmise.add(prec);
						}
						if(i> nbEch/6 && i < nbEch/3) {
							informationEmise.add(max);
						}
						if(i > nbEch/3 && i < nbEch/2) {
							prec-= (6*max/nbEch);
							informationEmise.add(prec);
						}if(i > nbEch/2) {
							informationEmise.add(0.f);
						}
			
					}
					else { //si le bit est 0
						if(i< nbEch/6) {
							prec -= Math.abs(6*min/nbEch);
							informationEmise.add(prec);
						}
						if(i> nbEch/6 && i < nbEch/3) {
							informationEmise.add(min);
						}
						if(i > nbEch/3 && i < nbEch/2) {
							prec+= Math.abs(6*min/nbEch);
							informationEmise.add(prec);
						}if(i > nbEch/2) {
							informationEmise.add(0.f);
						}
						
					}
				}
			}

		}
		
		this.emettre();
	}
	
	/**
         * Méthode permettant d'émettre l'information recue sans la modifier
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
