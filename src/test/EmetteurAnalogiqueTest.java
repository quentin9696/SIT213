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
import transmetteurs.EmetteurNonConforme;


public class EmetteurAnalogiqueTest {
	private EmetteurAnalogique emetteurAnalogique_RZ;
	private EmetteurAnalogique emetteurAnalogique_NRZ;
	private EmetteurAnalogique emetteurAnalogique_NRZT;
	
	private int nombreEchantillonsRZ=20;
	private int nombreEchantillons=0;
	private int nombreEchantillonsNRZ=12;
	private int nombreEchantillonsNRZT=15;
	
	
	public EmetteurAnalogiqueTest() {
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
		emetteurAnalogique_NRZ = new EmetteurAnalogique(-1, 1, "NRZ", nombreEchantillonsNRZ );
	} catch (EmetteurNonConforme e) {
		e.printStackTrace();
	}		
    try {
		emetteurAnalogique_NRZT = new EmetteurAnalogique(-1, 1, "NRZT", nombreEchantillonsNRZT );
	} catch (EmetteurNonConforme e) {
		e.printStackTrace();
	}		
    try {
		emetteurAnalogique_RZ = new EmetteurAnalogique(-1, 1, "RZ", nombreEchantillonsRZ );
	} catch (EmetteurNonConforme e) {
		e.printStackTrace();
	}		
    	
    	
    }

    @After
    public void tearDown() {

    }
    
	@Test
	public void testEmetteurAnalogiqueConforme1() throws Exception {
		System.out.println("Test 0 du constructeur : ");
		emetteurAnalogique_RZ = new EmetteurAnalogique(-1, 1, "RZ", nombreEchantillonsRZ );	
	}
	@Test
	public void testEmetteurAnalogiqueConforme2() throws Exception {
		System.out.println("Test 1 du constructeur : ");
		emetteurAnalogique_NRZ = new EmetteurAnalogique(-1, 1, "NRZ", nombreEchantillonsNRZ );		
	}
	@Test
	public void testEmetteurAnalogiqueConforme3() throws Exception {
		System.out.println("Test 2 du constructeur : ");
		emetteurAnalogique_NRZT = new EmetteurAnalogique(-1, 1, "NRZT", nombreEchantillonsNRZT );		
	}
	@Test(expected=EmetteurNonConforme.class)
	public void testEmetteurAnalogiqueAvecUnMinSuperieurAuMax() throws Exception {
		System.out.println("Test 1 du constructeur : ");
		emetteurAnalogique_RZ = new EmetteurAnalogique(2, 1, "RZ", nombreEchantillonsRZ );		
	}
	@Test(expected=EmetteurNonConforme.class)
	public void testEmetteurAnalogiqueAvecUnMaxInferieurAuMin() throws Exception {
		System.out.println("Test 2 du constructeur : ");
		emetteurAnalogique_RZ = new EmetteurAnalogique(-1, -2, "RZ", nombreEchantillonsRZ );		
	}
	@Test(expected=EmetteurNonConforme.class)
	public void testEmetteurAnalogiqueFormeIncorrecte() throws Exception {
		System.out.println("Test 3 du constructeur : ");
		emetteurAnalogique_RZ = new EmetteurAnalogique(-1, 1, "", nombreEchantillonsRZ );		
	}
	@Test(expected=EmetteurNonConforme.class)
	public void testEmetteurAnalogiqueFormeIncorrecteBis() throws Exception {
		System.out.println("Test 4 du constructeur : ");
		emetteurAnalogique_RZ = new EmetteurAnalogique(-1, 1, null, nombreEchantillonsRZ );		
	}
	@Test(expected=EmetteurNonConforme.class)
	public void testEmetteurAnalogiqueNombreEchantillonsDeFormeNonConforme() throws Exception {
		System.out.println("Test 5 du constructeur : ");
		emetteurAnalogique_RZ = new EmetteurAnalogique(-1, 1, "NRZL", nombreEchantillonsRZ );
		
	}
	@Test(expected=EmetteurNonConforme.class)
	public void testEmetteurAnalogiqueNombreEchantillonsNul() throws Exception {
		System.out.println("Test 5 du constructeur : ");
		emetteurAnalogique_RZ = new EmetteurAnalogique(-1, 1, "NRZT", nombreEchantillons);		
	}
	
	@Test
	public void testRecevoirNRZ() throws Exception {
		System.out.println("Test 1 de la méthode recevoir : ");
		Boolean[] tableauDeBooleens= {true, false, true, true, true, true, false};
		Information<Boolean> messageBinaire= new Information<Boolean>(tableauDeBooleens);
		emetteurAnalogique_NRZ.recevoir(messageBinaire);
		//System.out.println(tableauDeBooleens.length);
		//System.out.println(emetteurAnalogique_NRZ.getInformationRecue().nbElements());
		assertEquals(messageBinaire, emetteurAnalogique_NRZ.getInformationRecue());
		
	}
	@Test
	public void testRecevoirNRZT() throws Exception {
		System.out.println("Test 2 de la méthode recevoir : ");
		Boolean[] tableauDeBooleens= {true, true, true, false, true, false, false, true};
		Information<Boolean> messageBinaire= new Information<Boolean>(tableauDeBooleens);
		emetteurAnalogique_NRZT.recevoir(messageBinaire);
		//System.out.println(tableauDeBooleens.length);
		//System.out.println(emetteurAnalogique_NRZT.getInformationRecue().nbElements());
		assertEquals(messageBinaire, emetteurAnalogique_NRZT.getInformationRecue());
		
		
	}
	@Test
	public void testRecevoirRZ() throws Exception {
		System.out.println("Test 3 de la méthode recevoir : ");
		Boolean[] tableauDeBooleens= {true, true, true, true, true, false};
		Information<Boolean> messageBinaire= new Information<Boolean>(tableauDeBooleens);
		emetteurAnalogique_RZ.recevoir(messageBinaire);
		//System.out.println(tableauDeBooleens.length);
		//System.out.println(emetteurAnalogique_RZ.getInformationRecue().nbElements());
		assertEquals(messageBinaire, emetteurAnalogique_RZ.getInformationRecue());
		
		
	}
	

}
