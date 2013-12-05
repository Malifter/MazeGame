package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;

import engine.Vector2i;
import game.Inventory;
import game.entities.npcs.Player;
import game.enums.ItemType;
import game.environment.Interior;
import game.environment.Room;

import org.junit.Test;

/**
 * JUnit tests for the Inventory class. These tests will not include anything from the superclass Entity.
 * InventoryTest: <add description>
 */

public class InventoryTest {
    
   
    public String image;
    public int xPos;
    public int yPos;
    public Inventory inventory;
    public Player player;
    public Interior room;
    public HashMap<ItemType,Integer> items = new HashMap<ItemType,Integer>();
    
    public void main(String [] args) {
    }
    
    @Test
    public void testInventory() {
        initiateTestVariables();        
        inventory = null;
        player = null;
        inventory = new Inventory();
        assertNotNull(this.inventory);      
    }
    
    @Test
    public void testAddItem() {
        initiateTestVariables();        
        inventory = null;
        player = null;
        inventory = new Inventory();
        assertNotNull(this.inventory);
        ItemType item = ItemType.BOMB;
        this.inventory.addItem(item);
        assertEquals(inventory.hasItem(item),true);
    }
    
    @Test
    public void testRemoveItem() {
        initiateTestVariables();        
        inventory = null;
        player = null;
        inventory = new Inventory();
        assertNotNull(this.inventory);
        ItemType item = ItemType.BOMB;
        this.inventory.addItem(item);
        this.inventory.removeItem(item);
        assertEquals(inventory.hasItem(item),false);
    }
    
    @Test
    public void testGetItem() {
        initiateTestVariables();        
        inventory = null;
        player = null;
        inventory = new Inventory();
        assertNotNull(this.inventory); 
        ItemType item = ItemType.BOMB;
        this.inventory.addItem(item);
        assertNotNull(this.inventory.getItem()); 
    }
    
    public void testUseSelectedItem(Player player, Room room){
        initiateTestVariables();        
        inventory = null;
        player = null;
        inventory = new Inventory();
        assertNotNull(this.inventory);
        inventory.useSelectedItem(player, room);
    }
    
    public void initiateTestVariables() {
        try {
            Interior room = new Interior(new Vector2i(0,0), 5);
            image = null;
            xPos = 232;
            yPos = 72;
        } catch(Exception e) {
            System.out.println("Variable instantiation failed. Aborting JUnit tests.");
            e.printStackTrace();
            System.exit(1);
        }
    }

}
