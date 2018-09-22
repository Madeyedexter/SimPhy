/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simphy;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
/**
 * @author Madeyedexter
 */
public final class DrawingPanel extends Canvas implements MouseListener, MouseMotionListener
{
    private static final long serialVersionUID = 1L;
    /*variable declaration follows*/
    //if Grid Lines are enabled
    private boolean gridEnabled=true;
    //width of the drawing area
    public static int WIDTH_DP;
    //height of the drawing area
    public static int HEIGHT_DP;
    //top left x coordinate of drawing area
    public static int BOXX;
    //top left y coordinate of drawing area
    public static int BOXY;
    /*
     * following variables are used for dragging and dropping
     * objects inside the drawing area
     */ 
    //x coordinate of mouse press event
    public int pressX=0;
    //y coordinate of mouse press event
    public int pressY=0;
    // to store the drag events' x coordinate 
    public int dragX=0;
    //to store drag events' y coordinate
    public int dragY=0;
    //to store mouse release events' x coordinate
    public int releaseX=0;
    //to store mouse release events' y coordinate
    public int releaseY=0;
    /*
     * stores the type of selected item.
     * the selected item can be circle, block,
     * inclined plane, spring, etc. This variable is used to 
     * draw a selection outline according to 
     * the type of item selected.
     */
    public static String selectedItem="NONE";
    /*
     * if the selected item is circle,
     * this Circle variable is used to store a reference to
     * the circle object that has to be dragged
     */
    private Circle draggedCircle=null;
    /*
     * if the selected item is a block,
     * this Block variable is used to store a reference to
     * the block object that has to be dragged
     */
    private Block draggedBlock;
    private Plane draggedPlane;
    /*
     * the master jframe object is a refernce to an instance of SimJFrame
     * it is used to convey events and information between frame variables
     * and this drawing panel. eg: frame.setLabelX() and frame.setLabelY(), etc.
     * calls are made to set x and y coordinate on the Jlabels at the bottom
     */
    private SimJFrame frame;
    // a boolean flag to check if the simulation is running or not
    public static volatile boolean RUNNING=false;
    //the scale of the drawing panel. The default value is:
    //100px=5m. Which is equal to a factor of 20;
    static int scale=120/3;
    private Rectangle dRec,boxRectangle;
    private GhostBall gball;
    private Graphics2D g2;
    private Arrow arrow;
    private final Object lock;
    private BufferStrategy strategy;
    private final BasicStroke dashedStroke,lineStroke;
    private int pivotPendulumX;
    private int pivotPendulumY;
    private boolean pendulumStarted=false;
    private boolean planeStarted;
    private int planeY;
    private int planeX;
    private int planeX1;
    private int planeY1;
    public XYChart chart;
    private Pendulum draggedPendulum;

    public static boolean drawChart=false;
    
    private void scanAndRemove() {
        //circles
        for(int i=0;i<Items.numCircles();i++)
        {
            Circle C=Items.getCircle(i);
            Vector2D c=C.getCenter();
            int x=(int) (c.getX()*scale),y= getHeight()-(int)(c.getY()*scale);
            if(dRec!=null && x>dRec.x && x<dRec.x+dRec.width && y>dRec.y && y< dRec.y+dRec.height)
                if(!RUNNING){
                    Items.removeCircle(C); Items.removeid(C.getID());}
                else
                    Items.removeLater(C);
        }
        //pendulums
        for(int i=0;i<Items.numPendulums();i++)
        {
            Pendulum p=Items.getPendulum(i);
            Vector2D pivot=p.getPivot(), center=p.getCenter();
            int px=(int)(pivot.getX()*scale), py=getHeight()-(int)(pivot.getY()*scale);
            int cx=(int)(center.getX()*scale), cy=getHeight()-(int)(center.getY()*scale);
            if(dRec!=null && ((cx>dRec.x && cx<dRec.x+dRec.width && cy>dRec.y && cy< dRec.y+dRec.height)||
                    (px>dRec.x && px<dRec.x+dRec.width && py>dRec.y && py< dRec.y+dRec.height)))
            {
                Items.removePendulum(p);
                if(!RUNNING)
                    draw();
            }
        }
        //planes
        for(int i=0;i<Items.numPlanes();i++)
        {
            Plane p=Items.getPlane(i);
            int x1=(int)(p.getV1().getX()*scale),x2=(int)(p.getV2().getX()*scale),x3=(int)(p.getV3().getX()*scale);
            int y1=(int)(HEIGHT_DP-p.getV1().getY()*scale), y2=(int)(HEIGHT_DP-p.getV2().getY()*scale), y3=(int)(HEIGHT_DP-p.getV3().getY()*scale);
            if(dRec!=null && ((x1>dRec.x && x1<dRec.x+dRec.width && y1>dRec.y && y1< dRec.y+dRec.height)||
                    (x2>dRec.x && x2<dRec.x+dRec.width && y2>dRec.y && y2< dRec.y+dRec.height)||(x2>dRec.x && x2<dRec.x+dRec.width && y2>dRec.y && y2< dRec.y+dRec.height)))    
            {
                Items.removePlane(p);
                if(!RUNNING)
                    draw();
            }
        }
    }

