package simphy;

import java.io.Serializable;


public final class Vector2D implements Serializable
{

	private double x;
	private double y;

	public Vector2D()
	{
		this.setX(0);
		this.setY(0);
	}

	public Vector2D(double x, double y)
	{
		this.setX(x);
		this.setY(y);
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getX() {
		return x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getY() {
		return y;
	}

	public void set(double x, double y)
	{
		this.setX(x);
		this.setY(y);
	}


	public double dot(Vector2D v2)
	{
		double result = this.getX() * v2.getX() + this.getY() * v2.getY();
		return result;
	}

	public double getLength()
	{
		return (double)Math.sqrt(getX()*getX() + getY()*getY());
	}

	public double getDistance(Vector2D v2)
	{
		return Math.sqrt((v2.getX() - getX()) * (v2.getX() - getX()) + (v2.getY() - getY()) * (v2.getY() - getY()));
	}


	public Vector2D add(Vector2D v2)
	{
		Vector2D result = new Vector2D();
		result.setX(getX() + v2.getX());
		result.setY(getY() + v2.getY());
		return result;
	}

	public Vector2D subtract(Vector2D v2)
	{
		Vector2D result = new Vector2D();
		result.setX(this.getX() - v2.getX());
		result.setY(this.getY() - v2.getY());
		return result;
	}

	public Vector2D multiply(double scaleFactor)
	{
		Vector2D result = new Vector2D();
		result.setX(this.getX() * scaleFactor);
		result.setY(this.getY() * scaleFactor);
		return result;
	}
        
        public Vector2D rotate(double angle)
        {
            Vector2D v=new Vector2D();
            v.setX((x*Math.cos(angle)+y*Math.sin(angle)));
            v.setY((-x*Math.sin(angle)+y*Math.cos(angle)));
            return v;
        }

	public Vector2D normalize()
	{
		double len = getLength();
		if (len != 0.0f)
		{
			this.setX(this.getX() / len);
			this.setY(this.getY() / len);
		}
		else
		{
			this.setX(0.0f);
			this.setY(0.0f);
		}

		return this;
	}
        
    /**
     *
     * @param d1
     * @param d2
     */
    public void add(double d1, double d2)
        {
            x+=d1; y+=d2;
        }

        public Vector2D crossOmega(double omega)
        {
             double x1=getY()*omega;
             double y1=-getX()*omega;
             return new Vector2D(x1,y1);
        }
        
        @Override
	public String toString()
	{
		return (getX() + "," + getY());
	}


}
