package transmetteurs;

import java.util.Random;

import information.Information;
import information.InformationNonConforme;

import destinations.*;

/**
 * Classe d'un transmetteur parfait. Celui se contente de recevoir un message et le transmet à un tous les destinataire qui lui sont rattaché
*/

public class TransmetteurAnalogiqueBruite extends Transmetteur<Float,Float> {

	/**
	 * Méthode permettant de revevoir une information  et le stocker dans l'attribut informationRecue
	 * @param information : L'information (de type boolean) recue de la source
	 * @throws InformationNonConforme : L'information n'est pas conforme (exemple : information null)
	 *
	*/
	float snr;
	public TransmetteurAnalogiqueBruite(float snr) throws TransmetteurAnalogiqueBruiteNonConforme {
		super();
		informationEmise = new Information<Float>();
		this.snr = (float) Math.pow(10, snr/10);
	}
	
	@Override
	public void recevoir(Information<Float> information)
			throws InformationNonConforme {
		
		if(information == null || information.nbElements() == 0) {
			throw new InformationNonConforme();
		}

		this.informationRecue = information;
		//this.informationEmise = informationRecue;
		
		float puissanceSignalMoyen = 0.0f;
		
		for(float valeur : informationRecue) {
			puissanceSignalMoyen += valeur * valeur;
		}
		
		puissanceSignalMoyen /= informationRecue.nbElements();
		
		float puissanceBruitMoyen = puissanceSignalMoyen/snr;
		
		float sigma = (float) Math.sqrt(puissanceBruitMoyen);
		
		Random a1 = new Random();
		Random a2 = new Random();
		
		for(float signal : informationRecue) {
			signal += sigma*Math.sqrt(-2*Math.log(1-a1.nextFloat()))*Math.cos(2*Math.PI*a2.nextFloat());
			informationEmise.add(signal);
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