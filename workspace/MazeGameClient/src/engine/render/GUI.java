package engine.render;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import game.Game;

public class GUI {
    // Variables
    // Map of items
    // health
    // lives
    
    // all sprites necessary for rendering GUI
    // list of sprites for all items
    // render a black box (not sprite/image) on top portion... you do this via open gl calls..
    // render health bar (not sprite/image)
    
    
    
    
    // render order matters:
    // black box
    // all stuff on top
    
    //could render on top of the camera, openGL how to have overlayer interface
    
    private GUI() {
        
     /*   GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 800, 0, 600, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        
        //while (!Display.isCloseRequested()) {
            // Clear the screen and depth buffer
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);  
     
            // set the color black
            GL11.glColor3f(0,0,0);*/
     
            
     
      
}
    
    private static void drawInventory (float startx, float starty, float width,
            float height) {
        //Display.reset();
       /* GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);  
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 800, 0, 600, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);*/
       
        GL11.glColor3f(1f,0f,0f);
        GL11.glBegin(GL11.GL_QUADS);
           
            GL11.glVertex2f(startx, starty);
            GL11.glVertex2f(startx + width, starty);
            GL11.glVertex2f(startx + width, starty + height);
            GL11.glVertex2f(startx, starty + height);
        GL11.glEnd();
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);  
        

    }
    
    public static void init() {
        // init all sprites and stuff here
        // new sprites = Game.getDisplay().getSprite(/*image path*/);
    }
    
    public static void populate(/*player specific arguments here*/) {
        // items, health, lives
        // populate variables with data (mostly quantity)
        // quantities will be rendered via openGL (not images) // look this up
    }
    
    public static void draw() {
        // will call draw function for each sprite
        //sprite.draw(position x, position y);
        drawInventory(0, 0, 100, 100);
    }
    
    
    
    
    
}