    private void createCircle() 
    {
        if(gball!=null){
        float x=((float)pressX)/scale,y=((float)(getHeight()-pressY))/scale;
        Vector2D center=new Vector2D(x,y);
        Circle c=new Circle("Circle"+Items.numCircles(),gball.radius,center,5.0f);
        Items.addCircle(c);    
        Items.addid(c.getID());}
    }

    private void createDeleteRectangle() 
    {
        //find top left
        int TOP,LEFT,W,H;
        if(pressX<dragX)
        {
            if(pressY<dragY)
            {
                TOP=pressY;LEFT=pressX;W=dragX-pressX;H=dragY-pressY;
            }
            else
            {
                TOP=dragY;LEFT=pressX;W=dragX-pressX;H=pressY-dragY;
            }
        }
        else
        {
            if(pressY<dragY)
            {
                TOP=pressY;LEFT=dragX;W=pressX-dragX;H=dragY-pressY;
            }
            else
            {
                TOP=dragY;LEFT=dragX;W=pressX-dragX;H=pressY-dragY;
            }
        }
        dRec=new Rectangle(LEFT,TOP,W,H);
    }

    private void dragSelectedItem() 
    {
        switch(selectedItem)
        {
            case "circle": Vector2D c=draggedCircle.getCenter();
                c.set(((float)dragX)/scale, ((float)(getHeight()-dragY))/scale);
                break;
            case "pendulum": Vector2D c1=draggedPendulum.getCenter();
                //c.set(BOXX, BOXX);
                break;
            case "plane" : draggedPlane.translate((float)(dragX-pressX)/scale, (float)(pressY-dragY)/scale);
            pressX=dragX; pressY=dragY;    
            break;
        }
    }

    private void createGhostBall() {
        gball=new GhostBall();                                               
        gball.W=2*Math.abs(pressX-dragX);
        gball.H=gball.W;
        gball.LEFT=pressX-gball.H/2;
        gball.TOP=pressY-gball.H/2;
        gball.x=dragX;
        gball.y=dragY;
        gball.radius=((float)(gball.W/2))/scale;
    }

