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
import engine.Vector2i;
import engine.physics.RigidBody;
import game.entities.Entity;
import game.entities.environment.Door;
import game.entities.npcs.Player;
import game.enums.Side;
import game.environment.Room;
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
    public String image;
    public int xPos;
    public int yPos;
    public Vector2i exitLocation;
    public Room room;
    public Entity player;
    
    public void main(String [] args) {
    }
    
    @Test
    public void testDoorConstructor() {
        initiateTestVariables();
        // linked Door is an optional parameter, so test with no linked door
        door = null;
        String img = null; 
        RigidBody rb = null;
        Vector2i exitLoc = null;
        Room room = null;
        Door linkedDoor = null;
        Side side = null;
        boolean locked = false;
        door = new Door(img, rb, exitLoc, room, linkedDoor, side, locked);
        assertNotNull(this.door);
    }
    
    @Test
    public void testDoorPosition() {
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
    }
    
    @Test
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
    }
    
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
    public void testDoorLink() {
        initiateTestVariables();
        door = null;
        String img = null; 
        RigidBody rb = null;
        Vector2i exitLoc = null;
        Room room = null;
        Door linkedDoor = null;
        Side side = null;
        boolean locked = false;
        door = new Door(img, rb, exitLoc, room, linkedDoor, side, locked);assertNotNull(door);
        assertEquals(door.getLink(), linkedDoor);
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
    
    @Test
    public void testDoorContainsPlayer() {
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
        player.getRigidBody().move(10, 10); // moves player inside door        
        assertEquals(door.getRoom(), room);
    }
    
    @Test
    public void testDoorDoesNotContainPlayer() {
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
    public void testDoorTransportsPlayer() {
        initiateTestVariables();
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
    
    /* Future work -- keys don't yet exist.
    @Test
    public void testDoorLock/Unlock {
        
    }*/
    
    public void initiateTestVariables() {
        try {
            String img = null;
            RigidBody rb = null;
            int playerID = 1;
            Room room = null;
            image = "DoorImage";
            xPos = 232;
            yPos = 72;            
            //room = new Room();
            player = new Player(img, rb, playerID, room);
        } catch(Exception e) {
            System.out.println("Variable instantiation failed. Aborting JUnit tests.");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
