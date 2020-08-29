
package tunnelsnake;

import java.awt.Point;


public class Mouse
{
    private Point point;  
    private int direction;
    
    public Mouse(Point point, int direction)
    {
        this.point = point;
        this.direction = direction;      
    } 
 
    public int getX()
    {
        return point.x;
    }
    
    public int getY()
    {
        return point.y;
    }
    
    public Point getPoint()
    {
        return point;
    }
    
    public int getDirection()
    {
        return direction;
    }
    
    public void setDirection(int direction)
    {
        this.direction = direction;
    }
    
    public void setPoint(Point point)
    {
        this.point = point;
    }
    
    
    
    
    
    
    
}
