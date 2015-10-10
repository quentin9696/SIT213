import information.Information;

import java.util.LinkedList;
import java.util.Random;

import visualisations.SondeAnalogiqueHistogramme;


public class Histogramme {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		SondeAnalogiqueHistogramme sondeHisto =  new SondeAnalogiqueHistogramme("Histogramme de merde !");
		float sigma = 1;
		Information<Float> histo = new Information<Float>();
		LinkedList<Float> valeur = new LinkedList<>();
		
		//Déclaraion de 2 loi uniforme
		Random a1 = new Random();
		Random a2 = new Random();
		
		for(int i = 0; i<30*100; i++) {
			//Calcul du bruit et ajout au signal
			valeur.add((float) (sigma*Math.sqrt(-2*Math.log(1-a1.nextFloat()))*Math.cos(2*Math.PI*a2.nextFloat())));
		}
		
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

		
		float interval = (max - min)/50;
		
		for (float val : valeur)
		{
			int indice = Math.round((val-min)/interval);
			histo.setIemeElement(indice, histo.iemeElement(indice) + 1);
		}
		
		sondeHisto.recevoir(histo);
	}

}
