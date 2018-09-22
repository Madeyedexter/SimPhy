/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package simphy;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;

/**
 *
 * @author Madeyedexter
 */
class Plane implements Serializable
{
    private String ID;
    private Color color;
    private Vector2D p1,p2,p3;
    public Plane(Vector2D p1, Vector2D p2, Vector2D p3)
    {
        this.p1=p1;
        this.p2=p2;
        this.p3=p3;
        color=Color.GRAY;
        
    }
    
    public void drawPlane(Graphics2D g2)
    {
        g2.setColor(color);
        int x1=(int)(p1.getX()*DrawingPanel.scale);
        int x2=(int)(p2.getX()*DrawingPanel.scale);
        int x3=(int)(p3.getX()*DrawingPanel.scale);
        int y1=DrawingPanel.HEIGHT_DP-(int)(p1.getY()*DrawingPanel.scale);
        int y2=DrawingPanel.HEIGHT_DP- (int)(p2.getY()*DrawingPanel.scale);
        int y3=DrawingPanel.HEIGHT_DP- (int)(p3.getY()*DrawingPanel.scale);
        g2.fillPolygon(new int[]{x1,x2,x3}, new int[]{y1,y2,y3},3);
    }

    Vector2D getV1() {
return p1;    }

    Vector2D getV2() {
return p2;    }

    Vector2D getV3() {
return p3;    }

    void translate(double i, double i0) {
    p1.add(i,i0);
    p2.add(i,i0);
    p3.add(i,i0);
    
    }
    
}
