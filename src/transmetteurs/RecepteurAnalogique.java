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
	 * Constructeur
	 * 
	 * @param min
	 * @param max
	 * @param forme
	 * @param nbEch
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

	@Override
	public void recevoir(Information<Float> information)
			throws InformationNonConforme {

		if (information == null) {
			throw new InformationNonConforme();
		}

		this.informationRecue = information;
		this.informationEmise = new Information<Boolean>();
		int j = 0;
		float somme = 0;
		if (forme.equalsIgnoreCase("RZ")) {
			System.out.println("nb bits : " + informationRecue.nbElements()/nbEch);
			for (float echantillon : informationRecue) {
				j++;
				
				if ((j - 1)> 5 * nbEch / 12 && (j-1) < 7 * nbEch / 12) {
					somme += echantillon;
				}
				
				
				if (j == nbEch) {
					//System.out.println("Somme : " + somme + "nb elts" + informationRecue.nbElements() + "");

					if (somme / (-1 + nbEch / 6) > 0.95 * max) // max sur
																	// les
																	// échantillons
																	// compris
																	// entre
																	// 5/12 et
																	// 7/12 de
																	// nbEch
					{
						informationEmise.add(true);
					} else
						informationEmise.add(false);

					j = 0;
					somme = 0;

				}
				
			}
		} 
		else if (forme.equalsIgnoreCase("NRZT")) {
			System.out.println("nb bits : " + informationRecue.nbElements()/nbEch);
			int i = 0;
			if (informationRecue.nbElements() <= nbEch) {
				for (float echantillon : informationRecue) {
					somme += echantillon;
				}
				if (somme > 0.95 * nbEch * max / 2)
					informationEmise.add(true);
				else
					informationEmise.add(false);
			} else {
				boolean first = true;
				for (float echantillon : informationRecue) {
j++;


					if (first) {
						if (j > nbEch / 3 && j < 5 * nbEch / 6) {
							somme += echantillon;
						}
						//first = false;
					} 
					else if (j > nbEch / 6 && j < 5 * nbEch / 6) {
						somme += echantillon;
					}
					
					if (j == nbEch) {
						i++;
						if (i< informationRecue.nbElements()/nbEch) {
						if (first) {
							if (somme > 0.95 * (nbEch / 2 - 1) * max) {
								informationEmise.add(true);
							} else
								informationEmise.add(false);

							first = false;
						} else if (somme > 0.95 * (4 * nbEch / 6 - 1) * max) {
							informationEmise.add(true);
						} else {
							informationEmise.add(false);
						}
						}
						else
						{
							if (somme > 0.95 * ( nbEch / 2 - 1) * max) {
								informationEmise.add(true);
							} else {
								informationEmise.add(false);
							}
							
						}
						j = 0;
						somme = 0;
					}
				}
			System.out.println("i : " + i + "nbech : " + nbEch);
			}

		} else if (forme.equalsIgnoreCase("NRZ")) {
			System.out.println("nb bits : " + informationRecue.nbElements()/nbEch);
			for (float echantillon : informationRecue) {
				j++;
				somme += echantillon;
				if (j == nbEch ) {
					if (somme / nbEch > 0.95 * max) {
						informationEmise.add(true);
					} else {
						informationEmise.add(false);
					}
					j = 0;
					somme = 0;
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