import java.util.LinkedList;
import java.util.Random;


public class Hitograme {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		float sigma = 1;
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

		float[][] histo = new float[50][2];
		
		float interval = (max - min )/50;
		
		for(int i=0; i<50; i++) {
			histo[i][0] = min + (i*interval);
			System.out.println(histo[i][0]);
		}
		
		
	}

}