    private void selectItem() 
    {
        for(int i=0;i<Items.numCircles();i++)
            {
                //check each circle if the mousePress event occured inside any of the Circles
                Circle c=Items.getCircle(i);
                Vector2D center = c.getCenter();
                int x=(int)(center.getX()*scale);
                int y=HEIGHT_DP-(int)(center.getY()*scale);
                int r=(int)(c.getRadius()*scale);
                if((Math.pow(pressX-x,2)+Math.pow(pressY-y,2)-Math.pow(r, 2))<0)
                {
                    selectedItem="circle";
                    draggedCircle=c;
                    return;
                }
                else{
                selectedItem="NONE";
                draggedCircle=null;
                }
            }
        //Pendulum select
        for(int i=0;i<Items.numPendulums();i++)
            {
                Pendulum p=Items.getPendulum(i);
                Vector2D center = p.getCenter();
                int x=(int)(center.getX()*scale);
                int y=HEIGHT_DP-(int)(center.getY()*scale);
                int r=(int)(p.getRadius()*scale);
                if((Math.pow(pressX-x,2)+Math.pow(pressY-y,2)-Math.pow(r, 2))<0)
                {
                    selectedItem="pendulum";
                    draggedPendulum=p;
                    return;
                }
                else{
                selectedItem="NONE";
                draggedPendulum=null;
                }
            }
        //Plane select
        for(int i=0;i<Items.numPlanes();i++)
            {
                Plane p=Items.getPlane(i);
                Vector2D v1=p.getV1(), v2=p.getV2(), v3=p.getV3();
                double x1=v1.getX(), x2= v2.getX(), x3=v3.getX();
                double y1=v1.getY(), y2=v2.getY(), y3=v3.getY();
                double pX=(double)pressX/scale, pY=(double)(HEIGHT_DP-pressY)/scale;
                
                double alpha = ((y2 - y3)*(pX - x3) + (x3 - x2)*(pY - y3))/((y2 - y3)*(x1 - x3) + (x3 - x2)*(y1 - y3));
                double beta = ((y3 - y1)*(pX - x3) + (x1 - x3)*(pY - y3))/((y2 - y3)*(x1 - x3)+(x3 - x2)*(y1 - y3));
                double gamma = 1.0 - alpha - beta;
                
                if(alpha>0 && beta>0 && gamma>0)
                {
                    selectedItem="plane";
                    draggedPlane=p;
                    return;
                }
                else
                {
                    selectedItem="NONE";
                    draggedPlane=null;
                }
            }
        }

    private void setVelocity() 
    {
            float v1= (float)(arrow.getX2()-arrow.getX1())/10;
            float v2= (float)(arrow.getY1()-arrow.getY2())/10;
            if(arrow!=null)
                switch(selectedItem)
                {
                    case "circle": draggedCircle.setV(draggedCircle.getV().add(new Vector2D(v1,v2)));
                                    break;
                }
            
    }

    private void createPendulum() {
        
            float x1=((float)pivotPendulumX)/DrawingPanel.scale;
            float y1=((float)(HEIGHT_DP-pivotPendulumY))/DrawingPanel.scale;
            float x2=((float)pressX)/DrawingPanel.scale;
            float y2=((float)(HEIGHT_DP-pressY))/DrawingPanel.scale;
            
            Pendulum p=new Pendulum(x1,y1,x2,y2);
            Items.addPendulum(p);
        }

    private void createPlane() 
    {
        float x1=((float)planeX)/scale;
        float y1=((float)(HEIGHT_DP-planeY))/scale;
        float x2=((float)pressX)/scale;
        float y2=((float)(HEIGHT_DP-pressY))/scale;
        float x3=((float)planeX1)/scale;
        float y3=((float)(HEIGHT_DP-planeY1))/scale;
        Plane p=new Plane(new Vector2D(x1,y1),new Vector2D(x2,y2),new Vector2D(x3,y3));
        Items.addPlane(p);
    
    }

    Pendulum getDraggedPendulum() {return draggedPendulum;}
    /*the constructor of the drawing panel. It adds to listeners to the panel:
     * MouseListener: mouseClicked, mousePressed, MouseReleased
     * MouseMotionListener: mouseEnter, mouseExitted, mouseMove, mouseDragged
     */
    public enum Mode {MODE_DELETE,MODE_CREATECIRCLE,MODE_CREATEPLANE,MODE_CREATEBLOCK,MODE_SELECT,MODE_IMPULSE,MODE_CREATEPENDULUM,
    MODE_GRAPH};
    public static Mode mode=Mode.MODE_SELECT;
    
    public DrawingPanel()
    {
        super();
        addMouseListener(this);
        addMouseMotionListener(this);
        //setDoubleBuffered(true);
        arrow=new Arrow(0,0,0,0);
        lock=new Object();
        setIgnoreRepaint(true);
        //init stroke
        dashedStroke=new BasicStroke(1.0f,
                        BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER,
                        5.0f, new float[]{4.0f}, 0.0f);
        lineStroke=new BasicStroke();
        gball=new GhostBall();
        //get width and height of DrawingPanel
        this.addComponentListener(new ComponentListener() {
        
            @Override
            public void componentResized(ComponentEvent e) 
            {
                if(!RUNNING)
                prepareGraphics();
            }

            @Override
            public void componentMoved(ComponentEvent e) 
            {
            }

            @Override
            public void componentShown(ComponentEvent e) 
            {
                if(!RUNNING)
                prepareGraphics();
            }

            @Override
            public void componentHidden(ComponentEvent e) {}
        });
    }


