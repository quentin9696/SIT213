import information.Information;

import java.util.ArrayList;
import java.util.Random;

import visualisations.SondeAnalogiqueHistogramme;


public class Histogramme {

	/**
	 * Méthode main qui génère la densité de probabilité du bruit généré par la classe TransmetteurAnalogiqueBruite
	 * @param args aucun argument n'est requis
	 */
	public static void main(String[] args) {
		
		SondeAnalogiqueHistogramme sondeHisto =  new SondeAnalogiqueHistogramme("Histogramme");
		float sigma = 1;
		Information<Float> histo = new Information<Float>(100);
		ArrayList<Float> valeur = new ArrayList<>(30*1000000);
		
		//Déclaraion de 2 loi uniforme
		Random a1 = new Random();
		Random a2 = new Random();
		
		for(int i = 0; i<30*1000000; i++) {
			//Calcul du bruit et ajout au signal
			valeur.add((float) (sigma*Math.sqrt(-2*Math.log(1-a1.nextFloat()))*Math.cos(2*Math.PI*a2.nextFloat())));
		}
		
		
		// Récupération du min et max pour faire l'histogramme
		float max = valeur.get(0); 
		float min = valeur.get(0);
		
		for(int i=0; i<valeur.size(); i++) {
			if(min > valeur.get(i)) {
				min = valeur.get(i);
			}
			
			
			if(max < valeur.get(i)) {
				max = valeur.get(i);
			}
		}
		
		//initialisation histogramme
		for (int i = 0; i<100; i++)
		{
			histo.add(0.f);	
		}
		
		//Intervalle séparant deux colonnes de l'histo
		float interval = (max - min)/100;
		
		//Répartition des valeurs du bruit dans les buckets
		for (float val : valeur)
		{
			int indice = Math.round((val-min)/interval);
			if (indice == 100)
				indice --;
			histo.setIemeElement(indice, histo.iemeElement(indice) + 1);
		}
		
		// Affichage 
		sondeHisto.recevoir(histo);
	}

}
