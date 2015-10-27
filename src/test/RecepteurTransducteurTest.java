package test;

import static org.junit.Assert.*;
import information.Information;
import information.InformationNonConforme;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import transmetteurs.RecepteurTransducteur;

public class RecepteurTransducteurTest {

	private RecepteurTransducteur recepteurTransducteur;
	private Information <Boolean> message;
    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    recepteurTransducteur= new RecepteurTransducteur();
    }

    @After
    public void tearDown() {

    }
    
	@Test
	public void testMessageConforme0() throws InformationNonConforme {
		 message = new Information<Boolean>();
		 message.add(false);
		 message.add(true);
		 message.add(false);
		recepteurTransducteur.recevoir(message);
		Information<Boolean> messageTheorique = new Information<Boolean>();
		messageTheorique.add(false);
		assertEquals("le message empirique devrait être égal au message théorique", recepteurTransducteur.getInformationEmise(), messageTheorique);
	}
	 
		@Test
		public void testMessageConforme1() throws InformationNonConforme {
		 message = new Information<Boolean>();
		 message.add(false);
		 message.add(false);
		 message.add(false);
		 recepteurTransducteur.recevoir(message);
		 Information<Boolean> messageTheorique = new Information<Boolean>();
		 messageTheorique.add(false);		
		 assertEquals("le message empirique ne devrait pas être égal au message théorique",recepteurTransducteur.getInformationEmise(), messageTheorique);
		}

		@Test
		public void testMessageConforme2() throws InformationNonConforme {
		message = new Information<Boolean>();
		 message.add(false);
		 message.add(false);
		 message.add(true);
		 recepteurTransducteur.recevoir(message);
		 Information<Boolean> messageTheorique = new Information<Boolean>();
		 messageTheorique.add(true);			
		 assertEquals("le message empirique ne devrait pas être égal au message théorique",recepteurTransducteur.getInformationEmise(), messageTheorique);
		}

		@Test
		public void testMessageConforme3() throws InformationNonConforme {
		message = new Information<Boolean>();
		 message.add(false);
		 message.add(true);
		 message.add(true);
		 recepteurTransducteur.recevoir(message);
		 Information<Boolean> messageTheorique = new Information<Boolean>();
		 messageTheorique.add(false);			
		 assertEquals("le message empirique ne devrait pas être égal au message théorique",recepteurTransducteur.getInformationEmise(), messageTheorique);

	}
		@Test
		public void testMessageConforme4() throws InformationNonConforme {
		message = new Information<Boolean>();
		 message.add(true);
		 message.add(false);
		 message.add(false);
		 recepteurTransducteur.recevoir(message);
		 Information<Boolean> messageTheorique = new Information<Boolean>();
		 messageTheorique.add(true);			
		 assertEquals("le message empirique ne devrait pas être égal au message théorique",recepteurTransducteur.getInformationEmise(), messageTheorique);
		}
		@Test
		public void testMessageConforme5() throws InformationNonConforme {
		message = new Information<Boolean>();
		 message.add(true);
		 message.add(false);
		 message.add(true);
		 recepteurTransducteur.recevoir(message);
		 Information<Boolean> messageTheorique = new Information<Boolean>();
		 messageTheorique.add(true);			
		 assertEquals("le message empirique ne devrait pas être égal au message théorique",recepteurTransducteur.getInformationEmise(), messageTheorique);
		}
		@Test
		public void testMessageConforme6() throws InformationNonConforme {
		message = new Information<Boolean>();
		 message.add(true);
		 message.add(true);
		 message.add(false);
		 recepteurTransducteur.recevoir(message);
		 Information<Boolean> messageTheorique = new Information<Boolean>();
		 messageTheorique.add(false);			
		 assertEquals("le message empirique ne devrait pas être égal au message théorique",recepteurTransducteur.getInformationEmise(), messageTheorique);
		}
		@Test
		public void testMessageConforme7() throws InformationNonConforme {
		message = new Information<Boolean>();
		 message.add(true);
		 message.add(true);
		 message.add(true);
		 recepteurTransducteur.recevoir(message);
		 Information<Boolean> messageTheorique = new Information<Boolean>();
		 messageTheorique.add(true);			
		 assertEquals("le message empirique ne devrait pas être égal au message théorique",recepteurTransducteur.getInformationEmise(), messageTheorique);
		}
		@Test
		public void testMessageNonConforme() throws InformationNonConforme {
		message = new Information<Boolean>();
		 message.add(true);
		 message.add(false);
		 message.add(false);
		 recepteurTransducteur.recevoir(message);
		 Information<Boolean> messageTheorique = new Information<Boolean>();
		 messageTheorique.add(false);			
		 assertFalse("le message empirique ne devrait pas être égal au message théorique",recepteurTransducteur.getInformationEmise().equals(messageTheorique));
		}
}
