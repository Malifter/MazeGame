package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import engine.Vector2i;
import engine.physics.RigidBody;

import game.entities.environment.Portal;
import game.entities.npcs.GateKeeper;
import game.entities.npcs.Player;
import game.enums.ItemType;
import game.environment.Interior;

import org.junit.Test;

/**
 * JUnit tests for the GateKeeper class, which corresponds to negotiator use case.
 * These tests will not include anything from the superclass Entity.
 * GateKeeperTest: <add description>
 */

public class GateKeeperTest {
    
    public String image;
    public int xPos;
    public int yPos;
    public GateKeeper gateKeeper;
    public Portal myPortal;
    public Interior myRoom;
    public ArrayList<Interior> rooms;
    public Player player;
    public RigidBody rb;
    
    public void main(String [] args) {
    }
    
    @Test
    public void testGateKeeperConstructor() {
        initiateTestVariables();
        
        gateKeeper = null;
        gateKeeper = new GateKeeper(image, rb, myPortal);
        assertNotNull(this.gateKeeper);
    }
    
    @Test
    public void testNegotiate() {
        initiateTestVariables();
        
        gateKeeper = null;
        gateKeeper = new GateKeeper(image, rb, myPortal);
        //player.getInventory().addItem(ItemType.GOLD);
        gateKeeper.negotiate(player);
        assertEquals(myPortal.isActivated(), true);
    }
    
    @Test
    public void testGetPortal() {
        initiateTestVariables();
        
        gateKeeper = null;
        gateKeeper = new GateKeeper(image, rb, myPortal);      
        assertNotNull(this.gateKeeper.getPortal());
        
    }
    
    @Test
    public void testActivateWarmHole() {
        initiateTestVariables();
        
        gateKeeper = null;
        gateKeeper = new GateKeeper(image, rb, myPortal);
        assertNotNull(this.gateKeeper);
        this.gateKeeper.activateWarmHole();
        assertEquals(myPortal.isActivated(), true);
    }
    
    @Test
    public void testDeactivateWarmHole() {
        initiateTestVariables();
        
        gateKeeper = null;
        gateKeeper = new GateKeeper(image, rb, myPortal);
        assertNotNull(this.gateKeeper);
        this.gateKeeper.deactivateWarmHole();
        assertEquals(myPortal.isActivated(), false);
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
