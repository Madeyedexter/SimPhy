/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simphy;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author Madeyedexter
 */
public class Utils 
{
    public static double GRAVITY=9.8f;
    public static final double PI=3.142f;
    public static double RESTITUTION=0.8f;
    public static double FRICTION=0.4f;
    public static double T_IMPACT=0.02f;
    
    private static final Random r=new Random();
    
    public static boolean checkSpaceCircle(int x,int y, int radius)
    {
        return true;
    }

    public static Color randomColor()
    {
        int R=r.nextInt(256);
        int G=r.nextInt(256);
        int B=r.nextInt(256);
        return new Color(R,G,B);
    }
    
    public static void checkCollisions()
	{
		Collections.sort(Items.getCircles());
                for(int i=0;i<Items.numCircles();i++)
                {
                    Circle c=Items.getCircle(i);
                    double X=c.getCenter().getX(),Y=c.getCenter().getY(),VX=c.getV().getX(),VY=c.getV().getY();
                    if(X<c.getRadius())
                    {
                        if(Utils.FRICTION!=0.0f)
                            rotate(c,new Vector2D(0,Y));
                        VX=Math.abs(VX)*Utils.RESTITUTION;
                        X=c.getRadius();
                        c.getV().setX(VX);
                        c.getCenter().setX(X);
                    }
                    if(Y<c.getRadius())
                    {
                        if(Utils.FRICTION!=0.0f)
                            rotate(c,new Vector2D(0,X));
                        VY=Math.abs(VY)*Utils.RESTITUTION;
                        Y=c.getRadius();
                        c.getCenter().setY(Y);
                        c.getV().setY(VY);
                    }
                    if(X+c.getRadius()>DrawingPanel.WIDTH_DP*(5.0f/120.0f))
                    {
                        if(Utils.RESTITUTION!=0.0f)
                            rotate(c,new Vector2D(DrawingPanel.WIDTH_DP*(5.0f/120.0f),Y));
                        X=DrawingPanel.WIDTH_DP*(5.0f/120.0f)-c.getRadius();
                        VX=-(VX*Utils.RESTITUTION);
                        c.getCenter().setX(X);
                        c.getV().setX(VX);
                    }
                    if(Y+c.getRadius()>DrawingPanel.HEIGHT_DP*(5.0f/120.0f))
                    {
                        if(Utils.RESTITUTION!=0.0f)
                            rotate(c,new Vector2D(X,DrawingPanel.HEIGHT_DP*(5.0f/120.0f)));
                        Y=DrawingPanel.HEIGHT_DP*(5.0f/120.0f)-c.getRadius();
                        VY=-(VY*Utils.RESTITUTION);
                        c.getCenter().setY(Y);
                        c.getV().setY(VY);                       
                    }
                    for(int j = i + 1; j < Items.numCircles(); j++)
			{
                            Circle d=Items.getCircle(j);
				if ((X + c.getRadius()) < (d.getCenter().getX() - d.getRadius()))
						break;

				if((Y + c.getRadius()) < (d.getCenter().getY() - d.getRadius()) ||
				   (d.getCenter().getY() + d.getRadius()) < (Y - c.getRadius()))
				   		continue;
				c.resolveCollision(d);
			}
                }
                //collision with inclined planes
                for(int i=0;i<Items.numCircles(); i++)
                {
                    for(int j=0;j<Items.numPlanes();j++)
                    {
                        Circle c=Items.getCircle(i);
                        c.resolveCollision(Items.getPlane(j));
                    }
                }
	}

    static void loadScene(String URI, ClassLoader classLoader) {
            try
            {
                ObjectInputStream ois=new ObjectInputStream(classLoader.getResourceAsStream(URI));
                //read gravity
                Utils.GRAVITY=ois.readDouble();
                //read restitution
                Utils.RESTITUTION=ois.readDouble();
                //read ids
                int numIds=ois.readInt();
                for(int i=0;i<numIds;i++)
                    Items.addid((String)ois.readObject());
                //read circles
                int numCircles=ois.readInt();
                for(int i=0;i<numCircles;i++)
                    Items.addCircle((Circle)ois.readObject());
                //read Pendulums
                int numPend=ois.readInt();
                for(int i=0;i<numPend;i++)
                    Items.addPendulum((Pendulum)ois.readObject());
                //read Planes
                int numPlanes=ois.readInt();
                for(int i=0;i<numPlanes;i++)
                    Items.addPlane((Plane)ois.readObject());
            }
            catch (IOException | ClassNotFoundException ex)
            {
                System.out.println(ex.getMessage());
            }
        }
    
    public double getAngle(Vector2D screenPoint)
{
    double dx = screenPoint.getX();
    // Minus to correct for coord re-mapping
    double dy = -(screenPoint.getY());

    double inRads = Math.atan2(dy,dx);

    // We need to map to coord system when 0 degree is at 3 O'clock, 270 at 12 O'clock
    if (inRads < 0)
        inRads = Math.abs(inRads);
    else
        inRads = 2*Math.PI - inRads;

    return Math.toDegrees(inRads);
}
    /*
    public static double[] integrate(double curPos,double curVel, double dt)
    {
        double xa=curPos;
        double va=curVel;
        
        double xb=xa+0.5*dt*curVel;
        return 0.0f;
    }*/
    
    public static void rotate(Circle c, Vector2D p) 
    {
        //find Rcp
        Vector2D Rcp=p.subtract(c.getCenter());
        //find velocity of point of contact
        Vector2D vContact=Rcp.crossOmega(c.getOmega());
        //find Velocity vector perpendicular to Rcp
        Rcp.normalize();
        Vector2D Rcpn=new Vector2D(Rcp.getY(),-Rcp.getX());
        Vector2D vParallel=Rcpn.multiply(c.getV().dot(Rcpn));
        Vector2D vNormal=Rcp.multiply(c.getV().dot(Rcp));
        
        vContact=vContact.add(vParallel);
        vContact=vContact.normalize();
        if(vContact.getLength()!=0.0f)
        {
            vContact.multiply(-1);
            
        }
        
        
        
            
    }
    
    private static void rotateY(Circle c)
    {
            double omega=((-(2*c.getV().getX())/(3*c.getRadius()))+c.getOmega()/3);
            double velocityX=-c.getRadius()*omega;
            c.setOmega(omega);
            c.getV().setX(velocityX);
    }
}
