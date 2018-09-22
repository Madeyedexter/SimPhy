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
public class Pendulum implements Serializable
{
    private Vector2D pivot;
    private double length;
    private Vector2D center;
    private double mass;
    private double radius;
    private Color color;
    private double damp;
    private double omega;
    private double k;
    private double theta;
    double elapsedTime=0;
   
    Pendulum(double x1, double y1, double x2, double y2) 
    {
        pivot=new Vector2D(x1,y1);
        center=new Vector2D(x2,y2);
        length=pivot.getDistance(center);
        mass=5.0f;
        radius=0.5f;    
        k=-2*length/(radius*radius+2*length*length);
        theta=Math.atan((center.getY()-pivot.getY())/((center.getX()-pivot.getX())));
        if(theta>-Math.PI/2 && theta<Math.PI/2)
        {
            theta=-theta-Math.PI/2;
        }
        color=Utils.randomColor();
    }

    /**
     * @return the pivot
     */
    public Vector2D getPivot() {
        return pivot;
    }

    /**
     * @param pivot the pivot to set
     */
    public void setPivot(Vector2D pivot) {
        this.pivot = pivot;
    }

    /**
     * @return the length
     */
    public double getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(float length) {
        this.length = length;
    }

    /**
     * @return the center
     */
    public Vector2D getCenter() {
        return center;
    }

    /**
     * @param center the center to set
     */
    public void setCenter(Vector2D center) {
        this.center = center;
    }

    /**
     * @return the mass
     */
    public double getMass() {
        return mass;
    }

    /**
     * @param mass the mass to set
     */
    public void setMass(double mass) {
        this.mass = mass;
    }
    
    public void drawPendulum(Graphics2D g2)
    {
        int x1=(int) (pivot.getX()*DrawingPanel.scale);
        int y1= DrawingPanel.HEIGHT_DP-(int) (pivot.getY()*DrawingPanel.scale);
        int x2=(int) (getCenter().getX()*DrawingPanel.scale);
        int y2= DrawingPanel.HEIGHT_DP-(int) (getCenter().getY()*DrawingPanel.scale);
        int r = (int) (getRadius()*DrawingPanel.scale);
        g2.setColor(Color.BLACK);
        g2.fillOval(x1-2,y1-2,4,4);
        g2.drawLine(x1,y1,x2,y2);
        g2.setColor(color);
        g2.fillOval(x2-r,y2-r,2*r,2*r);
        g2.setColor(Color.BLACK);
    }

    /**
     * @return the radius
     */
    public double getRadius() {
        return radius;
    }

    /**
     * @param radius the radius to set
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * @return the damp
     */
    public double getDamp() {
        return damp;
    }

    /**
     * @param damp the damp to set
     */
    public void setDamp(double damp) {
        this.damp = damp;
    }

    /**
     * @return the omega
     */
    public double getOmega() {
        return omega;
    }

    /**
     * @param omega the omega to set
     */
    public void setOmega(double omega) {
        this.omega = omega;
    }

    /**
     * @return the k
     */
    public double getK() {
        return k;
    }

    /**
     * @param k the k to set
     */
    public void setK(double k) {
        this.k = k;
    }

    /**
     * @return the theta
     */
    public double getTheta() {
        return theta;
    }

    /**
     * @param theta the theta to set
     */
    public void setTheta(double theta) {
        this.theta = theta;
        this.setCenter(new Vector2D((double)(pivot.getX()+length*Math.sin(theta)),(double)(pivot.getY()+length*Math.cos(theta))));
    }

    void setColor(Color col) {
        
    this.color=col;}

    Color getColor() {return color;}
    
    
    
    
}
