package test;

import static org.junit.Assert.*;
import information.Information;
import information.InformationNonConforme;
import java.util.Objects;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Test;

import transmetteurs.EmetteurAnalogique;
import transmetteurs.RecepteurAnalogique;
import transmetteurs.RecepteurNonConforme;
import transmetteurs.Transmetteur;

import org.junit.Test;

public class RecepteurAnalogiqueTest {



		private RecepteurAnalogique recepteurAnalogique_RZ;
		private RecepteurAnalogique recepteurAnalogique_NRZ;
		private RecepteurAnalogique recepteurAnalogique_NRZT;
		private int nombreEchantillonsRZ=20;
		private int nombreEchantillons=0;
		private int nombreEchantillonsNRZ=12;
		private int nombreEchantillonsNRZT=15;
		
		public RecepteurAnalogiqueTest() {
	    }

	    @BeforeClass
	    public static void setUpClass() {
	    }

	    @AfterClass
	    public static void tearDownClass() {
	    }

	    @Before
	    public void setUp() {
	    try {
			recepteurAnalogique_RZ = new RecepteurAnalogique(-1, 1, "RZ", nombreEchantillonsRZ );
		} catch (RecepteurNonConforme e) {
			e.printStackTrace();
		}
	    try {
			recepteurAnalogique_NRZ = new RecepteurAnalogique(-1, 1, "NRZ", nombreEchantillonsNRZ );
		} catch (RecepteurNonConforme e) {
			e.printStackTrace();
		}
	    try {
			recepteurAnalogique_NRZT = new RecepteurAnalogique(-1, 1, "NRZT", nombreEchantillonsNRZT );
		} catch (RecepteurNonConforme e) {
			e.printStackTrace();
		}
	    }

	    @After
	    public void tearDown() {

	    }
	    private Float[] getRandomFloatArray(int n) {
	        Float[] bits = new Float[n];
	        Random rnd = new Random();

	        for (int i = 0; i < n; i++) {
	            bits[i] = rnd.nextFloat();
	        }
	        return bits;
	    }
		
		@Test
		public void testrecepteurAnalogiqueConforme1() throws Exception {
			System.out.println("Test 0 du constructeur : ");
			recepteurAnalogique_RZ = new RecepteurAnalogique(-1, 1, "RZ", nombreEchantillonsRZ );	
		}
		@Test
		public void testrecepteurAnalogiqueConforme2() throws Exception {
			System.out.println("Test 1 du constructeur : ");
			recepteurAnalogique_NRZ = new RecepteurAnalogique(-1, 1, "NRZ", nombreEchantillonsNRZ );		
		}
		@Test
		public void testrecepteurAnalogiqueConforme3() throws Exception {
			System.out.println("Test 2 du constructeur : ");
			recepteurAnalogique_NRZT = new RecepteurAnalogique(-1, 1, "NRZT", nombreEchantillonsNRZT );		
		}
		@Test(expected=RecepteurNonConforme.class)
		public void testrecepteurAnalogiqueAvecUnMinSuperieurAuMax() throws Exception {
			System.out.println("Test 1 du constructeur : ");
			recepteurAnalogique_RZ = new RecepteurAnalogique(2, 1, "RZ", nombreEchantillonsRZ );		
		}
		@Test(expected=RecepteurNonConforme.class)
		public void testrecepteurAnalogiqueAvecUnMaxInferieurAuMin() throws Exception {
			System.out.println("Test 2 du constructeur : ");
			recepteurAnalogique_RZ = new RecepteurAnalogique(-1, -2, "RZ", nombreEchantillonsRZ );		
		}
		@Test(expected=RecepteurNonConforme.class)
		public void testrecepteurAnalogiqueFormeIncorrecte() throws Exception {
			System.out.println("Test 3 du constructeur : ");
			recepteurAnalogique_RZ = new RecepteurAnalogique(-1, 1, "", nombreEchantillonsRZ );		
		}
		@Test(expected=RecepteurNonConforme.class)
		public void testrecepteurAnalogiqueFormeIncorrecteBis() throws Exception {
			System.out.println("Test 4 du constructeur : ");
			recepteurAnalogique_RZ = new RecepteurAnalogique(-1, 1, null, nombreEchantillonsRZ );		
		}
		@Test(expected=RecepteurNonConforme.class)
		public void testrecepteurAnalogiqueNombreEchantillonsDeFormeNonConforme() throws Exception {
			System.out.println("Test 5 du constructeur : ");
			recepteurAnalogique_RZ = new RecepteurAnalogique(-1, 1, "NRZL", nombreEchantillonsRZ );
			
		}
		@Test(expected=RecepteurNonConforme.class)
		public void testrecepteurAnalogiqueNombreEchantillonsNul() throws Exception {
			System.out.println("Test 5 du constructeur : ");
			recepteurAnalogique_RZ = new RecepteurAnalogique(-1, 1, "NRZT", nombreEchantillons);		
		}
		@Test
		public void testRecevoirNRZ() throws Exception {
			System.out.println("Test 1 de la méthode recevoir : ");
			Double[] tabDeDouble= {0.,2.5,0.3};
			Information<Double> messageAnalogique= new Information<Double>(tabDeDouble);
			recepteurAnalogique_NRZ.recevoir(messageAnalogique);
			//System.out.println(tableauDeBooleens.length);
			//System.out.println(emetteurAnalogique_NRZ.getInformationRecue().nbElements());
			assertEquals(messageAnalogique, recepteurAnalogique_NRZ.getInformationRecue());
			
		}
		@Test
		public void testRecevoirNRZT() throws Exception {
			System.out.println("Test 2 de la méthode recevoir : ");
			Double[] tableauDeDouble= {2.,3.5,8.9};
			Information<Double> messageAnalogique= new Information<Double>(tableauDeDouble);
			recepteurAnalogique_NRZT.recevoir(messageAnalogique);
			//System.out.println(tableauDeBooleens.length);
			//System.out.println(emetteurAnalogique_NRZT.getInformationRecue().nbElements());
			assertEquals(messageAnalogique, recepteurAnalogique_NRZT.getInformationRecue());
			
			
		}
		@Test
		public void testRecevoirRZ() throws Exception {
			System.out.println("Test 3 de la méthode recevoir : ");
			Double[] tableauDeDouble= {2.5,3.6,2.8};
			Information<Double> messageAnalogique= new Information<Double>(tableauDeDouble);
			recepteurAnalogique_RZ.recevoir(messageAnalogique);
			//System.out.println(tableauDeBooleens.length);
			//System.out.println(emetteurAnalogique_RZ.getInformationRecue().nbElements());
			assertEquals(messageAnalogique, recepteurAnalogique_RZ.getInformationRecue());
			
			
		
			
			
		}
		
		
}
