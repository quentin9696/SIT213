package transmetteurs;

import java.util.Random;

import information.Information;
import information.InformationNonConforme;

import destinations.*;

/**
 * Classe d'un transmetteur parfait. Celui se contente de recevoir un message et le transmet à un tous les destinataire qui lui sont rattaché
*/

public class TransmetteurAnalogiqueBruite extends Transmetteur<Double,Double> {

	/** Valeur du SNR (linéaire) */
	private double snr;
	private long seed;
	private boolean avecBruit; 
	
	/**
	 * Constructeur d'un transmetteur analogique bruité
	 * @param snr La valeur dur SNR en log
	 * @throws TransmetteurAnalogiqueBruiteNonConforme Le SNR en linéaire doit être positif non nul
	 */
	public TransmetteurAnalogiqueBruite(double snr) throws TransmetteurAnalogiqueBruiteNonConforme {
		super();
		
		this.snr = Math.pow(10, snr/10);
		avecBruit = false;
		
		if(this.snr <= 0) {
			throw new TransmetteurAnalogiqueBruiteNonConforme("SNR strictement positif non nul");
		}
	}
	
	public TransmetteurAnalogiqueBruite(double snr, long seed) throws TransmetteurAnalogiqueBruiteNonConforme {
		super();
		
		this.snr = Math.pow(10, snr/10);
		this.seed = seed;
		avecBruit = true;
		
		if(this.snr <= 0) {
			throw new TransmetteurAnalogiqueBruiteNonConforme("SNR strictement positif non nul");
		}
	}
	
	/**
	 * Méthode permettant de revevoir une information d'ajouter du bruit (blanc gaussien) et de transmettre 
	 * @param information : L'information (de type double) recue de l'emetteur
	 * @throws InformationNonConforme : L'information n'est pas conforme (exemple : information null)
	 * @throws  ez
	 *
	*/
	@Override
	public void recevoir(Information<Double> information)
			throws InformationNonConforme {
		
		if(information == null || information.nbElements() == 0) {
			throw new InformationNonConforme();
		}

		this.informationRecue = information;
		informationEmise = new Information<Double>(informationRecue.nbElements());
		
		double puissanceSignalMoyen = 0.0f;
		
		// Calcul de la puissance moyenne du signal
		for(double valeur : informationRecue) {
			puissanceSignalMoyen += Math.pow(valeur,2);
		}
		
		puissanceSignalMoyen /= informationRecue.nbElements();
		
		// Calcul de la puissance moyenne du bruit blanc gaussien
		double puissanceBruitMoyen = puissanceSignalMoyen/snr;
		
		double sigma = Math.sqrt(puissanceBruitMoyen);
		
		Random a1 = new Random();
		Random a2 = new Random();
		
		//Déclaraion de 2 loi uniforme
		if(avecBruit) {
			a1.setSeed(seed);
			a2.setSeed(seed);
		}
		
		
		for(double signal : informationRecue) {
			//Calcul du bruit et ajout au signal
			signal += sigma*Math.sqrt(-2*Math.log(1-a1.nextDouble()))*Math.cos(2*Math.PI*a2.nextDouble());
			informationEmise.add(signal);
		}
	}
	
	/**
         * Méthode permettant d'émettre l'information emise (modifier au préalable par la methode recevoir)
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
			this.informationRecue.vider();
		}

}