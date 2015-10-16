

package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import transmetteurs.EmetteurNonConforme;
import transmetteurs.TransmetteurAnalogiqueMultiTrajet;
import transmetteurs.TransmetteurAnalogiqueMultiTrajetNonConforme;

public class TransmetteurAnalogiqueMultiTrajetTest {
 private ArrayList<Integer> tau = new ArrayList<Integer>(4);
 private ArrayList<Integer> tau1 = new ArrayList<Integer>(0);
 private ArrayList<Integer> tau2 = new ArrayList<Integer>(6);
 private ArrayList<Double> alpha= new ArrayList<Double>(4);
 private ArrayList<Double> alpha1= new ArrayList<Double>(0);
 private ArrayList<Double> alpha2= new ArrayList<Double>(1);
 private TransmetteurAnalogiqueMultiTrajet transmetteurAnalogiqueMultiTrajet1;
 private TransmetteurAnalogiqueMultiTrajet transmetteurAnalogiqueMultiTrajet2;


 public TransmetteurAnalogiqueMultiTrajetTest() {
 }


 @BeforeClass
 public static void setUpClass() {


 }

 @AfterClass
 public static void tearDownClass() {
 }

 @Before
 public void setUp() {
   tau.add(1);
   tau.add(2);
   tau.add(3);
   tau.add(4);
   alpha.add(1.0);
   alpha.add(0.5);
   alpha.add(0.8);
   alpha.add(0.25);

   tau2.add(1);
   tau2.add(2);
   tau2.add(-3);
   tau2.add(4);
   tau2.add(5);
   tau2.add(6);

   alpha1.add(1.0);
   alpha1.add(0.5);
   alpha1.add(0.8);
   alpha1.add(0.55);
   alpha1.add(0.68);
   alpha1.add(0.9);
   alpha1.add(-0.1);

   alpha2.add(2.0);
 }

 @After
 public void tearDown() {

 }
 //On ne peut avoir qu'entre 1 et 5 multi-trajet(s) maximum
 @Test (expected=TransmetteurAnalogiqueMultiTrajetNonConforme.class)
 public void testTransmetteurAnalogiqueMultiTrajetNonConformeTailleTauInferieureA1() throws Exception{
   System.out.println("Test 0 du constructeur : ");
   transmetteurAnalogiqueMultiTrajet1= new TransmetteurAnalogiqueMultiTrajet(tau1, alpha );
 }
 //On ne peut avoir qu'entre 1 et 5 multi-trajet(s) maximium
 @Test (expected=TransmetteurAnalogiqueMultiTrajetNonConforme.class)
 public void testTransmetteurAnalogiqueMultiTrajetNonConformeTailleTauSup5() throws Exception{
   System.out.println("Test 1 du constructeur : ");
   transmetteurAnalogiqueMultiTrajet1= new TransmetteurAnalogiqueMultiTrajet(tau2, alpha );
 }

 //On ne peut avoir qu'entre 1 et 5 multi-trajet(s) maximum
 @Test (expected=TransmetteurAnalogiqueMultiTrajetNonConforme.class)
 public void testTransmetteurAnalogiqueMultiTrajetNonConformeTailleAlphaInf1() throws Exception{
   System.out.println("Test 2 du constructeur : ");
   transmetteurAnalogiqueMultiTrajet1= new TransmetteurAnalogiqueMultiTrajet(tau, alpha1 );
 }

 //On ne peut avoir qu'entre 1 et 5 multi-trajet(s) maximum
 @Test (expected=TransmetteurAnalogiqueMultiTrajetNonConforme.class)
 public void testTransmetteurAnalogiqueMultiTrajetNonConformeTailleAlphaSup5() throws Exception{
   System.out.println("Test 3 du constructeur : ");
   transmetteurAnalogiqueMultiTrajet1= new TransmetteurAnalogiqueMultiTrajet(tau, alpha1 );
 }

 //On doit forcemént avoir un tau associé à un alpha
 @Test (expected=TransmetteurAnalogiqueMultiTrajetNonConforme.class)
 public void testTransmetteurAnalogiqueMultiTrajetNonConformeTailleAlphaDiffTailleTau() throws Exception{
   System.out.println("Test 4 du constructeur : ");
   transmetteurAnalogiqueMultiTrajet1= new TransmetteurAnalogiqueMultiTrajet(tau, alpha1 );
 }

 //Test de conformité
 @Test
 public void testTransmetteurAnalogiqueMultiTrajetConformeTailleAlphaEgaleTailleTau() throws Exception{
   System.out.println("Test 5 du constructeur : ");
   transmetteurAnalogiqueMultiTrajet1= new TransmetteurAnalogiqueMultiTrajet(tau, alpha );
 }
 
 //Le décalage ne peut pas être négatif
 @Test (expected=TransmetteurAnalogiqueMultiTrajetNonConforme.class)
 public void testTransmetteurAnalogiqueMultiTrajetNonConformeValeurMinimaleTauNegative() throws Exception{
   System.out.println("Test 6 du constructeur : ");
   transmetteurAnalogiqueMultiTrajet1= new TransmetteurAnalogiqueMultiTrajet(tau2, alpha1 );
 }


 //L'atténuation minimale ne peut pas être négative
 @Test (expected=TransmetteurAnalogiqueMultiTrajetNonConforme.class)
 public void testTransmetteurAnalogiqueMultiTrajetNonConformeValeurMinimaleAlphaNegative() throws Exception{
   System.out.println("Test 7 du constructeur : ");
   transmetteurAnalogiqueMultiTrajet1= new TransmetteurAnalogiqueMultiTrajet(tau2, alpha1 );
 }

 //L'atténuation maximale ne peut pas être supérieure à 1
 @Test (expected=TransmetteurAnalogiqueMultiTrajetNonConforme.class)
 public void testTransmetteurAnalogiqueMultiTrajetNonConformeValeurMaximaleAlphaSup1() throws Exception{
   System.out.println("Test 8 du constructeur : ");
   transmetteurAnalogiqueMultiTrajet1= new TransmetteurAnalogiqueMultiTrajet(tau2, alpha2 );
 }


}

