package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import engine.Vector2i;
import engine.physics.RigidBody;

import game.entities.Entity;

import game.entities.npcs.Player;
import game.environment.Room;

import org.junit.Test;

/**
 * JUnit tests for the Chest class. These tests will not include anything from the superclass Entity.
 * ChestTest: <add description>
 */

public class RoomTest {
    
  
    public int roomLayout;
    public int xPos;
    public int yPos;
    public Room room;
    public Entity tile;
    public Player player;
    
    public void main(String [] args) {
    }
    
    @Test
    public void testRoomConstructor() {
        initiateTestVariables();

        room = null;
        room = new Room(roomLayout);
        assertNotNull(this.room);
        
    }
    
    
    @Test
    public void testAddPlayer() {
        initiateTestVariables();
        player = new Player (null, new RigidBody(new Vector2i(0, 0), 24, 24), 0, room);
        room = null;
        room = new Room(roomLayout);
        this.room.addPlayer(player);
        assertEquals(true, room.getPlayers().contains(player));
        
    }
    
    @Test
    public void testRemovePlayer() {
        initiateTestVariables();
        player = new Player (null, new RigidBody(new Vector2i(0, 0), 24, 24), 0, room);
        room = null;
        room = new Room(roomLayout);
        assertNotNull(this.room);
        this.room.addPlayer(player);
        this.room.removePlayer(player);
        assertEquals(room.hasPlayers(), false);
    }
    
    
    @Test
    public void testNumPlayers() {
        initiateTestVariables();
        
        room = null;
        room = new Room(roomLayout);
        assertNotNull(this.room);
        player = new Player (null, new RigidBody(new Vector2i(0, 0), 24, 24), 0, room);
        this.room.addPlayer(player);
        assertEquals(this.room.numPlayers(), 1);
    }
    
    @Test
    public void testGetPlayers() {
        initiateTestVariables();
        
        room = null;
        room = new Room(roomLayout);
        assertNotNull(this.room);
        player = new Player (null, new RigidBody(new Vector2i(0, 0), 24, 24), 0, room);
        this.room.addPlayer(player);
        assertNotNull(this.room.getPlayers());
    }
    
   
    
    public void initiateTestVariables() {
        try {
            roomLayout = 1;
        } catch(Exception e) {
            System.out.println("Variable instantiation failed. Aborting JUnit tests.");
            e.printStackTrace();
            System.exit(1);
        }
    }
    
}