    @Override
    public void mouseClicked(MouseEvent e) 
    {
        pressX=e.getX();pressY=e.getY();
        switch(mode)
        {
            case MODE_DELETE: for(int i=0;i<Items.numCircles();i++)
            {
                //check each circle if the mousePress event occured inside any of the Circles
                Circle c=Items.getCircle(i);
                Vector2D center = c.getCenter();
                int x=(int)(center.getX()*scale);
                int y=HEIGHT_DP-(int)(center.getY()*scale);
                int r=(int)(c.getRadius()*scale);
                if((Math.pow(pressX-x,2)+Math.pow(pressY-y,2)-Math.pow(r, 2))<0)
                {
                    clearReferences();
                    if(!RUNNING){
                        Items.removeCircle(c); Items.removeid(c.getID()); draw();}
                    else
                        Items.removeLater(c);
                }                
            }
            //delete pendulum
            for(int i=0;i<Items.numPendulums();i++)
            {
                Pendulum p=Items.getPendulum(i);
                Vector2D center = p.getCenter();
                int x=(int)(center.getX()*scale);
                int y=HEIGHT_DP-(int)(center.getY()*scale);
                int r=(int)(p.getRadius()*scale);
                if((Math.pow(pressX-x,2)+Math.pow(pressY-y,2)-Math.pow(r, 2))<0)
                {
                    clearReferences();
                    Items.removePendulum(p);
                    if(!RUNNING)
                         draw();
                        
                }
            }
            
            for(int i=0; i<Items.numPlanes();i++)
            {
                Plane p=Items.getPlane(i);
                Vector2D v1=p.getV1(), v2=p.getV2(), v3=p.getV3();
                double x1=v1.getX(), x2= v2.getX(), x3=v3.getX();
                double y1=v1.getY(), y2=v2.getY(), y3=v3.getY();
                double pX=(double)pressX/scale, pY=(double)(HEIGHT_DP-pressY)/scale;
                
                double alpha = ((y2 - y3)*(pX - x3) + (x3 - x2)*(pY - y3))/((y2 - y3)*(x1 - x3) + (x3 - x2)*(y1 - y3));
                double beta = ((y3 - y1)*(pX - x3) + (x1 - x3)*(pY - y3))/((y2 - y3)*(x1 - x3)+(x3 - x2)*(y1 - y3));
                double gamma = 1.0 - alpha - beta;
                
                    System.out.println(alpha+" "+beta+" "+gamma);
                if(alpha>0 && beta>0 && gamma>0)
                {
                    clearReferences();
                    Items.removePlane(p);
                    if(!RUNNING)
                        draw();
                }
            }
            break;
            case MODE_CREATEPENDULUM: 
                if(!pendulumStarted)
                {
                    pendulumStarted=true;
                     pivotPendulumX=pressX;
                     pivotPendulumY=pressY;
                }
            else
            {
                createPendulum();
                pendulumStarted=false;
            }
            if(!RUNNING)
                draw();
            break;
            case MODE_CREATEPLANE: 
                if(!planeStarted)
                {
                    planeStarted=true;
                    planeX=pressX;
                    planeY=pressY;
                }
                else
                {
                    createPlane();
                    planeStarted=false;
                }
                if(!RUNNING)
                    draw();
                break;
            case MODE_GRAPH: 
                selectItem();
                if(!RUNNING) draw();
                if(!drawChart)
                {
                    drawChart=true;
                    switch(selectedItem)
                    {
                        case "circle": chart=new XYChart(draggedCircle);
                                        break;
                        case "pendulum": chart=new XYChart(draggedPendulum); 
                                        break;
                    }
                }

            default: break;
            
        }
    }

