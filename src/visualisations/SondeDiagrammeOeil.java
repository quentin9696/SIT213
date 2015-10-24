   package visualisations;
	
	
   import information.Information;


/** 
 * Classe réalisant l'affichage d'information composée d'éléments réels (float)
 * @author prou
 */
   public class SondeDiagrammeOeil extends Sonde <Double> {
    
   
	   private int nbEch;
	   private int nbSymbole;
    /**
    * pour construire une sonde analogique  
    * @param nom  le nom de la fenêtre d'affichage
    */
      public SondeDiagrammeOeil(String nom, int nbEch, int nbSym) {
         super(nom);
         this.nbEch = nbEch;
         nbSymbole = nbSym;
      }
   
   
   	 
      public void recevoir (Information <Double> information) { 
         informationRecue = information;
         int nbElements = information.nbElements();
         double [] table = new double[nbElements];
         int i = 0;
         for (double f : information) {
            table[i] = f;
            i++;
         }
         new VueDiagramme(table, nom, nbEch, nbSymbole); 
      }
   	 
   	
   
   
   }