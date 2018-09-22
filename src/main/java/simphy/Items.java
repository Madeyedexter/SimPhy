/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simphy;

import java.util.ArrayList;

/**
 *
 * @author Madeyedexter
 */
public class Items 
{
    private static final ArrayList<String> ids=new ArrayList<>(20);
    private static final ArrayList<Block> blocks=new ArrayList<>(20);
    private static final ArrayList<Circle> circles=new ArrayList<>(20);
    private static final ArrayList<Circle> removeQueue=new ArrayList<>(20);
    private static final ArrayList<Plane> planes=new ArrayList<>(20); 
    private static final ArrayList<Pendulum> pendulums=new ArrayList<>(20);

    static int numPlanes() {return planes.size();
    }

    static void addPendulum(Pendulum p)
    {
        pendulums.add(p);
    }
    static Plane getPlane(int i) {
        return planes.get(i);
    }
    static void addPlane(Plane p)
    {
        planes.add(p);
    }

    static int numPendulums() {
    return pendulums.size();
    }

    static Pendulum getPendulum(int i) {
        return pendulums.get(i);
    }

    static void removePendulum(Pendulum p) {
        pendulums.remove(p);
    
    }

    static void removePlane(Plane p) {planes.remove(p);}

    static ArrayList<Plane> getPlanes() {
        return planes;
    }

    static ArrayList<Pendulum> getPendulums() {
        return pendulums;
    }
    private Items()
    {
    }
    public static int numCircles()
    {
        return circles.size();
    }
    public static int numBlocks()
    {
        return blocks.size();
    }
    public static Circle getCircle(int i)
    {
        return circles.get(i);
    }
    public static Block getBlock(int i)
    {
        return blocks.get(i);
    }
    
    public static ArrayList<Block> getBlocks()
    {
        return blocks;
    }
    public static ArrayList<Circle> getCircles()
    {
        return circles;
    }
    public static ArrayList<String> getids()
    {
        return ids;
    }
    public static void addid(String id)
    {
        ids.add(id);
    }
    public static void removeid(String id)
    {
        ids.remove(id);
    }
    public static boolean exists_id(String id)
    {
        return ids.contains(id);
    }
    
    public static void addBlock(Block b )
    {
        blocks.add(b);
    }
    public static void addCircle(Circle c)
    {
        circles.add(c);
    }
    public static void removeCircle(int index)
    {
        ids.remove(ids.indexOf(circles.get(index).getID()));
        circles.remove(index);
        
    }
    public static void removeLater(Circle c)
    {
        removeQueue.add(c);
    }
    public static ArrayList<Circle> getRemoveQueue()
    {
        return removeQueue;
    }
    public static void removeBlock(int index)
    {
        blocks.remove(index);
    }
    public static void removeCircle(Circle c)
    {
        circles.remove(c);
    }
    
}
