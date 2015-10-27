package test;

import static org.junit.Assert.*;
import information.Information;
import information.InformationNonConforme;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import transmetteurs.*;
 


public class EmetteurTransducteurTest {

	private EmetteurTransducteur emetteurTransducteur;
	private Information <Boolean> message;
    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    emetteurTransducteur= new EmetteurTransducteur();
    message = new Information<Boolean>();
    message.add(true);
    message.add(false);
    }

    @After
    public void tearDown() {

    }
    
	@Test
	public void testMessageConforme() throws InformationNonConforme {
		emetteurTransducteur.recevoir(message);
		Information<Boolean> messageTheorique = new Information<Boolean>();
		messageTheorique.add(true);
		messageTheorique.add(false);
		messageTheorique.add(true);
		messageTheorique.add(false);
		messageTheorique.add(true);
		messageTheorique.add(false);
		assertEquals("le message empirique devrait être égal au message théorique", emetteurTransducteur.getInformationEmise(), messageTheorique);
	}
	 
		@Test
		public void testMessageNonConforme() throws InformationNonConforme {
			emetteurTransducteur.recevoir(message);
			Information<Boolean> messageTheorique = new Information<Boolean>();
			messageTheorique.add(true);
			messageTheorique.add(false);
			messageTheorique.add(false);
			messageTheorique.add(false);
			messageTheorique.add(true);
			messageTheorique.add(false);			
			assertFalse("le message empirique ne devrait pas être égal au message théorique",emetteurTransducteur.getInformationEmise().equals(messageTheorique));
		}

}
