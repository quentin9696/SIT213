package sources;

import java.util.Random;

import information.Information;

/**
 * Classe d'une source générant de l'aléatoire. L'aléatoire généré une pseudo aléatoire suivant une loi uniforme.
 * @author Quentin Vallin, Yannick Omnès
 *
*/

public class SourceAleatoire extends Source <Boolean> {

	/**
	 * Constructeur pour généré un séquance avec une seed quelquonque. 
	 * @param taille : Taille de l'information à générer
	 *
	*/

	public SourceAleatoire(int taille) {
		super();
		informationGeneree = new Information<Boolean>();
	 	
		Random rdm = new Random();
		
		for(int i=0; i<taille; i++)
		{
			informationGeneree.add(rdm.nextBoolean());
		}
		
	}
	
	/**
         * Constructeur pour généré un séquance avec une seed donné. 
         * @param taille : Taille de l'information à générer
         * @param seed : Le germe permettant la génération du pseudo aléatoire
        */

	public SourceAleatoire(int taille, long seed) {
		super();
		informationGeneree = new Information<Boolean>();
	 	
		Random rdm = new Random(seed);
		
		for(int i=0; i<taille; i++)
		{
			informationGeneree.add(rdm.nextBoolean());
		}
	}
}
