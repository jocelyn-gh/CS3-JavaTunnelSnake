/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tunnelsnake;

import java.awt.Point;
//import javax.swing.ImageIcon;


public class SnakePart 
{
    private Point point;  
    private int direction = 360;
    
    
    public SnakePart(Point point, int direction)
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
}
