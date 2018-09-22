/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simphy;

import java.awt.Color;

/**
 *
 * @author Madeyedexter
 */
public class Block 
{
    private String ID;
    private int width;
    private int height;
    private int mass=10;
    private boolean fixed=false;
    private int TopX;
    private int TopY;
    private float damping;
    float[] V;
    float[] A;
    private Color color;
    
    public Block(String id,int w,int h,int m,boolean f,int bx,int by, float d, Color c)
    {
        ID=id;
        width=w;
        height=h;
        mass=m;
        fixed=f;
        TopX=bx;
        TopY=by;
        damping=d;
        V=new float[2];
        A=new float[2];
        color=c;
    }

    /**
     * @return the ID
     */
    public String getID() {
        return ID;
    }


    /**
     * @return the width
     */
    public int getWidth() 
    {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @return the mass
     */
    public int getMass() {
        return mass;
    }

    /**
     * @param mass the mass to set
     */
    public void setMass(int mass) {
        this.mass = mass;
    }

    /**
     * @return the fixed
     */
    public boolean isFixed() {
        return fixed;
    }

    /**
     * @param fixed the fixed to set
     */
    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    /**
     * @return the BOXX
     */
    public int getTopX() {
        return TopX;
    }

    /**
     * @param BOXX the BOXX to set
     */
    public void setTopX(int BOXX) {
        this.TopX = BOXX;
    }

    /**
     * @return the BOXY
     */
    public int getTopY() {
        return TopY;
    }

    /**
     * @param BOXY the BOXY to set
     */
    public void setBOXY(int BOXY) {
        this.TopY = BOXY;
    }

    /**
     * @return the damping
     */
    public float getDamping() {
        return damping;
    }

    /**
     * @param damping the damping to set
     */
    public void setDamping(float damping) {
        this.damping = damping;
    }

    /**
     * @return the V
     */
    public float[] getV() {
        return V;
    }

    /**
     * @param V the V to set
     */
    public void setV(float[] V) {
        this.V = V;
    }

    /**
     * @return the A
     */
    public float[] getA() {
        return A;
    }

    /**
     * @param A the A to set
     */
    public void setA(float[] A)
    {
        this.A = A;
    }

    public Color getColor() 
    {
        return this.color;
    }
    public void setColor(Color c)
    {
        this.color=c;
    }
    
}
