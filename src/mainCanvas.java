
package tunnelsnake;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


public class mainCanvas extends Canvas implements Runnable, KeyListener
{
    private static long serialversionuid = 1L;
    public static final int SCALE = 6;
    //public static final int WIDTH = 160*SCALE;
    //public static final int HIGHT = 160*SCALE;
    
    public static final String NAME = "Snake";
    
    
    private final int BOX_HEIGHT = 8*SCALE;
    private final int BOX_WIDTH = 8*SCALE;
    private final int GRID_WIDTH = 4*SCALE;
    private final int GRID_HEIGHT = 4*SCALE/12*9;
    
    
    private static final int NO_DIRECTION = 360;
    private static final int NORTH = 0;
    private static final int SOUTH = 180;
    private static final int WEST = 270;
    private static final int EAST = 90;
    
    
    private Thread runThread;
    //private Thread mouseThread;
    
    public int mouseTimer;
    
    private Graphics globalGraphics;    
    
    private LinkedList<SnakePart> snake;
    private Point leadSnakePoint = new Point(0,0);
    private LinkedList<SnakePart> snakeLives;
    private boolean musicSoundBool;
    private boolean snakeSoundBool;
    private boolean firstGame = true;
    int ranS;
    
    
    private Mouse food = new Mouse(new Point(0,0),NORTH);
    private int direction = 0;
    private int score = 0;
    private int refrechRate = 150;
    private SoundEngine sound = new SoundEngine();
    private int soundMoveIndex = 0;
    
   private String[] imageList = { 
                                  "SnakeBodyO2.png", "Mouse2.png"
                                };
    
    private BufferedImage[] iconList = new BufferedImage[imageList.length];
    
    public mainCanvas()
    {
        
        for(int i=0; i<iconList.length; i++)
        {
            try 
            {
                java.net.URL imgURL = getClass().getResource("images/"+imageList[i]);
                iconList[i] = ImageIO.read(imgURL);
            } 
            catch (Exception e) 
            {
            }  
        }
 
      setBackground (Color.black);
      setPreferredSize (new Dimension(GRID_WIDTH*BOX_WIDTH+200, GRID_HEIGHT*BOX_HEIGHT+50));
      setFocusable(true);
      this.addKeyListener(this);
            //BOX_HEIGHT * GRID_HEIGHT
            //GRID_WIDTH* BOX_WIDTH
      score = 0;
    }
    public void GenerateDefaultSnake()
    {
          if(musicSoundBool)
            //if(direction!=360)
            {
                Random rand = new Random();
                ranS = rand.nextInt(sound.musicListLegth());
                //sound.loopSong(ranS);
                snakeSoundBool=false;
            }
        
        
            musicSoundBool = true;
            snakeSoundBool = true;
            sound.stopAllSounds();
            sound.stopAllSongs();
            
            if(!firstGame)
            {             
                sound.playSound(2);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(mainCanvas.class.getName()).log(Level.SEVERE, null, ex);
                }   
            }
            
            refrechRate = 100;
            
            snake.clear();
            direction = NO_DIRECTION;
            
