package transmetteurs;

import information.Information;
import information.InformationNonConforme;

import destinations.*;

/**
 * Classe d'un transmetteur parfait. Celui se contente de recevoir un message et
 * le transmet à un tous les destinataire qui lui sont rattaché
 */

public class RecepteurAnalogique extends Transmetteur<Double, Boolean> {

	/**
	 * Méthode permettant de revevoir une information et le stocker dans
	 * l'attribut informationRecue
	 * 
	 * @param information
	 *            : L'information (de type boolean) recue de la source
	 * @throws InformationNonConforme
	 *             : L'information n'est pas conforme (exemple : information
	 *             null)
	 * 
	 */

	private double min;
	private double max;
	private String forme;
	private int nbEch;

	/**
	 * Constructeur qui initialise un Recepteur parfait
	 * 
	 * @param min
	 *            amplitude min du signal analagique généré
	 * @param max
	 *            amplitude max du signal analagique généré
	 * @param forme
	 *            type de codage du signal analogique (NRZ, NRZT ou RZ)
	 * @param nbEch
	 *            nombre d'echantillons par symbole
	 * @throws RecepteurNonConforme
	 *             Un des paramètres n'est pas conforme
	 */

	public RecepteurAnalogique(double min, double max, String forme, int nbEch)
			throws RecepteurNonConforme {
		super();
		if (min > max) {
			throw new RecepteurNonConforme(
					"Amplitude min ne peut pas être supérieur à l'amplitude max");
		}

		if (forme == null) {
			throw new RecepteurNonConforme("La forme doit être spécifiée");
		}

		if (nbEch < 1) {
			throw new RecepteurNonConforme(
					"Le nombre d'enchentillon doit être positif non nul");
		}

		// Si la forme ne match pas un de ce paramètres
		if (!(forme.equalsIgnoreCase("NRZ") || forme.equalsIgnoreCase("NRZT") || forme
				.equalsIgnoreCase("RZ"))) {
			throw new RecepteurNonConforme(
					"Le codage doit être NRZ, RZ ou NRZT");
		}
		this.min = min;
		this.max = max;
		this.forme = forme;
		this.nbEch = nbEch;
	}

	/**
	 * Méthode qui recois une information analogique, et la rend binaire en
	 * fonction des paramètre du constructeur
	 * 
	 * @param information
	 *            : L'information (de type analogique) recue du transmetteur
	 * @throws InformationNonConforme
	 *             : L'information n'est pas conforme (exemple : information
	 *             null)
	 * 
	 */

	@Override
	public void recevoir(Information<Double> information)
			throws InformationNonConforme {

		if (information == null) {
			throw new InformationNonConforme();
		}

		this.informationRecue = information;
		this.informationEmise = new Information<Boolean>(informationRecue.nbElements());
		int j = 0;
		int i = 0;
		double somme = 0;
		double esperance = (max + min) / 2;
		if (forme.equalsIgnoreCase("RZ")) // Cas du signal RZ
		{
			for (double echantillon : informationRecue) {
				j++;
				if (j >= 1*nbEch/3  && j < 2*nbEch/3)
				{
					somme += echantillon;
					i++;
				}
				if (j == nbEch) {
					if (somme  > esperance) {
						informationEmise.add(true);
					} else
						informationEmise.add(false);

					j = 0;
					i = 0;
					somme = 0;

				}

			}
		} else if (forme.equalsIgnoreCase("NRZT")) // cas NRZT
		{
			
			for (double echantillon : informationRecue) {
				j++;
				
						somme += echantillon;
				
						if (j == nbEch) {
							if (somme / nbEch > esperance) {
								informationEmise.add(true);
							} else
								informationEmise.add(false);
						
						
						j = 0;
						somme = 0;	
			}
				

				
			}
		} else if (forme.equalsIgnoreCase("NRZ")) { // signal de forme NRZ
			{
				for (double echantillon : informationRecue) {
					j++;
					somme += echantillon;
					if (j == nbEch) {
						if (somme / nbEch > esperance) {
							informationEmise.add(true);
						} else
							informationEmise.add(false);

						j = 0;
						somme = 0;

					}

				}
			}
		}

	}

	/**
	 * Méthode permettant d'émettre l'information recue sans la modifier
	 * 
	 * @throws InformationNonConforme
	 *             : L'information n'est pas conforme
	 * 
	 */

	@Override
	public void emettre() throws InformationNonConforme {

		for (DestinationInterface<Boolean> destinationConnectee : destinationsConnectees) {
			destinationConnectee.recevoir(informationEmise);
		}
	}

	/**
     * recycle la RAM en libérant les information stockée 
     */
    
	   	public void recyclerRAM() {
			this.informationRecue.vider();
		}
}