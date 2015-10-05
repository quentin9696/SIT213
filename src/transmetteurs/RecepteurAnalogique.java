package transmetteurs;

import information.Information;
import information.InformationNonConforme;

import destinations.*;

/**
 * Classe d'un transmetteur parfait. Celui se contente de recevoir un message et
 * le transmet à un tous les destinataire qui lui sont rattaché
 */

public class RecepteurAnalogique extends Transmetteur<Float, Boolean> {

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

	private float min;
	private float max;
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

	public RecepteurAnalogique(float min, float max, String forme, int nbEch)
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
	public void recevoir(Information<Float> information)
			throws InformationNonConforme {

		if (information == null) {
			throw new InformationNonConforme();
		}

		this.informationRecue = information;
		this.informationEmise = new Information<Boolean>();
		int j = 0;
		int i = 0;
		float somme = 0;
		float esperance = (max + min) / 2;
		if (forme.equalsIgnoreCase("RZ")) // Cas du signal RZ
		{
			for (float echantillon : informationRecue) {
				j++;
				if (j >= 5*nbEch/12  && j < 7*nbEch/12)
				{
					somme += echantillon;
					i++;
				}
				if (j == nbEch) {
					if (somme/i  > esperance) {
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
			int nbBits = informationRecue.nbElements()/nbEch;
			
			for (float echantillon : informationRecue) {
				j++;
				if (i == 0)
				{
					if (j > nbEch/3)
						somme += echantillon;
				}
				else if (i == nbBits-1) 
				{
					if (j < 2*nbEch/3)
					somme += echantillon;
				}
				else
				{
					somme += echantillon;
				}
				if (j == nbEch) {
					if (i == 0)
					{
						if (somme / (2*nbEch/3) > esperance) {
							informationEmise.add(true);
						} else
							informationEmise.add(false);
					}
					else if (i == nbBits - 1)
					{
						if (somme / (2*nbEch/3) > esperance) {
							informationEmise.add(true);
						} else
							informationEmise.add(false);
						
					}
					else if (somme / nbEch > esperance) {
						informationEmise.add(true);
					} else
						informationEmise.add(false);

					j = 0;
					i++;
					somme = 0;

				}
			}
		} else if (forme.equalsIgnoreCase("NRZ")) { // signal de forme NRZ
			{
				for (float echantillon : informationRecue) {
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

		this.emettre();

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

}