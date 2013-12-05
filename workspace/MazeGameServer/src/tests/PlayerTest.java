package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import engine.Vector2i;
import engine.physics.RigidBody;
import game.entities.environment.Portal;
import game.entities.items.Bomb;
import game.entities.items.Item;
import game.entities.npcs.GateKeeper;
import game.entities.npcs.Hostage;
import game.entities.npcs.Player;
import game.enums.ItemType;
import game.environment.Interior;

/**
 * JUnit tests for the Player class
 */

public class PlayerTest {
    
    public String image;
    public int xPos;
    public int yPos;
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
        int currHealthPoints = player.getHealthPoints() - 5;
        player.setHealthPoints(currHealthPoints);
        int newHealthPoints = player.getHealthPoints();
        assertEquals(currHealthPoints, newHealthPoints);
    }
    
    @Test
    public void testPlayer() {
        initiateTestVariables();
                
        assertNotNull(this.player);
    }
    
    @Test
    public void testGetInventory() {
        initiateTestVariables();
        
        Bomb item = new Bomb(rb);
        item.enable();        
        item.pickUp(player);
        assertNotNull(player.getInventory().hasItem(ItemType.BOMB));
    }
    
    @Test
    public void testGetPlayerID() {
        initiateTestVariables();
        
        assertNotNull(this.player.getPlayerID());
    }
    
    @Test
    public void testGetLives() {
        initiateTestVariables();
        player.addLife();
        assertNotNull(this.player.getLives());
    }
    
    @Test
    public void testAddLife() {
        initiateTestVariables();
        int currLive = player.getLives() + 1;
        player.addLife();
        int newLive = player.getLives();
        assertEquals(currLive, newLive);
    }
    
    @Test
    public void testRemoveLife() {
        initiateTestVariables();        
        int currLive = player.getLives() - 1;
        player.removeLife();
        int newLive = player.getLives();
        assertEquals(currLive, newLive);
    }
    
    @Test
    public void testSetFollower() {
        initiateTestVariables();
        Hostage hostage = null;
        player.setFollower(hostage);
        assertNotNull(this.player.hasFollower());
    }
    
    @Test
    public void testGetFollower() {
        initiateTestVariables();
        Hostage hostage = null;
        player.setFollower(hostage);
        assertNotNull(this.player.hasFollower());
    }
    
    @Test
    public void testHasFollower() {
        initiateTestVariables();
        Hostage hostage = null;
        player.setFollower(hostage);
        assertNotNull(this.player.hasFollower());
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
