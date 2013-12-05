package tests;

/*
* Classname:            DoorTest.java
*
* Version information:  1.0
*
* Date:                 11/4/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import engine.Vector2f;
import engine.Vector2i;
import engine.physics.RigidBody;
import game.entities.Entity;
import game.entities.EntityFactory;
import game.entities.environment.Door;
import game.entities.npcs.Player;
import game.enums.Side;
import game.environment.Room;
import game.levelloader.LevelLoader;
import game.GameEngine;
import game.MazeGameServer;

import org.junit.Test;

/**
 * JUnit tests for the Door class. These tests will not include anything from the superclass Entity.
 * Eventually Door will be separated from the Entity superclass.
 * DoorTest: <add description>
 */
public class DoorTest {
    
    public Door door;
    public String img;
    public int xPos;
    public int yPos;
    public Vector2i exitLocation;
    public Room room;
    public Room room2;
    public Player player;
    public RigidBody rb;
    public Side side;
    public int roomLayout = 0;
    public boolean locked;
    public Vector2i location;
    
    public void main(String [] args) {
    }
    
    @Test
    public void testDoorConstructor() {
        initiateTestVariables();
        // linked Door is an optional parameter, so test with no linked door
        door = null;
        door = new Door(rb, exitLocation, room, null, side);
        assertNotNull(this.door);
    }
    
    @Test
    public void testDoorPosition() {
        initiateTestVariables();
        door = null;
        
        door = new Door(rb, exitLocation, room, null, side);
    }
    
    /*@Test
    public void testDoorExitLocation() {
        initiateTestVariables();
        door = null;
        String img = null; 
        RigidBody rb = null;
        Vector2i exitLoc = null;
        Room room = null;
        Door linkedDoor = null;
        Side side = null;
        boolean locked = false;
        door = new Door(img, rb, exitLoc, room, linkedDoor, side, locked);
        assertEquals(door.getExit(), exitLocation);
    }*/
    
    /*
    @Test
    public void testDoorRoom() {
        initiateTestVariables();
        door = null;
        String img = null; 
        RigidBody rb = null;
        Vector2i exitLoc = null;
        Room room = null;
        Door linkedDoor = null;
        Side side = null;
        boolean locked = false;
        door = new Door(img, rb, exitLoc, room, linkedDoor, side, locked);
        assertEquals(door.getRoom(), room);
    }
    
    
    @Test
    public void testDoorSide() {
        initiateTestVariables();
        door = null;
        String img = null; 
        RigidBody rb = null;
        Vector2i exitLoc = null;
        Room room = null;
        Door linkedDoor = null;
        Side side = null;
        boolean locked = false;
        door = new Door(img, rb, exitLoc, room, linkedDoor, side, locked);
        assertEquals(door.getRoom(), room);
    }
    */

    @Test
    public void testDoorLink() {
        initiateTestVariables();
        Door linkedDoor = new Door(rb, new Vector2i(0 + LevelLoader.TILESIZE, 0 + LevelLoader.TILESIZE), room, null, Side.LEFT);
        door = new Door(rb, exitLocation, room, linkedDoor, side);  
        assertNotNull(door);
        assertEquals(door.getLink(), linkedDoor);
        assertEquals(door, linkedDoor.getLink());
    }
    

    
    @Test
    public void testDoorTransportsPlayer() {
        initiateTestVariables();
        room2 = new Room(roomLayout);
        Door linkedDoor = new Door(rb, new Vector2i(0 + LevelLoader.TILESIZE, 0 + LevelLoader.TILESIZE), room2, null, Side.LEFT);
        door = new Door(rb, exitLocation, room, linkedDoor, side);  
        door.unlock();
        linkedDoor.unlock();
        System.out.println(player.getRigidBody().getLocation());
        
        player.getRigidBody().setLocation(door.getRigidBody().getLocation());
        
        System.out.println(player.getRigidBody().getLocation());
        
        //door.transport(player);
                
        //assertEquals(room2.getPlayers().contains(player), true);
        //assertEquals((int)player.getMinY(), (int)linkedDoor.getExit().getY());
    }
    
    
    @Test
    public void testDoorLock () {
        initiateTestVariables();
        door = new Door(rb, exitLocation, room, null, side); 
        door.lock();
        assertEquals(door.isLocked(), true);
        
    }
    
    public void initiateTestVariables() {
        try {
            
            //location.addEq(new Vector2i(LevelLoader.TILESIZE/2, LevelLoader.TILESIZE/2));
            exitLocation = new Vector2i(0, 0);
            rb = new RigidBody(exitLocation, 24, 24);
                       
            img = "animations/door/unlocked/down/door.gif";
            //xPos = 232;
            //yPos = 72;            
            
            side = Side.RIGHT;
            room = new Room(roomLayout);
            //player = new Player("player", rb, playerID, room);
            locked = true;
        } catch(Exception e) {
            System.out.println("Variable instantiation failed. Aborting JUnit tests.");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
