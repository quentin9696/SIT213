
import sources.*;
import destinations.*;
import transmetteurs.*;

   import information.*;

   import visualisations.*;

import java.util.regex.*;
import java.util.*;
import java.lang.Math;

	
   import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


/** La classe Simulateur permet de construire et simuler une chaine de transmission composÃ©e d'une Source, d'un nombre variable de Transmetteur(s) et d'une Destination.  
 * @author cousin
 * @author prou
 * @author vallin
 * @author omnes
 *
 */
   public class Simulateur {
      	
   /** indique si le Simulateur utilise des sondes d'affichage */
      private          boolean affichage = false;
   /** indique si le Simulateur utilise un message gÃ©nÃ©rÃ© de maniÃšre alÃ©atoire */
      private          boolean messageAleatoire = true;
   /** indique si le Simulateur utilise un germe pour initialiser les gÃ©nÃ©rateurs alÃ©atoires */
      private          boolean aleatoireAvecGerme = false;
   /** la valeur de la semence utilisÃ©e pour les gÃ©nÃ©rateurs alÃ©atoires */
      private          Integer seed = null;
   /** la longueur du message alÃ©atoire Ã  transmettre si un message n'est pas impose */
      private          int nbBitsMess = 100; 
   /** la chaine de caractÃšres correspondant Ã  m dans l'argument -mess m */
      private          String messageString = "100";
   /** le nb d'echantillons par bit correspondant à -nbEch ne*/
      private 			int nbEch = 30;
   /** la forme du signal. Correspond au paramètre -form f */
      private 			String forme = "RZ";
   /** l'amplitude du signal. Correspond au paramètre -ampl min max */
      private 			float min = 0.0f;
      private			float max = 1.0f;
   
   
   	
   /** le  composant Source de la chaine de transmission */
      private			  Source <Boolean>  source = null;
      
      private			Transmetteur<Boolean, Float> emetteur = null;
      
   /** le  composant Transmetteur parfait logique de la chaine de transmission */
      private			  Transmetteur <Boolean, Boolean>  transmetteurLogique = null;
      private			  Transmetteur <Float, Float>  transmetteurAnalogique = null;
      
      private			Transmetteur<Float, Boolean> recepteur = null;
   /** le  composant Destination de la chaine de transmission */
      private			  Destination <Boolean>  destination = null;
      
   	
   
   /** Le constructeur de Simulateur construit une chaine de transmission composÃ©e d'une Source <Boolean>, d'une Destination <Boolean> et de Transmetteur(s) [voir la mÃ©thode analyseArguments]...  
   * <br> Les diffÃ©rents composants de la chaine de transmission (Source, Transmetteur(s), Destination, Sonde(s) de visualisation) sont crÃ©Ã©s et connectÃ©s.
   * @param args le tableau des diffÃ©rents arguments.
   *
   * @throws ArgumentsException si un des arguments est incorrect
   * @throws EmetteurNonConforme l'emetteur n'est pas correct  
   *
   */   
      public  Simulateur(String [] args) throws ArgumentsException, EmetteurNonConforme, RecepteurNonConforme {
      
      	// analyser et rÃ©cupÃ©rer les arguments
      	
         analyseArguments(args);

        // Message pas aléatoire (-m avec suite logique) 
         if(!messageAleatoire) {
        	 source = new SourceFixe(messageString);
         }
         else {
	// message avec un seed (et eventuellement une taille
        	 if(aleatoireAvecGerme){
        		 source = new SourceAleatoire(nbBitsMess, seed);
        	 }
	// message sans seed (et eventuellement une taille
        	 else {
        		 source = new SourceAleatoire(nbBitsMess);
        	 }
         }
         
         
         // Emetteur/Recepteur analogique
         emetteur = new EmetteurAnalogique(min, max, forme, nbEch);
         recepteur = new RecepteurAnalogique(min, max, forme, nbEch);
         
         //Transmeteur
         transmetteurAnalogique = new TransmetteurParfaitAnalogique();
         
         
         // Destination 
         destination = new DestinationFinale();
         
         // Connection entre les modules
         
         source.connecter(emetteur);
         emetteur.connecter(transmetteurAnalogique);
         transmetteurAnalogique.connecter(recepteur);
         recepteur.connecter(destination);
         
         /*transmetteurLogique = new TransmetteurParfait();
         source.connecter(transmetteurLogique);
         transmetteurLogique.connecter(destination);*/
         
         
         // Ajout des sonde si option -s
         if(affichage) {
        	 SondeLogique sl1 = new SondeLogique("Source", 150);
             SondeLogique sl2 = new SondeLogique("Destination", 150);
             SondeAnalogique sa1 = new SondeAnalogique("Emetteur");
             SondeAnalogique sa2 = new SondeAnalogique("Recepteur");
             
             source.connecter(sl1);
             emetteur.connecter(sa1);
             transmetteurAnalogique.connecter(sa2);
             recepteur.connecter(sl2);
             
         }
      	
      }
   
   
   
   /** La mÃ©thode analyseArguments extrait d'un tableau de chaines de caractÃšres les diffÃ©rentes options de la simulation. 
   * Elle met Ã  jour les attributs du Simulateur.
   *
   * @param args le tableau des diffÃ©rents arguments.
   * <br>
   * <br>Les arguments autorisÃ©s sont : 
   * <br> 
   * <dl>
   * <dt> -mess m  </dt><dd> m (String) constituÃ© de 7 ou plus digits Ã  0 | 1, le message Ã  transmettre</dd>
   * <dt> -mess m  </dt><dd> m (int) constituÃ© de 1 Ã  6 digits, le nombre de bits du message "alÃ©atoire" Ã  transmettre</dd> 
   * <dt> -s </dt><dd> utilisation des sondes d'affichage</dd>
   * <dt> -seed v </dt><dd> v (int) d'initialisation pour les gÃ©nÃ©rateurs alÃ©atoires</dd> 
   * <br>
   * <dt> -form f </dt><dd>  codage (String) RZ, NRZR, NRZT, la forme d'onde du signal Ã  transmettre (RZ par dÃ©faut)</dd>
   * <dt> -nbEch ne </dt><dd> ne (int) le nombre d'Ã©chantillons par bit (ne >= 6 pour du RZ, ne >= 9 pour du NRZT, ne >= 18 pour du RZ,  30 par dÃ©faut))</dd>
   * <dt> -ampl min max </dt><dd>  min (float) et max (float), les amplitudes min et max du signal analogique Ã  transmettre ( min < max, 0.0 et 1.0 par dï¿œfaut))</dd> 
   * <br>
   * <dt> -snr s </dt><dd> s (float) le rapport signal/bruit en dB</dd>
   * <br>
   * <dt> -ti i dt ar </dt><dd> i (int) numero du trajet indirect (de 1 Ã  5), dt (int) valeur du decalage temporel du iÃšme trajet indirect 
   * en nombre d'Ã©chantillons par bit, ar (float) amplitude relative au signal initial du signal ayant effectuÃ© le iÃšme trajet indirect</dd>
   * <br>
   * <dt> -transducteur </dt><dd> utilisation de transducteur</dd>
   * <br>
   * <dt> -aveugle </dt><dd> les rÃ©cepteurs ne connaissent ni l'amplitude min et max du signal, ni les diffÃ©rents trajets indirects (s'il y en a).</dd>
   * <br>
   * </dl>
   * <br> <b>Contraintes</b> :
   * Il y a des interdÃ©pendances sur les paramÃštres effectifs. 
   *
   * @throws ArgumentsException si un des arguments est incorrect.
   *
   */   
      public  void analyseArguments(String[] args)  throws  ArgumentsException {
      		
         for (int i=0;i<args.length;i++){ 
         
              
            if (args[i].matches("-s")){
               affichage = true;
            }
            else if (args[i].matches("-seed")) {
               aleatoireAvecGerme = true;
               i++; 
            	// traiter la valeur associee
               try { 
                  seed =new Integer(args[i]);
               }
                  catch (Exception e) {
                     throw new ArgumentsException("Valeur du parametre -seed  invalide :" + args[i]);
                  }           		
            }
            
            else if (args[i].matches("-mess")){
               i++; 
            	// traiter la valeur associee
               messageString = args[i];
               if (args[i].matches("[0,1]{7,}")) {
                  messageAleatoire = false;
                  nbBitsMess = args[i].length();
               } 
               else if (args[i].matches("[0-9]{1,6}")) {
                  messageAleatoire = true;
                  nbBitsMess = new Integer(args[i]);
                  if (nbBitsMess < 1) 
                     throw new ArgumentsException ("Valeur du parametre -mess invalide : " + nbBitsMess);
               }
               else 
               		throw new ArgumentsException("Valeur du parametre -mess invalide : " + args[i]);
            }
            
            else if (args[i].matches("-form")) {
        	   i++; 
        	   // traiter la valeur associee
        	   if (args[i].matches("NRZ") || args[i].matches("NRZT") || args[i].matches("RZ")) {
        		   forme = args[i];
               } 
               else 
            	   throw new ArgumentsException("Valeur du parametre -form invalide : " + args[i]);   
            }
            
            else if (args[i].matches("-nbEch")) {
        	   i++; 
        	   // traiter la valeur associee
        	    try { 
                   nbEch =new Integer(args[i]);
                }
                catch (Exception e) {
                      throw new ArgumentsException("Valeur du parametre -nbEch  invalide :" + args[i]);
                }
        	    
        	    if(nbEch < 1) {
        	    	throw new ArgumentsException("Valeur du parametre -nbEch  invalide :" + nbEch);
        	    }
                  
            }
            else if (args[i].matches("-ampl")) {
        	   i++; 
        	   // traiter la valeur associee
        	   
        	   try { 
                   min =new Float(args[i]);
                }
                catch (Exception e) {
                      throw new ArgumentsException("Valeur du parametre -ampl  invalide :" + args[i]);
                }
        	   
        	   i++;
        	   
        	   try { 
                   max =new Float(args[i]);
                }
                catch (Exception e) {
                      throw new ArgumentsException("Valeur du parametre -ampl  invalide :" + args[i]);
                }
        	   
        	   if(!(min < max)) {
        		   	throw new ArgumentsException("Valeur du parametre -ampl  invalide :" + args[i]);
        	   }
            }
                                   
            else 
            	throw new ArgumentsException("Option invalide :"+ args[i]);
         }
      
      }
     
    
   	
   /** La mÃ©thode execute effectue un envoi de message par la source de la chaine de transmission du Simulateur. 
   * @return les options explicites de simulation.
   *
   * @throws Exception si un problÃšme survient lors de l'exÃ©cution
   *
   */ 
      public void execute() throws Exception {      
         
    	 try {
  			source.emettre();
  		} catch (InformationNonConforme e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
      	     	      
      }
   
   	   	
   	
   /** La mÃ©thode qui calcule le taux d'erreur binaire en comparant les bits du message Ã©mis avec ceux du message reÃ§u.
   *
   * @return  La valeur du Taux dErreur Binaire.
   */   	   
      public float  calculTauxErreurBinaire() {
    	  
    	  // Prend les info reçues et envoyées	
    	  Information<Boolean> infoSource = source.getInformationEmise();
    	  Information<Boolean> infoRecu = destination.getInformationRecue();

    	  // Compte le nombre de bits
    	  float nbTotal = infoSource.nbElements();
    	  float nbRecu = infoRecu.nbElements();
    	  float nbErreurs = 0.0f;

    	  // Compte le nb d'erreur 
    	  for(int i=0; i<nbRecu; i++) {
    		  if(infoRecu.iemeElement(i) != infoSource.iemeElement(i)) {
    			  nbErreurs ++;
    		  }
    	  }
    	  
    	  // Calcul du TEB 
    	  return  nbErreurs/nbTotal;
    	  
      }
   
   
   
   
   /** La fonction main instancie un Simulateur Ã  l'aide des arguments paramÃštres et affiche le rÃ©sultat de l'exÃ©cution d'une transmission.
   *  @param args les diffÃ©rents arguments qui serviront Ã  l'instanciation du Simulateur.
   */
      public static void main(String [] args) { 
      
         Simulateur simulateur = null;
      	
         try {
            simulateur = new Simulateur(args);
         }
            catch (Exception e) {
               System.out.println(e); 
               System.exit(-1);
            } 
      		
         try {
            simulateur.execute();
            float tauxErreurBinaire = simulateur.calculTauxErreurBinaire();
            String s = "java  Simulateur  ";
            for (int i = 0; i < args.length; i++) {
         		s += args[i] + "  ";
         	}
            System.out.println(s + "  =>   TEB : " + tauxErreurBinaire);
         }
            catch (Exception e) {
               System.out.println(e);
               e.printStackTrace();
               System.exit(-2);
            }              	
      }
   	
   }