    /*
     * mousePressed callback function. Handles selecting of item when simulation
     * is not running and mouse is pressed
     */
    @Override
    public void mousePressed(MouseEvent e) 
    {
        //get X and Y
        pressX=e.getX();
        pressY=e.getY();
        switch(mode)
        {
            case MODE_DELETE: 
                              break;
            case MODE_CREATECIRCLE: 
                              break;
            case MODE_IMPULSE: selectItem();
                               if(!RUNNING) draw(); break;
            case MODE_SELECT: selectItem();
                                if(!RUNNING) draw();
                                break;
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent e) 
    {
        switch(mode)
        {
            case MODE_DELETE: releaseX=e.getX();
                              releaseY=e.getY();
                              scanAndRemove();
                              clearReferences();
                              dRec=null;
                              if(!RUNNING)
                                  draw();
                              break;
            case MODE_CREATECIRCLE: releaseX=e.getX();
                                    releaseY=e.getY();
                                    createCircle();
                                    gball=null;
                                    if(!RUNNING)
                                        draw();
                                    break;
            case MODE_SELECT: break;
            case MODE_IMPULSE: setVelocity();
                                arrow=null;
                                if(!RUNNING)
                                    draw();
                                break;
        }
    }
    /*
     * Fired when mouse is dragged,i.e., moved while being pressed.
     * this method handles dragging of the selected item based on its category.
     * e.g.: if item is circle, the draggedCircle object will hold the reference
     * to the circle to be dragged. the location of Center of Mass of the selected
     * object is changed when the mouse is dragged.
     */
    @Override
    public void mouseDragged(MouseEvent e) 
    {
        
        frame.setlabelX((float)e.getX()/(float)scale);
        frame.setlabelY((float)(HEIGHT_DP-e.getY())/(float)scale);
        dragX=e.getX();
        dragY=e.getY();
        switch(mode)
        {
            case MODE_DELETE: createDeleteRectangle();
                              if(!RUNNING)
                                  draw();
                              break;
            case MODE_SELECT: if(selectedItem!=null)
            {
                dragSelectedItem();
                if(!RUNNING)
                    draw();
            }
            break;
            case MODE_CREATECIRCLE: createGhostBall();
                                    if(!RUNNING)
                                        draw();
                                    break;
            case MODE_IMPULSE: prepareVelocityArrow(e.getX(),e.getY());
                                if(!RUNNING)
                                    draw();
                                break;
        }
    }
    
    @Override
    public void mouseEntered(MouseEvent e) 
    {
        setCursor();
    }
    /*
     * Callback method mouseExitted, fires when mouse exits the drawing panel.
     * on this event set JLabels at the bottom to empty values.
     */
    @Override
    public void mouseExited(MouseEvent e) 
    {
        frame.setlabelX(-1.0f);
        frame.setlabelY(-1.0f);
    }
    
    /*
     * on mouseMove event, update the values of the jlabels at the bottom to show
     * current X,Y coordinate in terms of meter
     */
    @Override
    public void mouseMoved(MouseEvent e) 
    {
        frame.setlabelX((float)e.getX()/(float)scale);
        frame.setlabelY((float)(HEIGHT_DP-e.getY())/(float)scale);
        switch(mode)
        {
            case MODE_CREATEPENDULUM: 
                if(pendulumStarted)
                {
                    pressX=e.getX();
                    pressY=e.getY();
                    if(!RUNNING)
                        draw();
                }
                break;
            case MODE_CREATEPLANE:
                if(planeStarted)
                {
                    pressX=e.getX();
                    pressY=e.getY();
                    if(!RUNNING)
                        draw();
                }
                break;
                
        }
    }
    //used to initialize the frame variable
    public void setFrame(SimJFrame frame)
    {
        this.frame=frame;
    }
    //called from the DrawingPanel.paintComponent() method to draw an outline on 
    //the selected object.
    private void drawOutline() 
    {
        g2.setColor(Color.BLACK);
        switch(selectedItem)
        {
            case "circle": 
                int[] c={ (int)(draggedCircle.getCenter().getX()*scale),HEIGHT_DP-(int)(draggedCircle.getCenter().getY()*scale)};
                int r=(int)(draggedCircle.getRadius()*scale);
                g2.drawArc(c[0]-r, c[1]-r,2*r,2*r,0,360);
                break;
            case "block":
                break;
                
            case "plane":Vector2D v1=draggedPlane.getV1(),v2= draggedPlane.getV2(),v3= draggedPlane.getV3();
                int[] x=new int[]{(int)(v1.getX()*scale),(int)(v2.getX()*scale),(int)(v3.getX()*scale)};
                int[] y=new int[]{HEIGHT_DP-(int)(v1.getY()*scale),HEIGHT_DP-(int)(v2.getY()*scale),HEIGHT_DP-(int)(v3.getY()*scale)};
                
                g2.drawString(String.format("%.2f,%.2f",v1.getX(),v1.getY()), x[0]+2, y[0]);
                g2.drawString(String.format("%.2f,%.2f",v2.getX(),v2.getY()), x[1]+2, y[1]);
                g2.drawString(String.format("%.2f,%.2f",v3.getX(),v3.getY()), x[2]+2, y[2]);
                g2.drawPolygon(x, y, 3);
                break;
            case "pendulum": 
                int[] c1={ (int)(draggedPendulum.getCenter().getX()*scale),HEIGHT_DP-(int)(draggedPendulum.getCenter().getY()*scale)};
                int pX=(int)(draggedPendulum.getPivot().getX()*scale);
                int pY=HEIGHT_DP-(int)(draggedPendulum.getPivot().getY()*scale);
                int r1=(int)(draggedPendulum.getRadius()*scale);
                g2.setColor(Color.RED);
                g2.drawString(String.format("ω=%.2f rad/s",draggedPendulum.getOmega()),(c1[0]+pX)/2 ,(c1[1]+pY)/2);
                g2.drawString(String.format("Θ=%.2f degree",Math.toDegrees(draggedPendulum.getTheta())),(c1[0]+pX)/2 ,(c1[1]+pY)/2+14);
                g2.drawString(String.format("L=%.2f meter", draggedPendulum.getLength()), (c1[0]+pX)/2, (c1[1]+pY)/2+28);
                g2.setColor(Color.BLACK);
                g2.drawArc(c1[0]-r1, c1[1]-r1,2*r1,2*r1,0,360);
                break;
            default:
                break;
        }
    }
    //called from the SimJFrame class to set the value selected by user using the jslider
    //embedded in the toolbar of SimJFrame
    void setScale(int value) 
    {    
        scale=120/value;
        if(!RUNNING)
        prepareGraphics();
    }

    /**
     * @return the isgridEnabled
     */
    public boolean isGridEnabled() {
        return gridEnabled;
    }

    /**
     * @param gridEnabled
     */
    public void setGridEnabled(boolean gridEnabled) 
    {
        this.gridEnabled = gridEnabled;
    }
//this method draws the grid on the drawing panel for every 20px
    private void drawGrid() 
    {
        g2.setColor(Color.LIGHT_GRAY);
        for(int i=0;i<getWidth();i += 24)
        {
            g2.drawLine(i,0,i,getHeight());
        }
        for(int i=getHeight();i>=0;i -= 24)
        {
            g2.drawLine(0, i,getWidth(), i);
        }
        g2.setColor(Color.BLACK);
    }

    private void setCursor()
    {
        Toolkit kit=Toolkit.getDefaultToolkit();
        switch(mode)
        {
            case MODE_DELETE: this.setCursor(kit.createCustomCursor(kit.getImage(getClass().getResource("remcursor.png")),new Point(0,0),"Delete"));
                            break;
            default: this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                     break;
        }
        
    }

    /**
     * @return the draggedCircle
     */
    public Circle getDraggedCircle() 
    {
        return draggedCircle;
    }

    /**
     * @param draggedCircle the draggedCircle to set
     */
    public void setDraggedCircle(Circle draggedCircle) {
        this.draggedCircle = draggedCircle;
    }

    void clearReferences() 
    {
        this.draggedCircle=null;
        this.draggedBlock=null;
        this.draggedPendulum=null;
        this.draggedPlane=null;
        DrawingPanel.selectedItem="NONE";
        System.gc();
    }

    public void prepareGraphics() 
    {
        
        WIDTH_DP=getWidth();
        HEIGHT_DP=getHeight();
		// Create BufferStrategy for rendering/drawing
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		Graphics g = strategy.getDrawGraphics();
		this.g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if(!RUNNING)
            draw();
    }
    
    public void draw() 
    {
        if(strategy==null || strategy.contentsLost())
        {
            // Create BufferStrategy for rendering/drawing
            createBufferStrategy(2);
            strategy = getBufferStrategy();
            Graphics g = strategy.getDrawGraphics();
            this.g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }  
        //clear background
        g2.setColor(Color.WHITE);
        g2.fillRect(0,0, getWidth(),getHeight());
        //draw grid if user has enabled the flag. Grid lines are enabled by default
        if(gridEnabled)
            /*
             * Call to this function draws the grid lines.
             */
            drawGrid();
        //draw the Scale
        g2.drawString(120/scale+" m", 144/2-5,12);
        g2.drawLine(24,17, 144, 17);
        g2.drawLine(24, 16, 144, 16);
        //draw circles
        for(int i=0;i<Items.numCircles();i++)
        {
            Items.getCircle(i).drawCircle(g2);
        }
        //draw inclined plane
        for(int i=0;i<Items.numPlanes();i++)
        {
            Items.getPlane(i).drawPlane(g2);
        }
        //draw pendulums
        for(int i=0;i<Items.numPendulums();i++)
        {
            Items.getPendulum(i).drawPendulum(g2);
        }
        switch(mode)
        {
            case MODE_IMPULSE://draw velocity arrow
                            Arrow tempArrow = arrow;
                            if(tempArrow!=null)
                            {
                                tempArrow.draw(g2);
                                g2.drawString(String.format("%.2f m/s", tempArrow.getLength()/10, 2), (tempArrow.getX2() + tempArrow.getX1())/2, (tempArrow.getY1() + tempArrow.getY2())/2);
                            }
                            break;
            case MODE_DELETE:// draw dRec
                            if(dRec!=null)
                            {
                                g2.setStroke(dashedStroke);
                                g2.drawRect(dRec.x,dRec.y,dRec.width,dRec.height);
                                g2.setStroke(lineStroke);
                            }
                            break;
            case MODE_CREATECIRCLE: //draw Ghost Ball
                            if(gball!=null)
                            {
                                gball.drawGhostBall(g2);
                            }
                            break;
            case MODE_CREATEPENDULUM: 
             if(pendulumStarted)
            {
                g2.setColor(Color.black);
                g2.fillOval(pivotPendulumX-2, pivotPendulumY-2, 4, 4);
                g2.drawLine(pivotPendulumX, pivotPendulumY, pressX, pressY);
                g2.drawString(String.format("Length = %.2f m",Math.sqrt(Math.pow(pressY-pivotPendulumY,2)+Math.pow(pressX-pivotPendulumX,2))/scale),pressX-8,pressY-8);
                g2.drawString(String.format("Angle = %.2f degree",Math.toDegrees(Math.atan((double)(pressY-pivotPendulumY)/(double)(pressX-pivotPendulumX)))),pressX-22,pressY-22);
            
            }
                            break;
            case MODE_CREATEPLANE:
                if(planeStarted)
                {
                    if(planeX<pressX)
                    {
                        if(planeY<pressY)
                        {
                            g2.drawPolygon(new int[]{planeX,pressX,planeX},new int[]{planeY,pressY,pressY},3);
                            planeX1=planeX; planeY1=pressY;                            
                        }
                        else
                        {
                            g2.drawPolygon(new int[]{planeX,pressX,pressX},new int[]{planeY,pressY,planeY},3);
                            planeX1=pressX; planeY1=planeY;
                        }
                    }
                    else
                    {
                        if(planeY<pressY)
                        {
                            g2.drawPolygon(new int[]{planeX,pressX,planeX},new int[]{planeY,pressY,pressY},3);
                            planeX1=planeX; planeY1=pressY;                            
                        }
                        else
                        {
                            g2.drawPolygon(new int[]{planeX,pressX,pressX},new int[]{planeY,pressY,planeY},3);
                            planeX1=pressX; planeY1=planeY;
                        }
                    }                    
                }
            break;
            default: break;
        }
        //draw outline & label on selected object
        drawOutline();
        if (!strategy.contentsLost() && strategy!=null) 
            strategy.show();
    }

    private void prepareVelocityArrow(int x, int y) 
    {
        switch(selectedItem)
        {
            case "circle":  Vector2D v2=draggedCircle.getCenter();
                            int x1=(int)(v2.getX()*scale);
                            int y1=HEIGHT_DP-(int)(v2.getY()*scale);
                            arrow=new Arrow(x1,y1,x1-x+x1,y1-y+y1);
                            break;
        }
    }
}
