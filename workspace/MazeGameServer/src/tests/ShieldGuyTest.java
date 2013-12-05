package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import engine.Vector2i;
import engine.physics.RigidBody;
import game.entities.environment.Portal;
import game.entities.npcs.GateKeeper;
import game.entities.npcs.Player;
import game.entities.npcs.ShieldGuy;
import game.environment.Interior;
import game.GameEngine;

/**
 * JUnit tests for the ShieldGuy class.
 */

public class ShieldGuyTest {
    
    public String image;
    public int xPos;
    public int yPos;
    public ShieldGuy sgieldGuy;
    public Portal myPortal;
    public Interior myRoom;
    public ArrayList<Interior> rooms;
    public Player player;
    public RigidBody rb;
    
    public void main(String [] args) {
    }
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }
    
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }
       
    @Test
    public void testTakeDamage() {
        initiateTestVariables();
        
        sgieldGuy = null;
        sgieldGuy = new ShieldGuy(image, rb, myRoom);
        int currHealthPoints = sgieldGuy.getHealthPoints() + 10;
        sgieldGuy.setHealthPoints(currHealthPoints);
        int newHealthPoints = sgieldGuy.getHealthPoints();
        assertEquals(currHealthPoints, newHealthPoints);
    }
    
    @Test
    public void testShieldGuy() {
        initiateTestVariables();
        
        sgieldGuy = null;
        sgieldGuy = new ShieldGuy(image, rb, myRoom);
        assertNotNull(this.sgieldGuy);
    }
    
    public void initiateTestVariables() {
        try {            
          
            image = null;
            rb = new RigidBody(new Vector2i(0, 0), 24, 24);
            myPortal = new Portal(image, rb, myRoom, null);
            player = new Player(image, new RigidBody(new Vector2i(0, 0), 24, 24), 1, myRoom);
            
        } catch(Exception e) {
            System.out.println("Variable instantiation failed. Aborting JUnit tests.");
            e.printStackTrace();
            System.exit(1);
        }
    }
    
}
