package sources;

import java.util.Random;

import information.Information;
/**
 * Source fixe, qui emmet une information préablement défini par l'utilisateur (valeures binaire).
 *
*/

public class SourceFixe extends Source<Boolean> {
	
	/**
	 * Constructeur de la source fixe, prend une chaine de caractères composée de 0 et de 1 puis transforme en objet Information
	 * @param message : Chaine de caractère correspondant à un message binaire (que des 0 ou 1). Tout autre caractère sera transformé en 1
	 *
	*/
	public SourceFixe(String message) {
		super();
	
		informationGeneree = new Information<Boolean>();
	 	
		char[] tabMessage = message.toCharArray();
		
		for(char c : tabMessage) {
			if(c == '0') 
				informationGeneree.add(false);
			else
				informationGeneree.add(true);
				
		}
		
	}
	
}
