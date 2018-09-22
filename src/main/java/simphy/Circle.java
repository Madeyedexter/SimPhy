/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simphy;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.Random;
import static simphy.DrawingPanel.scale;

/**
 *
 * @author Madeyedexter
 */
public class Circle implements Comparable<Circle>, Serializable
{
    private final String ID;
    private double radius;
    private Vector2D center;
    private double mass;
    private Color color;
    private double theta;
    private Vector2D V;
    private final Vector2D VT;
    private Random r;
    private double omega;
    
    public Circle(String id,double r,Vector2D c,double m)
    {
        ID=id;
        radius=r;
        center=c;
        mass=m;
        color=Utils.randomColor();
        V=new Vector2D();
        VT=new Vector2D();
        theta=0;
        omega=0;
    }


    /**
     *
     * @param g2
     */
    public void drawCircle(Graphics2D g2)
    {
            g2.setColor(color);
            int ra=(int)(radius*scale);
            int x=(int)(center.getX()*scale-ra);
            int y=DrawingPanel.HEIGHT_DP-(int)((center.getY()+radius)*scale);
            double t=-theta;
            g2.fillOval(x, y,2*ra,2*ra);
            g2.setColor(Color.BLACK);
            g2.drawLine(x+ra, y+ra,x+ra+(int)(ra*Math.cos(t))-1 , y+ra+(int)(ra*Math.sin(t)));
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
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * @return the V
     */
    public Vector2D getV() {
        return V;
    }

    /**
     * @param V the V to set
     */
    public void setV(Vector2D V) {
        this.V = V;
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

    String getID() 
    {
        return this.ID;            
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
    }
    
//collision with circle
    void resolveCollision(Circle c) 
    {
        // get the mtd
		Vector2D delta = (center.subtract(c.getCenter()));
		double rad = getRadius() + c.getRadius();
		double dist2 = delta.dot(delta);

		if (dist2 > rad*rad) return; // they aren't colliding


		double d = delta.getLength();

		Vector2D mtd;
		if (d != 0.0f)
		{
			mtd = delta.multiply(((getRadius() + c.getRadius())-d)/d); // minimum translation distance to push balls apart after intersecting

		}
		else // Special case. Balls are exactly on top of eachother.  Don't want to divide by zero.
		{
			d = c.getRadius() + getRadius() - 1.0f;
			delta = new Vector2D(c.getRadius() + getRadius(), 0.0f);

			mtd = delta.multiply(((getRadius() + c.getRadius())-d)/d);
		}

		// resolve intersection
		double im1 = 1 / getMass(); // inverse mass quantities
		double im2 = 1 / c.getMass();

		// push-pull them apart
		center = center.add(mtd.multiply(im1 / (im1 + im2)));
		c.setCenter(c.getCenter().subtract(mtd.multiply(im2 / (im1 + im2))));

		// impact speed
		Vector2D v = (V.subtract(c.getV()));
		double vn = v.dot(mtd.normalize());

		// sphere intersecting but moving away from each other already
		if (vn > 0.0f) return;

		// collision impulse
		double i = (-(1.0f + Utils.RESTITUTION) * vn) / (im1 + im2);
		Vector2D impulse = mtd.multiply(i);

		// change in momentum
		V = V.add(impulse.multiply(im1));
		c.setV(c.getV().subtract(impulse.multiply(im2)));
}

//collision with triangle
    
    public void resolveCollision(Plane p)
    {
        
        
        Vector2D v1=p.getV1(), v2=p.getV2(), v3=p.getV3(); 
        //intersection with vertex of plane
        //TEST 1: Vertex within circle
        double c1x = v1.getX() - center.getX();
        double c1y = v1.getY() - center.getY();
        if (c1x*c1x + c1y*c1y <= radius*radius)
        {
            double angle=Math.atan2(-c1x, c1y);
            Vector2D v=V.rotate(angle);
            if(v.getY()<0.0f)
                return;
            v.setY(-Utils.RESTITUTION*v.getY());
            V=v.rotate(-angle);
            Utils.rotate(this, v1);
            return;
        }
        double c2x = v2.getX() - center.getX();
        double c2y = v2.getY() - center.getY();
        if (c2x*c2x + c2y*c2y <= radius*radius)
        {
            double angle=Math.atan2(-c2x, c2y);
            Vector2D v=V.rotate(angle);
            if(v.getY()<0.0f)
                return;
            v.setY(-Utils.RESTITUTION*v.getY());
            V=v.rotate(-angle);
            return;
        }
        double c3x = v3.getX() - center.getX();
        double c3y = v3.getY() - center.getY();
        if (c3x*c3x + c3y*c3y <= radius*radius)
        {
            double angle=Math.atan2(-c3x, c3y);
            Vector2D v=V.rotate(angle);
            if(v.getY()<0.0f)
                return;
            v.setY(-Utils.RESTITUTION*v.getY());
            V=v.rotate(-angle);
            return;
        }
        //intersection with edge of plane
        //first edge
        c1x = -c1x;
        c1y = -c1y;
        double e1x = v2.getX() - v1.getX();
        double e1y = v2.getY() - v1.getY();
        double k = c1x*e1x + c1y*e1y;
        if (k > 0)
        {
            double len = Math.sqrt(e1x*e1x + e1y*e1y);
            k = (double) (k/len);
            if (k < len)
            {
                if (c1x*c1x + c1y*c1y - k*k <= radius*radius)
                {
                    
                    double angle=(double) (Math.atan(e1y/e1x));
                    System.out.println(Math.toDegrees(angle));
                    Vector2D v=V.rotate(angle);
                    if(v.getY()>0)
                        return;
                    v.setY(-v.getY()*Utils.RESTITUTION);
                    V=v.rotate(-angle);
                    return;
                }
            }
        }
        // Second edge
        c2x = -c2x;
        c2y = -c2y;
        double e2x = v3.getX() - v2.getX();
        double e2y = v3.getY() - v2.getY();
        k = c2x*e2x + c2y*e2y;
        if (k > 0)
        {
            double len = Math.sqrt(e2x*e2x + e2y*e2y);
            k = (double) (k/len);
            if (k < len)
            {
                if (Math.sqrt(c2x*c2x + c2y*c2y - k*k) <= radius)
                {
                    if(V.getY()<0.0f)
                        return;
                    V.setY(-V.getY()*Utils.RESTITUTION);
                    return;
                }
            }
        }
        // Third edge
        c3x = -c3x;
        c3y = -c3y;
        double e3x = v1.getX() - v3.getX();
        double e3y = v1.getY() - v3.getY();
        k = c3x*e3x + c3y*e3y;
        if (k > 0)
        {
            double len = Math.sqrt(e3x*e3x + e3y*e3y);
            k = (double) (k/len);
            if (k < len)
            {
                if (Math.sqrt(c3x*c3x + c3y*c3y - k*k) <= radius)
                {
                    if(center.getX()<v1.getX() && V.getX()<0.0f)
                        return;
                    V.setX(-V.getX()*Utils.RESTITUTION);
                }
            }
        }
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

    @Override
    public int compareTo(Circle o) 
    {
        if (this.center.getX() - this.getRadius() > o.center.getX() - o.getRadius())
		{
			return 1;
		}
		else if (this.center.getX() - this.getRadius() < o.center.getX() - o.getRadius())
		{
			return -1;
		}
		else
		{
			return 0;
		}}

}
