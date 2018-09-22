/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simphy;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 *
 * @author Madeyedexter
 */
public class PropertyTableUpdater extends Thread
{
    private final JTableCircle circleTable;
    private final DrawingPanel panel;
    private Circle c;
    
    public PropertyTableUpdater(JTableCircle t1, DrawingPanel p)
    {
        this.circleTable=t1;
        this.panel=p;
    }
   
    @Override
    public void run()
    {
        while(true)
        {
            switch(DrawingPanel.selectedItem)
            {
                case "circle":try 
                                {
                                    updateCircleTable();
                                } 
                                catch (InterruptedException ex) 
                                {
                                    Logger.getLogger(PropertyTableUpdater.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                break;
                    
            }
            
            if(DrawingPanel.selectedItem.equals("NONE"))
            {    
                circleTable.setVisible(false);
            }
            while(DrawingPanel.selectedItem.equals("NONE"));
                circleTable.setVisible(true);
        }
    }

    private void updateCircleTable() throws InterruptedException 
    {
        c=panel.getDraggedCircle();
        if(c!=null)
        {
           circleTable.setValueAt(c.getID(), 0, 1);
           circleTable.setValueAt(c.getRadius(), 1,1);
           circleTable.setValueAt(c.getCenter().toString(),2,1);
           circleTable.setValueAt(c.getMass(),3,1);
           circleTable.setValueAt(c.getColor().getRed()+","+c.getColor().getGreen()+","+c.getColor().getBlue(),4,1);
           circleTable.setValueAt(c.getV().toString(),5,1);
           circleTable.setValueAt("0,"+(-Utils.GRAVITY),6,1);
        }
        Thread.sleep(500);
            
    }
/*
    private void updatePendulumTable() throws InterruptedException {
        p=panel.getDraggedPendulum();
        if(p!=null)
        {
           pendulumTable.setValueAt(p.toString(), 0, 1);
           pendulumTable.setValueAt(p.getRadius(), 1,1);
           pendulumTable.setValueAt(p.getMass(),2,1);
           pendulumTable.setValueAt(p.getColor()+","+p.getColor().getGreen()+","+p.getColor().getBlue(),3,1);
           pendulumTable.setValueAt(p.getOmega(),4,1);
           pendulumTable.setValueAt(p.getTheta(),5,1);
        }
        Thread.sleep(500);
    }
    */
}
