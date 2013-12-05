package tests;

import static org.junit.Assert.*;
import engine.Vector2i;
import engine.physics.RigidBody;
import game.entities.environment.Door;
import game.entities.environment.Portal;
import game.entities.npcs.Player;
import game.enums.Side;
import game.environment.Room;

import org.junit.Test;

public class PortalTest {
    
    public Portal portal;
    public String img;
    public int xPos;
    public int yPos;
    public Vector2i exitLocation;
    public Room room;
    public Player player;
    public RigidBody rb;
    public Side side;
    public int playerID = 0;
    public boolean locked;
    public Vector2i location;

    public void main(String [] args) {
    }
    
    @Test
    public void testPortalConstructor() {
        initiateTestVariables();
        // linked Door is an optional parameter, so test with no linked door
        portal = null;
        portal = new Portal(img, rb, room, side);
        assertNotNull(this.portal);
    }
    

    
    @Test
    public void testTransport() {
        fail("Not yet implemented");
    }
    
    @Test
    public void testPortal() {
        fail("Not yet implemented");
    }
    
    @Test
    public void testActivate() {
        fail("Not yet implemented");
    }
    
    @Test
    public void testDeactivate() {
        fail("Not yet implemented");
    }
    
    @Test
    public void testIsActivated() {
        fail("Not yet implemented");
    }
    
    public void initiateTestVariables() {
    try {
        
        
        rb = new RigidBody(new Vector2i(0, 0);, 24, 24);
                   
        img = null;      
        
        side = Side.RIGHT;
        room = new Room(playerID);
       
    } catch(Exception e) {
        System.out.println("Variable instantiation failed. Aborting JUnit tests.");
        e.printStackTrace();
        System.exit(1);
    }
    
}
