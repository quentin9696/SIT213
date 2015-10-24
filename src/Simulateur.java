
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
      private 			double min = 0.0f;
      private			double max = 1.0f;
   /** indique si le simulateur utilise un canal bruité */
      private 			boolean avecBruit = false;
   /** valeur du rapport signal a bruit (linéraire) */
      private 			double snr = 0.0f;
      
   /** indique si le simulateur utilise des multi-trajet */
      private			boolean avecMultiTrajet = false;
   /**	tableau des retards pour le multitrajet    */
      private			ArrayList<Integer> dt = new ArrayList<Integer>(5);
      /**	tableau des atténuations pour le multitrajet    */
      private			ArrayList<Double> ar = new ArrayList<Double>(5);
   	
   /** le  composant Source de la chaine de transmission */
      private			  Source <Boolean>  source = null;
   /** les transmetteurs multi-trajet s'il en a */
      private			Transmetteur<Double, Double> transmetteurMultiTrajet = null;
      private			Transmetteur<Double, Double> recepteurMultiTrajet = null;

   /** le composant emetteur (information logique en information echentillonnée */
      private			Transmetteur<Boolean, Double> emetteur = null;
      
   /** le  composant Transmetteur parfait logique de la chaine de transmission */
      private			  Transmetteur <Boolean, Boolean>  transmetteurLogique = null;
   /** composant Transmetteur analogique */
      private			  Transmetteur<Double, Double>  transmetteurAnalogique = null;
      /** le composant recepteur (information echentillonnée en information logique) */
      private			Transmetteur<Double, Boolean> recepteur = null;
   /** le  composant Destination de la chaine de transmission */
      private			Destination <Boolean>  destination = null;
   /** utilisation des transducteurs */
      private 			boolean avecTransducteur = false;
   /** le composant de l'emetteur du transducteur */
      private 			EmetteurTransducteur emetteurTransducteur = null;
   /** le composant de l'emetteur du transducteur */
      private 			RecepteurTransducteur recepteurTransducteur = null;   
      
      private 			boolean avecOeil = false;
      
   
   /** Le constructeur de Simulateur construit une chaine de transmission composÃ©e d'une Source <Boolean>, d'une Destination <Boolean> et de Transmetteur(s) [voir la mÃ©thode analyseArguments]...  
   * <br> Les diffÃ©rents composants de la chaine de transmission (Source, Transmetteur(s), Destination, Sonde(s) de visualisation) sont crÃ©Ã©s et connectÃ©s.
   * @param args le tableau des diffÃ©rents arguments.
   *
   * @throws ArgumentsException si un des arguments est incorrect
   * @throws EmetteurNonConforme l'emetteur n'est pas correct  
 * @throws TransmetteurAnalogiqueBruiteNonConforme 
 * @throws TransmetteurAnalogiqueMultiTrajetNonConforme 
 * @throws RecepteurAnalogiqueMultiTrajetNonConforme 
   *
   */   
      public  Simulateur(String [] args) throws ArgumentsException, EmetteurNonConforme, RecepteurNonConforme, TransmetteurAnalogiqueBruiteNonConforme, TransmetteurAnalogiqueMultiTrajetNonConforme, RecepteurAnalogiqueMultiTrajetNonConforme {
      
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
         
         if(avecTransducteur) {
        	 emetteurTransducteur = new EmetteurTransducteur();
         }
         
         // Emetteur/Recepteur analogique
         emetteur = new EmetteurAnalogique(min, max, forme, nbEch);
         recepteur = new RecepteurAnalogique(min, max, forme, nbEch);
         
         // Multi trajet
         if(avecMultiTrajet) {
        	 transmetteurMultiTrajet = new TransmetteurAnalogiqueMultiTrajet(dt, ar);
        	 recepteurMultiTrajet = new RecepteurAnalogiqueMultiTrajet(dt, ar);

         }
         
         //Transmeteur 
         if(!avecBruit) {
        	 transmetteurAnalogique = new TransmetteurParfaitAnalogique();
         }
         else {
        	 if(aleatoireAvecGerme) {
        		 transmetteurAnalogique = new TransmetteurAnalogiqueBruite(snr, seed);
        	 }
        	 else {
        		 transmetteurAnalogique = new TransmetteurAnalogiqueBruite(snr);
        	 }
        	 
         }
         
         if(avecTransducteur) {
        	 recepteurTransducteur = new RecepteurTransducteur();
         }
         
         // Destination 
         destination = new DestinationFinale();
         
         // Connection entre les modules
         
         if(avecTransducteur) {
        	 source.connecter(emetteurTransducteur);
        	 emetteurTransducteur.connecter(emetteur);
         }
         else {
        	 source.connecter(emetteur);
         }
         
         
         
    	 
         //On ajout les briques multi-trajet s'il y en a 
    	 if(avecMultiTrajet) {
    		 emetteur.connecter(transmetteurMultiTrajet);
			 transmetteurMultiTrajet.connecter(transmetteurAnalogique);
    		 transmetteurAnalogique.connecter(recepteurMultiTrajet);
    		 recepteurMultiTrajet.connecter(recepteur);
    		 
    		 if(avecTransducteur) {
    			 recepteur.connecter(recepteurTransducteur);
    		 }
    		 
    	 }
    	 else {
    		 emetteur.connecter(transmetteurAnalogique);
    		 transmetteurAnalogique.connecter(recepteur);
    		 
    		 if(avecTransducteur) {
    			 recepteur.connecter(recepteurTransducteur);
    		 }
    		  
    	 }
         
    	 // Le recepteur est toujours connecté à la destination !
    	 if(avecTransducteur) {
    		 recepteurTransducteur.connecter(destination);
    	 }
    	 else {
    		 recepteur.connecter(destination);
    	 }
         
         
         
         // Ajout des sonde si option -s
         if(affichage) {
        	 SondeLogique sl1 = new SondeLogique("Source", 150);
             SondeLogique sl2 = new SondeLogique("Destination", 150);
             SondeLogique sl3 = new SondeLogique("Sortie Emetteur Transducteur", 150);
             SondeLogique sl4 = new SondeLogique("Sortie Recepteur Transducteur", 150);
             
             SondeAnalogique sa1 = new SondeAnalogique("Sortie Emetteur");
             SondeAnalogique sa2 = new SondeAnalogique("Sortie Transmetteur Analogique");
             
             SondeAnalogique sa3 = new SondeAnalogique("Sortie transmetteur multi-trajet");
             SondeAnalogique sa4 = new SondeAnalogique("Sortie Recepteur multi-trajet");
             
             source.connecter(sl1);
             if(avecTransducteur) {
            	 emetteurTransducteur.connecter(sl3);
            	 recepteurTransducteur.connecter(sl4);
             }
             
             
             emetteur.connecter(sa1);
             
             transmetteurAnalogique.connecter(sa2);
             
             
             if(avecMultiTrajet) {
            	 transmetteurMultiTrajet.connecter(sa3);
            	 recepteurMultiTrajet.connecter(sa4);
             }
             
             
             recepteur.connecter(sl2);
             
         }
         
         if(avecOeil) {
        	 
             //Diagramme oeil !
             SondeDiagrammeOeil sdo = new SondeDiagrammeOeil("Diagramme reception", nbEch, 4);
             
        	// Diagremme oeil ?
             transmetteurAnalogique.connecter(sdo);
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
   * <dt> -ampl min max </dt><dd>  min (double) et max (double), les amplitudes min et max du signal analogique Ã  transmettre ( min < max, 0.0 et 1.0 par dï¿œfaut))</dd> 
   * <br>
   * <dt> -snr s </dt><dd> s (double) le rapport signal/bruit en dB</dd>
   * <br>
   * <dt> -ti i dt ar </dt><dd> i (int) numero du trajet indirect (de 1 Ã  5), dt (int) valeur du decalage temporel du iÃšme trajet indirect 
   * en nombre d'Ã©chantillons par bit, ar (double) amplitude relative au signal initial du signal ayant effectuÃ© le iÃšme trajet indirect</dd>
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
               //if (args[i].matches("[0-9]{1,}")) {
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
        	    
        	    if(nbEch < 10) {
        	    	throw new ArgumentsException("Valeur du parametre -nbEch  invalide :" + nbEch);
        	    }
                  
            }
            else if (args[i].matches("-ampl")) {
        	   i++; 
        	   // traiter la valeur associee
        	   
        	   try { 
                   min =new Double(args[i]);
                }
                catch (Exception e) {
                      throw new ArgumentsException("Valeur du parametre -ampl  invalide :" + args[i]);
                }
        	   
        	   i++;
        	   
        	   try { 
                   max =new Double(args[i]);
                }
                catch (Exception e) {
                      throw new ArgumentsException("Valeur du parametre -ampl  invalide :" + args[i]);
                }
        	   
        	   if(!(min < max)) {
        		   	throw new ArgumentsException("Valeur du parametre -ampl  invalide :" + args[i]);
        	   }
            }
            
            else if (args[i].matches("-snr")) {
         	   	i++; 
         	   	
         	   	// traiter la valeur associee
         	   
         	   	try { 
                    snr = new Double(args[i]);
                }
                catch (Exception e) {
                       throw new ArgumentsException("Valeur du parametre -snr  invalide :" + args[i]);
                }
         	   	
         	   avecBruit = true; 
            }
            else if (args[i].matches("-ti")) {
         	   	i++; 
         	   	if(args[i].matches("[1-5]{1}")) {
         	   		try {
         	   			int j = new Integer(args[i]);
         	   			if(j != dt.size() + 1) {
         	   				throw new ArgumentsException("Valeur du parametre -ti i invalide :" + args[i]);
         	   			}
         	   		}
         	   		catch(Exception e) {
         	   			throw new ArgumentsException("Valeur du parametre -ti i invalide :" + args[i]);
         	   		}
         	   	}
         	   	else {
         	   		throw new ArgumentsException("Valeur du parametre -ti i invalide :" + args[i]);
         	   	}
         	   	
         	   	i++;
         	   	
         	   	if(args[i].matches("[0-9]{1,}")) {
         	   		try {
         	   			int k = new Integer(args[i]);
         	   			if(k < 1) {
         	   				throw new ArgumentsException("Valeur du parametre -ti dt invalide :" + args[i]);
         	   			}
         	   			
         	   			dt.add(k);
         	   		}
         	   		catch(Exception e) {
         	   			throw new ArgumentsException("Valeur du parametre -ti dt invalide :" + args[i]);
         	   		}
         	   	}
         	   	else {
         	   		throw new ArgumentsException("Valeur du parametre -ti i dt ar invalide :" + args[i]);
         	   	}
         	   	
         	   	i++;
         	   	if(args[i].matches("[0-1]{1}([.][0-9]*){0,1}")) {
	         	   	try {
			   			double l = new Double(args[i]);
			   			if(l<0 || l>1) {
			   				throw new ArgumentsException("Valeur du parametre -ti ar invalide :" + args[i]);
			   			}
			   			ar.add(1-l);
			   		}
			   		catch(Exception e) {
			   			throw new ArgumentsException("Valeur du parametre -ti ar invalide :" + args[i]);
			   		}
         	   	}
         	   	else {
         	   		throw new ArgumentsException("Valeur du parametre -ti i dt ar invalide :" + args[i]);
         	   	}
		   		
         	   	
		   		avecMultiTrajet = true; 
            }
            else if(args[i].matches("-transducteur")) {
            	avecTransducteur = true;
            }
            else if(args[i].matches("-oeil")) {
            	avecOeil = true;
            }
            else 
            	throw new ArgumentsException("Option invalide :"+ args[i]);
         }
         
         double sommeAlpha = 0.;
         for(double val : ar) {
        	 sommeAlpha += val;
         }
         
         if(sommeAlpha > 1) {
        	throw new ArgumentsException("La somme de tous les alphas ne peut dépasser 1"); 
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
  			
  			if(avecTransducteur) {
  				emetteurTransducteur.emettre();
  			}
  			
  			emetteur.emettre();
  			
  			if(avecMultiTrajet) {
  				transmetteurMultiTrajet.emettre();
  			}
  			
  			transmetteurAnalogique.emettre();
  			
  			if(avecMultiTrajet) {
  				recepteurMultiTrajet.emettre();
  			}
  			
  			recepteur.emettre();

  			
  			if(avecTransducteur) {
  				recepteurTransducteur.emettre();
  			}
  			
  			
  		} catch (InformationNonConforme e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
      	     	      
      }
   
   	   	
   	
   /** La mÃ©thode qui calcule le taux d'erreur binaire en comparant les bits du message Ã©mis avec ceux du message reÃ§u.
   *
   * @return  La valeur du Taux dErreur Binaire.
   */   	   
      public double  calculTauxErreurBinaire() {
    	  
    	  // Prend les info reçues et envoyées	
    	  Information<Boolean> infoSource = source.getInformationEmise();
    	  Information<Boolean> infoRecu = destination.getInformationRecue();

    	  // Compte le nombre de bits
    	  double nbTotal = infoSource.nbElements();
    	  double nbErreurs = 0.0f;

    	  
    	  // Compte le nb d'erreur
    	  // Utilisation des itérateurs 
    	  Iterator<Boolean> iterateurSource = infoSource.iterator();
    	  Iterator<Boolean> iterateurDest = infoRecu.iterator();
    	  
    	  while(iterateurDest.hasNext() && iterateurSource.hasNext()) {
    		  boolean bE = iterateurSource.next();
    		  boolean bR = iterateurDest.next();
    		  
    		  if(bE != bR) {
    			  nbErreurs ++;
    		  }
    	  }
    	  
    	  nbErreurs += Math.abs(infoRecu.nbElements() - infoSource.nbElements());
    	  
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
      	
        //////// Pour l'utilisation du profiler /////////
         
        /*Scanner scan = new Scanner(System.in);
      	scan.next();*/
      	
         try {
            simulateur.execute();
            double tauxErreurBinaire = simulateur.calculTauxErreurBinaire();
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

