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

import transmetteurs.RecepteurAnalogique;
import transmetteurs.RecepteurNonConforme;


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
    	
    	
    	
    }

    @After
    public void tearDown() {

    }
	
	@Test
	public void testEmetteurAnalogiqueConforme1() throws Exception {
		System.out.println("Test 0 du constructeur : ");
		recepteurAnalogique_RZ = new RecepteurAnalogique(-1, 1, "RZ", nombreEchantillonsRZ );	
	}
	@Test
	public void testEmetteurAnalogiqueConforme2() throws Exception {
		System.out.println("Test 1 du constructeur : ");
		recepteurAnalogique_NRZ = new RecepteurAnalogique(-1, 1, "NRZ", nombreEchantillonsNRZ );		
	}
	@Test
	public void testEmetteurAnalogiqueConforme3() throws Exception {
		System.out.println("Test 2 du constructeur : ");
		recepteurAnalogique_NRZT = new RecepteurAnalogique(-1, 1, "NRZT", nombreEchantillonsNRZT );		
	}
	@Test(expected=RecepteurNonConforme.class)
	public void testEmetteurAnalogiqueAvecUnMinSuperieurAuMax() throws Exception {
		System.out.println("Test 3 du constructeur : ");
		recepteurAnalogique_RZ = new RecepteurAnalogique(2, 1, "RZ", nombreEchantillonsRZ );		
	}
	@Test(expected=RecepteurNonConforme.class)
	public void testEmetteurAnalogiqueAvecUnMaxInferieurAuMin() throws Exception {
		System.out.println("Test 4 du constructeur : ");
		recepteurAnalogique_RZ = new RecepteurAnalogique(-1, -2, "RZ", nombreEchantillonsRZ );		
	}
	@Test(expected=RecepteurNonConforme.class)
	public void testEmetteurAnalogiqueFormeIncorrecte() throws Exception {
		System.out.println("Test 5 du constructeur : ");
		recepteurAnalogique_RZ = new RecepteurAnalogique(-1, 1, "", nombreEchantillonsRZ );		
	}
	@Test(expected=RecepteurNonConforme.class)
	public void testEmetteurAnalogiqueFormeIncorrecteBis() throws Exception {
		System.out.println("Test 6 du constructeur : ");
		recepteurAnalogique_RZ = new RecepteurAnalogique(-1, 1, null, nombreEchantillonsRZ );		
	}
	@Test(expected=RecepteurNonConforme.class)
	public void testEmetteurAnalogiqueNombreEchantillonsDeFormeNonConforme() throws Exception {
		System.out.println("Test 7 du constructeur : ");
		recepteurAnalogique_RZ = new RecepteurAnalogique(-1, 1, "NRZL", nombreEchantillonsRZ );
		
	}
	@Test(expected=RecepteurNonConforme.class)
	public void testEmetteurAnalogiqueNombreEchantillonsNul() throws Exception {
		System.out.println("Test 8 du constructeur : ");
		recepteurAnalogique_RZ = new RecepteurAnalogique(-1, 1, "NRZT", nombreEchantillons);		
	}
	

}