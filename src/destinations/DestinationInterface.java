package destinations;

	import information.*;


/** 
 * Interface d'un composant ayant le comportement d'une destination d'informations dont les Ã©lÃ©ments sont de type T 
 * @author prou
 */
    public  interface DestinationInterface <T>  {   
   
   /**
    * pour obtenir la derniÃšre information reÃ§ue par une destination.
    * @return une information   
    */  
       public Information <T>  getInformationRecue(); 
   	 
   /**
    * pour recevoir une information  de la source qui nous est connectÃ©e 
    * @param information  l'information  Ã  recevoir
    * @throws InformationNonConforme Exeception car l'information n'est pas valide
    */
       public void recevoir(Information <T> information) throws InformationNonConforme;
   	    
   
   }
