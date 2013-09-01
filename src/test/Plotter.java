package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;



public class Plotter {
	public static void main(String args[]) throws IOException
	{
		BufferedReader br=new BufferedReader(new FileReader("ErrorPlot.txt"));
		String s;
		XYSeries xyseries=new XYSeries("Error Plot");
		int i=10;
		while((s=br.readLine())!=null)
		{
			s=s.split("[ ]+")[0];
			xyseries.add(i, Double .parseDouble(s));
			i++;
		}
		br.close();
		XYDataset xydataset=new XYSeriesCollection(xyseries);
		JFreeChart chart=ChartFactory.createXYLineChart("LMS Error Plot","#Experiment","Error Per Move", xydataset,PlotOrientation.VERTICAL, true, true, false);
		ChartFrame chartFrame=new ChartFrame("LMS Error Plot",chart);
		chartFrame.setVisible(true);
		chartFrame.setSize(1000, 700);
	}

}
