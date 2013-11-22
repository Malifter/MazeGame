package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import game.Door;
import game.Entity;
import game.Game;
import game.GameEngine;
import game.MazeGameServer;
import game.Obstacle;
import game.Player;
import game.Room;

import org.junit.Test;

/**
 * JUnit tests for the Chest class. These tests will not include anything from the superclass Entity.
 * ChestTest: <add description>
 */

public class RoomTest {
    
    public Game game; // just a temporary game object to perform our tests on
    public String image;
    public int roomLayout = 1;
    public int xPos;
    public int yPos;
    public Room room;
    public Entity tile;
    
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
    public void testAddToForeground() {
        initiateTestVariables();
        
        room = null;
        room = new Room(roomLayout);
        assertNotNull(this.room);
        this.room.addToForeground(tile);
    }
    
    @Test
    public void testAddPlayer() {
        initiateTestVariables();
        Player player = null;
        room = null;
        room = new Room(roomLayout);
        assertNotNull(this.room);
        this.room.addPlayer(player);
    }
    
    @Test
    public void testRemovePlayer() {
        initiateTestVariables();
        Player player = null;
        room = null;
        room = new Room(roomLayout);
        assertNotNull(this.room);
        this.room.addPlayer(player);
        this.room.removePlayer(player);
    }
    
    @Test
    public void testAddDoor() {
        initiateTestVariables();
        Door door = null;
        room = null;
        room = new Room(roomLayout);
        assertNotNull(this.room);
        this.room.addDoor(door);
    }
    
    @Test
    public void testNumPlayers() {
        initiateTestVariables();
        
        room = null;
        room = new Room(roomLayout);
        assertNotNull(this.room);
        Player player = null;
        this.room.addPlayer(player);
        assertEquals(this.room.numPlayers(), 1);
    }
    
    @Test
    public void testGetPlayers() {
        initiateTestVariables();
        
        room = null;
        room = new Room(roomLayout);
        assertNotNull(this.room);
        Player player = null;
        this.room.addPlayer(player);
        assertNotNull(this.room.getPlayers());
    }
    
    @Test
    public void testGetForeground() {
        initiateTestVariables();
        
        room = null;
        room = new Room(roomLayout);
        assertNotNull(this.room);
        this.room.addToForeground(tile);
        assertNotNull(this.room.getForeground());
    }
    
    @Test
    public void testGetDoors() {
        initiateTestVariables();
        
        room = null;
        room = new Room(roomLayout);
        assertNotNull(this.room);
        Door door = null;
        this.room.addDoor(door);
        assertNotNull(this.room.getDoors());
    }
       
    public void initiateTestVariables() {
        try {
            game = new MazeGameServer(new GameEngine());
            image = "chestLockedImage";
            xPos = 232;
            yPos = 72;
            tile = null;
        } catch(Exception e) {
            System.out.println("Variable instantiation failed. Aborting JUnit tests.");
            e.printStackTrace();
            System.exit(1);
        }
    }
    
}
