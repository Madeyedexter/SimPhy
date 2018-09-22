/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simphy;
import java.awt.Color;
import java.awt.Graphics2D;
/**
 *
 * @author Madeyedexter
 */
public class GhostBall 
{
    public int W;
    public  int H;
    public int TOP;
    public int LEFT;
    public int x;
    public int y;
    public  float radius;
    public void drawGhostBall(Graphics2D g2)
    {
        g2.setColor(Color.BLACK);
        g2.drawOval(LEFT,TOP , W, H);
        g2.drawString("Radius = "+radius+" m", x-8, y-5);
    }   
}