   package visualisations;
	
	
   import information.Information;


/** 
 * Classe réalisant le diagramme de l'oeil d'information composée d'éléments réels (double)
 * @author vallin
 */
   public class SondeAnalogiqueHistogramme extends Sonde <Integer> {
    
   
    /**
    * pour construire une sonde analogique  
    * @param nom  le nom de la fenêtre d'affichage
    */
      public SondeAnalogiqueHistogramme(String nom) {
         super(nom);
      }
   
   
      public void recevoir (Information <Integer> information) { 
         informationRecue = information;
         int nbElements = information.nbElements();
         double [] table = new double[nbElements];
         int i = 0;
         for (double f : information) {
            table[i] = f;
            i++;
         }
         new VueCourbe (table, nom); 
      }
   	 
   	
   
   
   }