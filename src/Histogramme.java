import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

 
import org.jfree.data.*;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;




public class Histogramme extends ApplicationFrame{

	public Histogramme(String title, DefaultCategoryDataset dataset) {
		super(title);
		JFreeChart linechart = ChartFactory.createLineChart("Histogramme gaussien", "Amplitude", "Valeur", dataset, PlotOrientation.VERTICAL, true, true, false);
		ChartPanel chartPannel = new ChartPanel(linechart);
		chartPannel.setPreferredSize(new java.awt.Dimension(1900,1080));
		setContentPane(chartPannel);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		float sigma = 1;
		ArrayList<Integer> histo = new ArrayList<Integer>(50);
		ArrayList<Float> valeur = new ArrayList<>();
		
		//Déclaraion de 2 loi uniforme
		Random a1 = new Random();
		Random a2 = new Random();
		
		for(int i = 0; i<30000*1000; i++) {
			//Calcul du bruit et ajout au signal
			valeur.add((float) (sigma*Math.sqrt(-2*Math.log(1-a1.nextFloat()))*Math.cos(2*Math.PI*a2.nextFloat())));
		}
		
		float max = valeur.get(0); 
		float min = valeur.get(0);
		
		for(int i=0; i<valeur.size(); i++) {
			if(min > valeur.get(i)) {
				min = valeur.get(i);
			}
			
			
			if(max < valeur.get(i)) {
				max = valeur.get(i);
			}
		}
		for(int i = 0; i < 50; i++)
		{
			histo.add(0);
			
		}
		
		float interval = (max - min)/50;
		
		for (float val : valeur)
		{
			int indice = Math.round((val-min)/interval);
			if(indice == 50)
				indice--;
			//System.out.println("indice : " + indice);
			histo.set(indice, histo.get(indice) + 1);
			
		}
		
		for (float val2 : histo)
		{
			System.out.println(val2);
				}
	
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for(int i =0 ; i < 50 ; i++)
		{
			
		dataset.addValue((double)histo.get(i), "histogramme", "" + i*interval);	
		}
		
		
		Histogramme chart = new Histogramme("Histogramme Gaussien", dataset);
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	

	
	}
	
	/**
	 * @param args
	 */
		// TODO Auto-generated method stub


}





	
	
	
	

