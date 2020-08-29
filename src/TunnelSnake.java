/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tunnelsnake;

import javax.swing.*;

public class TunnelSnake 
{


   public static void main(String[] args) throws InterruptedException
   {
      JFrame game = new JFrame ("Snake Game");
      game.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);

      game.getContentPane().add (new mainCanvas());
      

      game.pack();
      game.setVisible(true);
      
      //game.paint(null);
      
    }
}