            snake.add(new SnakePart(new Point(10,10),1));
            snake.add(new SnakePart(new Point(10,11),1));
            snake.add(new SnakePart(new Point(10,12),1));        
            spawnFood();
            firstGame=false;          
    } 
    
    public void paint(Graphics g)
    {
            //this.setPreferredSize(new Dimension(WIDTH, HIGHT));
            // snakeLives = new LinkedList<>();
            if(snake==null)
            {
                snake = new LinkedList<>();
                GenerateDefaultSnake();       
            }   
            
            if (runThread == null)
            {
                    runThread = new Thread(this);
                    runThread.start();
            }
            
            drawGrid(g);
            drawFood(g);
            drawSnake(g);
            drawScore(g);
            drawVersion(g);
    }
    
    public void update(Graphics g)
    {
        Graphics offScreenGraphics;
        BufferedImage offScreen = null;
        Dimension d = this.getSize();
        
        offScreen = new BufferedImage(d.width,d.height,BufferedImage.TYPE_INT_ARGB);
        offScreenGraphics = offScreen.getGraphics();
        offScreenGraphics.setColor(this.getBackground());
        offScreenGraphics.fillRect(0, 0, d.width, d.height);
        offScreenGraphics.setColor(this.getForeground());
        paint(offScreenGraphics);
        
        //Flip
        g.drawImage(offScreen, 0, 0,this);
        
        
    }
    
    
    public boolean snakeCollision(Point point)
    {

        for(SnakePart p: snake)
        {
            if(p.getPoint().equals(point))
                return true;
        }
        return false;
    } 
    
    
    
    public void move()
    {
         //////////////////////////////////////////////// Snake Movment ////////////////////////////////////////////////      
        SnakePart headPoint = snake.peekFirst();
        SnakePart newNextPoint = headPoint;
        

        
		switch (direction) 
                {
		case NORTH:
			newNextPoint = new SnakePart(new Point(headPoint.getX(), headPoint.getY() - 1),direction);
                        leadSnakePoint = new Point(headPoint.getX(), headPoint.getY() - 2);
			break;
		case SOUTH:
			newNextPoint = new SnakePart(new Point(headPoint.getX(), headPoint.getY() + 1),direction);
                        leadSnakePoint = new Point(headPoint.getX(), headPoint.getY() + 2);
                        //System.out.println("South");
			break;
		case WEST:
			newNextPoint = new SnakePart(new Point(headPoint.getX() - 1, headPoint.getY()),direction);
                        leadSnakePoint = new Point(headPoint.getX() - 2, headPoint.getY());
			break;
		case EAST:
			newNextPoint = new SnakePart(new Point(headPoint.getX() + 1, headPoint.getY()),direction);
                        leadSnakePoint = new Point(headPoint.getX() + 2, headPoint.getY());
			break;
		}
                if(snakeSoundBool)
                    if(direction!=360)
                    {
                        sound.loopSong(ranS);
                        snakeSoundBool=false;
                    }
                
                
        snake.remove(snake.peekLast());
        

        
         //////////////////////////////////////////////// Snake Movment ////////////////////////////////////////////////  
        
        
         //////////////////////////////////////////////// Hit Detection ////////////////////////////////////////////////
        
                ////////////Check hitting food, add points and snake part////////////
                if(newNextPoint.getPoint().equals(food.getPoint()))
                {
                    sound.playSound(1);
                    refrechRate-=2;
                    score+=100;      
                    SnakePart lastPart = snake.peekLast();
                    SnakePart newLastPart = headPoint;
                    
                    switch (direction) 
                    {
                    case NORTH:
                            newLastPart = new SnakePart(new Point(headPoint.getX(), headPoint.getY() - 1),direction);
                            //System.out.println("North");
                            break;
                    case SOUTH:
                            newLastPart = new SnakePart(new Point(headPoint.getX(), headPoint.getY() + 1),direction);
                            //System.out.println("South");
                            break;
                    case WEST:
                            newLastPart = new SnakePart(new Point(headPoint.getX() - 1, headPoint.getY()),direction);
                            break;
                    case EAST:
                            newLastPart = new SnakePart(new Point(headPoint.getX() + 1, headPoint.getY()),direction);
                            break;
                    }
                    
                    snake.addLast(newLastPart);
                    spawnFood();
                }   
                ////////////Check out of bounds, reset game////////////
                else if (newNextPoint.getX() < 0 || newNextPoint.getX() > (GRID_WIDTH - 1))
		{
                        
			GenerateDefaultSnake();
			return;
		}
                else if (newNextPoint.getY() < 0 || newNextPoint.getY() > (GRID_HEIGHT - 1))
		{
			GenerateDefaultSnake();
			return;
		}
                ////////////Check crashing into our selves, reset game////////////
		else if (snakeCollision(newNextPoint.getPoint()))
		{	
			GenerateDefaultSnake();
			return;
		}
        
         //////////////////////////////////////////////// Hit Detection ////////////////////////////////////////////////  
        snake.push(newNextPoint);
    }
    
    public Point randomMouseMove(int num)
    {
        //int num = 6;
        Random rand = new Random();
        
        Point randPoint = (new Point(food.getX(),food.getY())), startPoint = randPoint;
        boolean bool = true;
        boolean wait = false;
        while(bool)
        {
            int ranD = rand.nextInt(num);
            switch (ranD) 
            {      
                //North
            case 0:
                    randPoint = new Point(food.getX(), food.getY() - 1);
                    food.setDirection(SOUTH);
                    bool = false;
                    //System.out.println("Mouse move North");
                    break;
                //South
            case 1:
                    randPoint = new Point(food.getX(), food.getY() + 1);
                    food.setDirection(NORTH);
                    bool = false;
                    //System.out.println("Mouse move South");
                    break;
                //East
            case 2:
                    randPoint = new Point(food.getX() - 1, food.getY());
                    food.setDirection(EAST);
                    bool = false;
                    //System.out.println("Mouse move East");
                    break;
                //West
            case 3:
                    randPoint = new Point(food.getX() + 1, food.getY());
                    food.setDirection(WEST);
                    bool = false;
                    //System.out.println("Mouse move West");
                    break;

            case 4:
            case 5:
                    num = 4;
                    wait = true;
                    break;
            } 
        }
        if(!wait)
            return randPoint;
        else
            return startPoint;

    }
    

    
    
    
    
    
    
    
    
    
    

    
    public void mouseMove()
    {
        Point randPoint = randomMouseMove(6);
        

        while(randPoint.getX() < 0 || randPoint.getX() > (GRID_WIDTH-1))
        {
                randPoint = randomMouseMove(6);
                //System.out.println("Mouse move away wall");
        }
        while(randPoint.getY() < 0 || randPoint.getY() > (GRID_HEIGHT-1))
        {
                randPoint = randomMouseMove(6);
                //System.out.println("Mouse move away wall");
        }

       while(randPoint.equals(leadSnakePoint))
       { 
            randPoint = randomMouseMove(4);
            System.out.println("Mouse move away");
       }
       
       if(food.getPoint().equals(leadSnakePoint))
       { 
            randPoint = randomMouseMove(4);
            System.out.println("Mouse move away now!");
       }
       
       while(snakeCollision(randPoint))
       {
            //System.out.println("Mouse move away");
            randPoint = randomMouseMove(4);
       }
       
       if(!food.getPoint().equals(randPoint))
           sound.playSound(0);        
       food.setPoint(randPoint);
       
       
       
    }
    
    
   
    
    public void drawGrid(Graphics g)
    {
        g.setColor(Color.BLUE);
        //drawing an outside rect
        g.drawRect(0, 0, GRID_WIDTH * BOX_WIDTH, GRID_HEIGHT * BOX_HEIGHT);
        //drawing the vertical lines
        for (int x = BOX_WIDTH; x < GRID_WIDTH * BOX_WIDTH; x+=BOX_WIDTH)
        {
            g.drawLine(x, 0, x, BOX_HEIGHT * GRID_HEIGHT);
        }
        //drawing the horizontal lines
        for (int y = BOX_HEIGHT; y < GRID_HEIGHT * BOX_HEIGHT; y+=BOX_HEIGHT)
        {
            g.drawLine(0, y, GRID_WIDTH * BOX_WIDTH, y);
        }
        
    }
    
    public void drawSnake(Graphics g)
    {   
        for(SnakePart p: snake)
                g.drawImage(rotateImage(p.getDirection(),iconList[0]), p.getX()* BOX_WIDTH, p.getY()* BOX_HEIGHT,BOX_WIDTH, BOX_HEIGHT, this); 
    } 
    
    public void drawScore(Graphics g)
    {
        g.setColor(Color.GREEN);
        g.fillRect(GRID_WIDTH * BOX_WIDTH,1, BOX_HEIGHT*4, BOX_HEIGHT);
        g.setColor(Color.BLACK);
            g.drawString("Score: "+score, GRID_WIDTH* BOX_WIDTH+50,  20);
            //GRID_HEIGHT*BOX_HEIGHT 
            //GRID_WIDTH* BOX_WIDTH
    }

        public void drawVersion(Graphics g)
    {
        g.setColor(Color.WHITE);
            g.drawString("Version: "+serialversionuid, 0, BOX_HEIGHT * GRID_HEIGHT + 10);
    }
    
    public void drawFood(Graphics g)
    {
        g.drawImage(rotateImage(food.getDirection(),iconList[1]), food.getX()* BOX_WIDTH, food.getY() * BOX_HEIGHT,BOX_WIDTH, BOX_HEIGHT, this);
    }
    
    	public void spawnFood()
	{
           Random rand = new Random(); 
           int ranX = rand.nextInt(GRID_WIDTH);
           int ranY = rand.nextInt(GRID_HEIGHT);
           Point randPoint = (new Point(ranX,ranY));
           while(snakeCollision(randPoint))
           {
                //System.out.println("spawning food");
                ranX = rand.nextInt(GRID_WIDTH);
                ranY = rand.nextInt(GRID_HEIGHT);
                randPoint = new Point(ranX, ranY);
           }
               
            food.setPoint(randPoint);
	}
    
    
    
    public BufferedImage rotateImage(double degrees, BufferedImage icon)
    {
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(degrees), icon.getWidth()/2, icon.getHeight()/2);
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        icon = op.filter(icon, null);
        return icon;
    }  

    
    


    @Override
      public void keyPressed (KeyEvent event)
      {
          
		switch (event.getKeyCode())
		{
		case KeyEvent.VK_UP:
			if (direction != SOUTH)
				direction = NORTH;
			break;
		case KeyEvent.VK_DOWN:
			if (direction != NORTH)
				direction = SOUTH;
			break;
		case KeyEvent.VK_RIGHT:
			if (direction != WEST)
				direction = EAST;
			break;
		case KeyEvent.VK_LEFT:
			if (direction != EAST)
				direction = WEST;
			break;
		}

         //repaint();
      }

  //--------------------------------------------------------------
      //  Provide empty definitions for unused event methods.
      //--------------------------------------------------------------
      public void keyTyped (KeyEvent event) {}
      public void keyReleased (KeyEvent event) {}

    private URL getCodeBase() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    
    
    
    
        @Override
    public void run() 
    {
        while(true)
        {
            //runs indefinitely
            if(direction != NO_DIRECTION)
                move();
            if(mouseTimer==2)
            {
                mouseMove();
                mouseTimer = 0;
            }
           repaint();
            

            try
            {
                    Thread.currentThread();
                    Thread.sleep(refrechRate);
            }
            catch (Exception e) 
            {
                    e.printStackTrace();
            }
            mouseTimer++;
	}   
   }
   }


