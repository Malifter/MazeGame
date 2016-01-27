package engine.render;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.util.ResourceLoader;

import engine.serializable.SerializedPlayer;
import game.Game;
import game.enums.ItemType;

public class GUI {
    private static final Sprite background = Game.getDisplay().getSprite("UI/background.jpg");
    private static final Sprite avatar = Game.getDisplay().getSprite("animations/npcs/player/idle/down/0.gif");
    private final static int WIDTH = 1024;
    private final static int HEIGHT = 154;
    private static TrueTypeFont font = initFont();
    private static Font awtFont;
    private static final int background_X_OFFSET = background.getWidth()/2;
    private static final int background_Y_OFFSET = background.getHeight()/2;
    private static List<Integer> items; // map of items
    private static int selectedItem = 0;
    private static int health = -1;
    private static int lives = -1;
    static boolean once = false;
    
    private GUI() {};
    
    private static TrueTypeFont initFont() {
        // load font from file
        try {
            InputStream inputStream = ResourceLoader.getResourceAsStream("/assets/UI/upheavtt.ttf");
 
            awtFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            awtFont = awtFont.deriveFont(24f); // set font size
            return new TrueTypeFont(awtFont, true);
 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private static void drawInventory(float startx, float starty, float width,
            float height) {
        //glPushMatrix();
        //GL11.glTranslatef(1, 0, 0);
        glColor3f(0f,0f,0f);
        glBegin(GL_QUADS);
            glVertex2f(startx, starty);
            glVertex2f(startx + width, starty);
            glVertex2f(startx + width, starty + height);
            glVertex2f(startx, starty + height);
        glEnd();
        //glPopMatrix();
    }
    
    public static void populate(SerializedPlayer sp) {
        health = sp.getHealth();
        lives = sp.getLives();
        selectedItem = sp.getSelectedItem();
        items = sp.getItems();
    }
    
    private static void drawLives() {
        // draw avatar
        glPushMatrix();
        glColor3f(1f,1f,1f);
        glScalef(3, 3, 0);
        avatar.draw(25, 35);

        // draw number of lives (e.g., x2)
        //Color.white.bind();
        //TODO: Cannot use slick util for drawing text without using the slick util Texture loader / texture files etc
        // Will need to download slick util (latest) and use that with the LWJGL 2.9.2.
        // Another option is to use LWJGL 3 and not use slick util. This would require some rewriting of code in order
        // to render/support using the new system.
        // could also check out JBox2D to completely replace the physics I implemented with real physics.
        TextureImpl.unbind(); // SOLVES THE PROBLEM
        font.drawString(47, 25, Integer.toString(lives), Color.white);
        glScalef(0.5f, 0.5f, 0);
        font.drawString(75, 69, "x", Color.white);
        glPopMatrix();
    }
    
    private static void drawHealthBar() {
        // Draw health
        // background
        glColor4f(0f,0f,0f,0.8f);
        glBegin(GL_QUADS);
            glVertex2f(40, 20);
            glVertex2f(244, 20);
            glVertex2f(244, 60);
            glVertex2f(40, 60);
        glEnd();
        
        // border
        glColor4f(0.2f,0.2f,0.2f,0.8f);
        glLineWidth(4);
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        glBegin(GL_QUADS);
            glVertex2f(40, 20);
            glVertex2f(244, 20);
            glVertex2f(244, 60);
            glVertex2f(40, 60);
        glEnd();
        
        // health
        glColor3f(1f,0.0f,0.0f);
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        glBegin(GL_QUADS);
            glVertex2f(42, 22);
            glVertex2f((health)+42, 22);
            glVertex2f((health)+42, 58);
            glVertex2f(42, 58);
        glEnd();
        
        // draw containers
        glColor4f(0.2f,0.2f,0.2f,0.8f);
        glLineWidth(4);
        for(int i = 1; i < 5; i++) {
            glBegin(GL_LINES);
                glVertex2f(40 + (41*i), 20);
                glVertex2f(40 + (41*i), 60);
            glEnd();   
        }
    }
    
    public static void draw() {
        // Draw background
        glColor3f(1f,0.8f,0.4f);
        background.draw(background_X_OFFSET, background_Y_OFFSET);
        
        glDisable(GL_TEXTURE_2D);
        
        // Draw border
        glColor3f(0.3f,0.1f,0.0f);
        glLineWidth(10);
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        glBegin(GL_QUADS);
            glVertex2f(3, 4);
            glVertex2f(WIDTH-2, 4);
            glVertex2f(WIDTH-2, HEIGHT-3);
            glVertex2f(3, HEIGHT-3);
        glEnd();
        
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        
        // Draw slightly opaque overlay
        glColor4f(0.9f,0.7f,0.3f,0.8f);
        glBegin(GL_QUADS);
            glVertex2f(0, 0);
            glVertex2f(WIDTH, 0);
            glVertex2f(WIDTH, HEIGHT);
            glVertex2f(0, HEIGHT);
        glEnd();
        
        // Draw healthbar
        drawHealthBar();
        
        glEnable(GL_TEXTURE_2D);
        // Draw lives
        drawLives();
        
        // draw sprites on top of it
        //drawInventory(0, 0, 1024, 154);
        
        glColor3f(1f,1f,1f);
        //glEnable(GL_TEXTURE_2D);
    }
}
