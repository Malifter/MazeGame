package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import game.Game;
import game.GameEngine;
import game.Inventory;
import game.MazeGameServer;
import game.entities.Entity;
import game.entities.environment.Obstacle;
import game.entities.items.NotConsumable;
import game.entities.npcs.Player;

import org.junit.Test;

/**
 * JUnit tests for the Inventory class. These tests will not include anything from the superclass Entity.
 * InventoryTest: <add description>
 */

public class InventoryTest {
    
    public Game game; // just a temporary game object to perform our tests on
    public String image;
    public int xPos;
    public int yPos;
    public Inventory inventory;
    public Player player;
    
    public void main(String [] args) {
    }
    
    @Test
    public void testInventory() {
        initiateTestVariables();        
        inventory = null;
        player = null;
        inventory = new Inventory(player);
        assertNotNull(this.inventory);      
    }
    
    @Test
    public void testAddItem() {
        initiateTestVariables();        
        inventory = null;
        player = null;
        inventory = new Inventory(player);
        assertNotNull(this.inventory);
        NotConsumable item = null;
        this.inventory.addItem(item);
    }
    
    @Test
    public void testRemoveItem() {
        initiateTestVariables();        
        inventory = null;
        player = null;
        inventory = new Inventory(player);
        assertNotNull(this.inventory);
        NotConsumable item = null;
        this.inventory.removeItem(item);
    }
    
    @Test
    public void testGetItem() {
        initiateTestVariables();        
        inventory = null;
        player = null;
        inventory = new Inventory(player);
        assertNotNull(this.inventory); 
        NotConsumable item = null;
        this.inventory.addItem(item);
        assertNotNull(this.inventory.getItem()); 
    }
    
    @Test
    public void testGetPlayer() {
        initiateTestVariables();        
        inventory = null;
        player = new Player(game, image, xPos, yPos, xPos, yPos, xPos, yPos, xPos, yPos);
        inventory = new Inventory(player);
        assertNotNull(this.inventory);     
        assertNotNull(this.inventory.getPlayer());
    }
    public void initiateTestVariables() {
        try {
            game = new MazeGameServer(new GameEngine());
            image = "chestLockedImage";
            xPos = 232;
            yPos = 72;
        } catch(Exception e) {
            System.out.println("Variable instantiation failed. Aborting JUnit tests.");
            e.printStackTrace();
            System.exit(1);
        }
    }
    
}
