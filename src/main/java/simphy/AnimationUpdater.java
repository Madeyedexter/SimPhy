/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simphy;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author Madeyedexter
 */
public class AnimationUpdater extends Thread
{
   private float interval=0.0f;
   private final DrawingPanel drawingPanel;
    private final JLabel fpslabel;
    private int frameCount;
    private long currentTime;
    private float elapsedTime;
    private int totalElapsedTime;
    public static double chartTime=0;
    
//    private ODE ballODE;
    
   public AnimationUpdater(DrawingPanel d, JLabel l)
   {
       this.drawingPanel=d;
       this.fpslabel=l;
       frameCount=0;
       
    
   }
    @Override
    public void run() 
    {
       long previousTime = System.currentTimeMillis();
       while(true)
       {
            currentTime=System.currentTimeMillis();
            elapsedTime=currentTime-previousTime;
            interval=(elapsedTime)/1000;
            totalElapsedTime+=elapsedTime;
            if(totalElapsedTime>1000)
            {
                fpslabel.setText("FPS: "+frameCount);
                frameCount = 0;
		totalElapsedTime = 0;
            }
            try 
            {
                updateCircle();
            }
            catch (InterruptedException ex) 
            {
                Logger.getLogger(AnimationUpdater.class.getName()).log(Level.SEVERE, null, ex);
            }
            previousTime=currentTime;
            frameCount++;
            
            if(!DrawingPanel.RUNNING)
            synchronized(this)
            {
                try 
                {
                    fpslabel.setText("FPS: "+0);
                    wait();
                    previousTime=System.currentTimeMillis();
                            
                }
                catch (InterruptedException ex) 
                {
                    Logger.getLogger(AnimationUpdater.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
       }
    }

    private void updateCircle() throws InterruptedException 
    {
        //before updating remove items
        Iterator<Circle> j=Items.getRemoveQueue().iterator();
        while(j.hasNext())
        {
            Circle c=j.next();
            Items.removeCircle(c);
            Items.removeid(c.getID());
        }
        for(int i=0;i<Items.numCircles();i++)
        {
            Circle c=Items.getCircle(i);
            Vector2D v=c.getV(),pos=c.getCenter();            
            double VX=v.getX();
            double VY=v.getY()+-Utils.GRAVITY*interval;
            double X=pos.getX()+VX*interval;
            double Y=pos.getY()+(v.getY()*interval+(0.5f)*Utils.GRAVITY*interval*interval);
                    c.getCenter().setX(X);
                    c.getCenter().setY(Y);
                    c.getV().setX(VX);
                    c.getV().setY(VY);
                    c.setTheta(c.getTheta()+c.getOmega()*interval);
                    
        }
        for(int i=0;i<Items.numPendulums();i++)
        {
            Pendulum p=Items.getPendulum(i);
            double omega0=p.getOmega();
            double omega=omega0+p.getK()*(-Utils.GRAVITY*Math.sin(p.getTheta())*interval/p.getLength());//*Math.pow(Math.E,-0.4*p.elapsedTime);
            double theta=p.getTheta()+(omega0*interval-Utils.GRAVITY*Math.sin(p.getTheta())*interval*interval/2);//*Math.pow(Math.E,-0.2*p.elapsedTime);//*Math.exp(-0.3*p.elapsedTime);
            p.elapsedTime+=interval;
            p.setOmega(omega);
            p.setTheta(theta);
        }
            Utils.checkCollisions();
            drawingPanel.draw();
            if(drawingPanel.chart!=null && DrawingPanel.drawChart)
            {
                chartTime+=interval;
                drawingPanel.chart.addPoint(chartTime);
            }
            else
                chartTime=0;
            
                        
    }
}
