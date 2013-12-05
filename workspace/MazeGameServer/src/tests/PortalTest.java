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
    public RigidBody rb2;
    public Side side;
    public int playerID = 0;
    public int roomLayout = 0;
    public boolean locked;
    public Vector2i location;

    public void main(String [] args) {
    }
    
    @Test
    public void testPortalConstructor() {
        initiateTestVariables();
        
        portal = null;
        portal = new Portal(img, rb, room, side);
        assertNotNull(this.portal);
    }
    

    
    @Test
    public void testTransport() {
        initiateTestVariables();
        
        player = new Player(img, rb2, playerID, room);
        portal = null;
        portal = new Portal(img, rb, room, side);
        
        portal.activate();
        player.getRigidBody().setLocation(portal.getRigidBody().getLocation());
        
        portal.transport(player);

        assertEquals(this.room.hasPlayers(), false);
       
    }
    
   
    @Test
    public void testActivate() {
        initiateTestVariables();
        
        portal = null;
        portal = new Portal(img, rb, room, side);
        portal.activate();
        assertEquals(portal.isActivated(), true);
    }
    
    @Test
    public void testDeactivate() {
        initiateTestVariables();
        
        portal = null;
        portal = new Portal(img, rb, room, side);
        portal.deactivate();
        assertEquals(portal.isActivated(), false);
    }
    
    @Test
    public void testIsActivated() {
        initiateTestVariables();
        
        portal = null;
        portal = new Portal(img, rb, room, side);
        portal.activate();
        assertEquals(portal.isActivated(), true);
    }
    
    public void initiateTestVariables() {
    try {

        rb = new RigidBody(new Vector2i(0, 0), 24, 24);
        rb2 = new RigidBody(new Vector2i(0, 0), 24, 24);
        room = new Room(roomLayout);
              
        img = null;      
        
        side = Side.RIGHT;
        
       
    } catch(Exception e) {
        System.out.println("Variable instantiation failed. Aborting JUnit tests.");
        e.printStackTrace();
        System.exit(1);
    }
    
    }
}
