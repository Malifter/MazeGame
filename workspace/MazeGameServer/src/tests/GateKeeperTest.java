package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import game.Entity;
import game.Game;
import game.GameEngine;
import game.GateKeeper;
import game.Interior;
import game.MazeGameServer;
import game.Obstacle;
import game.Portal;

import org.junit.Test;

/**
 * JUnit tests for the GateKeeper class, which corresponds to negotiator use case.
 * These tests will not include anything from the superclass Entity.
 * GateKeeperTest: <add description>
 */

public class GateKeeperTest {
    
    public Game game; // just a temporary game object to perform our tests on
    public String image;
    public int xPos;
    public int yPos;
    public GateKeeper gateKeeper;
    public Portal myPortal;
    public Interior myRoom;
    public ArrayList<Interior> rooms;
    public Entity player;
    
    public void main(String [] args) {
    }
    
    @Test
    public void testGateKeeperConstructor() {
        initiateTestVariables();
        
        gateKeeper = null;
        gateKeeper = new GateKeeper(game, image, xPos, yPos, xPos, yPos, xPos, yPos, myPortal);
        assertNotNull(this.gateKeeper);
    }
    
    @Test
    public void testNegotiate() {
        initiateTestVariables();
        
        gateKeeper = null;
        gateKeeper = new GateKeeper(game, image, xPos, yPos, xPos, yPos, xPos, yPos, myPortal);
        gateKeeper.negotiate(player);
        assertEquals(myPortal.isActivated(), true);
    }
    
    @Test
    public void testGetPortal() {
        initiateTestVariables();
        
        gateKeeper = null;
        gateKeeper = new GateKeeper(game, image, xPos, yPos, xPos, yPos, xPos, yPos, myPortal);        
        assertNotNull(this.gateKeeper.getPortal());
        
    }
    
    @Test
    public void testActivateWarmHole() {
        initiateTestVariables();
        
        gateKeeper = null;
        gateKeeper = new GateKeeper(game, image, xPos, yPos, xPos, yPos, xPos, yPos, myPortal);
        assertNotNull(this.gateKeeper);
        this.gateKeeper.activateWarmHole();
        assertEquals(myPortal.isActivated(), true);
    }
    
    @Test
    public void testDeactivateWarmHole() {
        initiateTestVariables();
        
        gateKeeper = null;
        gateKeeper = new GateKeeper(game, image, xPos, yPos, xPos, yPos, xPos, yPos, myPortal);
        assertNotNull(this.gateKeeper);
        this.gateKeeper.deactivateWarmHole();
        assertEquals(myPortal.isActivated(), false);
    }
    public void initiateTestVariables() {
        try {            
            game = new MazeGameServer(new GameEngine());
            image = "chestLockedImage";
            xPos = 232;
            yPos = 72;
            myPortal = null;
            myRoom = null;
            myPortal = new Portal(game, image, xPos, yPos, myRoom, rooms);
            player = null;
            
        } catch(Exception e) {
            System.out.println("Variable instantiation failed. Aborting JUnit tests.");
            e.printStackTrace();
            System.exit(1);
        }
    }
    
}
