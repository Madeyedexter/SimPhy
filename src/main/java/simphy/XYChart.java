/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package simphy;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Madeyedexter
 */
public class XYChart extends JFrame implements WindowListener{

public Circle c=null;
public Pendulum p=null;
private XYSeries series;
private XYSeries series1;
private XYSeriesCollection dataset;
private JLabel label;
private BufferedImage image;
    private final JFreeChart chart;
    private final ChartPanel panel;
    private String type;

public XYChart(Pendulum p)
{
    this.p=p;
    type="pendulum";
    // Create a simple XY chart
    series = new XYSeries("Omega-Time Plot");
    series1= new XYSeries("Theta-Time Plot");
    dataset = new XYSeriesCollection();
    dataset.addSeries(series);
    dataset.addSeries(series1);
    // Generate the graph
    chart = ChartFactory.createXYLineChart(
    "Velocity/Angle Graph", // Title
    "time", // x-axis Label
    "Angular Velocity(rad/s)/Angle(rad)", // y-axis Label
    dataset, // Dataset
    PlotOrientation.VERTICAL, // Plot Orientation
    true, // Show Legend
    true, // Use tooltips
    false // Configure chart to generate URLs?
    );
    this.setTitle(p.toString());
    this.setBounds(950, 0, 400, 700);
    panel = new ChartPanel(chart, false);
    this.add(panel, BorderLayout.CENTER);
    this.setAlwaysOnTop(true);
    this.addWindowListener(this);
    this.pack();
    
    SwingUtilities.invokeLater(new Runnable(){
    @Override
    public void run()
            {
                XYChart.this.setVisible(true);
            }
    });
    
    
}
public XYChart(Circle c)
{
    this.c=c;
    // Create a simple XY chart
    type="circle";
    series = new XYSeries("VT Plot");
    dataset = new XYSeriesCollection();
    dataset.addSeries(series);
    // Generate the graph
    chart = ChartFactory.createXYLineChart(
    "Velocity-Time Graph", // Title
    "time(s)", // x-axis Label
    "velocity(m/s)", // y-axis Label
    dataset, // Dataset
    PlotOrientation.VERTICAL, // Plot Orientation
    true, // Show Legend
    true, // Use tooltips
    false // Configure chart to generate URLs?
    );
    this.setTitle(c.getID());
    this.setBounds(950, 0, 400, 700);
    panel = new ChartPanel(chart, false);
    this.add(panel, BorderLayout.CENTER);
    this.setAlwaysOnTop(true);
    this.addWindowListener(this);
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.pack();
    
    SwingUtilities.invokeLater(() -> {
        XYChart.this.setVisible(true);
    });
    
}


public void addPoint(double time)
{
    switch(type)
    {
        case "circle": series.add(time, c.getV().getLength());
                        break;
        case "pendulum": series.add(time, p.getOmega());
                            series1.add(time,p.getTheta());
                            break;
    }
}

    @Override
    public void windowOpened(WindowEvent we) {}

    @Override
    public void windowClosing(WindowEvent we) {
        AnimationUpdater.chartTime=0;
        DrawingPanel.drawChart=false;
    }

    @Override
    public void windowClosed(WindowEvent we) {}

    @Override
    public void windowIconified(WindowEvent we) {}

    @Override
    public void windowDeiconified(WindowEvent we) {}

    @Override
    public void windowActivated(WindowEvent we) {}

    @Override
    public void windowDeactivated(WindowEvent we) {}

}

    
