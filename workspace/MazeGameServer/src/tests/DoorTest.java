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
import engine.Vertex2;
import game.Door;
import game.Door.Side;
import game.Entity;
import game.Game;
import game.GameEngine;
import game.MazeGameServer;
import game.Player;
import game.Room;

import org.junit.Test;

/**
 * JUnit tests for the Door class. These tests will not include anything from the superclass Entity.
 * Eventually Door will be separated from the Entity superclass.
 * DoorTest: <add description>
 */
public class DoorTest {
    
    public Door door;
    public Game game; // just a temporary game object to perform our tests on
    public String image;
    public int xPos;
    public int yPos;
    public Vertex2 exitLocation;
    public Room room;
    public Entity player;
    public Side side;
    
    public void main(String [] args) {
    }
    
    @Test
    public void testDoorConstructor() {
        initiateTestVariables();
        // linked Door is an optional parameter, so test with no linked door
        door = null;
        door = new Door(game, image, xPos, yPos, exitLocation, room, null, side);
        assertNotNull(this.door);
    }
    
    @Test
    public void testDoorPosition() {
        initiateTestVariables();
        door = null;
        door = new Door(game, image, xPos, yPos, exitLocation, room, null, side);
    }
    
    @Test
    public void testDoorExitLocation() {
        initiateTestVariables();
        door = null;
        door = new Door(game, image, xPos, yPos, exitLocation, room, null, side);
        assertEquals(door.getExit(), exitLocation);
    }
    
    @Test
    public void testDoorRoom() {
        initiateTestVariables();
        door = null;
        door = new Door(game, image, xPos, yPos, exitLocation, room, null, side);
        assertEquals(door.getRoom(), room);
    }
    
    @Test
    public void testDoorLink() {
        initiateTestVariables();
        Door linkedDoor = new Door(game, image, xPos + Door.TILESIZE, yPos, new Vertex2(xPos + (Door.TILESIZE*2), yPos), room, null, Side.LEFT);
        door = null;
        door = new Door(game, image, xPos, yPos, exitLocation, room, linkedDoor, side);
        assertNotNull(door);
        assertEquals(door.getLink(), linkedDoor);
        assertEquals(door, linkedDoor.getLink());
    }
    
    @Test
    public void testDoorSide() {
        initiateTestVariables();
        door = null;
        door = new Door(game, image, xPos, yPos, exitLocation, room, null, side);
        assertEquals(door.getSide(), side);
    }
    
    @Test
    public void testDoorContainsPlayer() {
        initiateTestVariables();
        door = null;
        door = new Door(game, image, xPos, yPos, exitLocation, room, null, side);
        player.setMinX(xPos); // moves player inside door
        player.setMinY(yPos);
        player.calculateBounds();
        assertEquals(door.contains(player), true);
    }
    
    @Test
    public void testDoorDoesNotContainPlayer() {
        initiateTestVariables();
        door = null;
        door = new Door(game, image, xPos, yPos, exitLocation, room, null, side);
        assertEquals(door.contains(player), false); // player was instantiated in the center of room
    }
    
    @Test
    public void testDoorTransportsPlayer() {
        initiateTestVariables();
        Door linkedDoor = new Door(game, image, xPos + Door.TILESIZE, yPos, new Vertex2(xPos + (Door.TILESIZE*2), yPos), room, null, Side.LEFT);
        door = null;
        door = new Door(game, image, xPos, yPos, exitLocation, room, linkedDoor, side);
        player.setMinX(0); // move player to origin
        player.setMinY(0);
        player.calculateBounds();
        door.transport(player);
        assertEquals((int)player.getMinX(), (int)linkedDoor.getExit().getX());
        assertEquals((int)player.getMinY(), (int)linkedDoor.getExit().getY());
    }
    
    /* Future work -- keys don't yet exist.
    @Test
    public void testDoorLock/Unlock {
        
    }*/
    
    public void initiateTestVariables() {
        try {
            game = new MazeGameServer(new GameEngine());
            image = "DoorImage";
            xPos = 232;
            yPos = 72;
            exitLocation = new Vertex2(xPos - Door.TILESIZE, yPos);
            side = Side.RIGHT;
            room = new Room();
            player = new Player(game, "player", 120, 72, 120, 72, 10, 10, 1, 0);
        } catch(Exception e) {
            System.out.println("Variable instantiation failed. Aborting JUnit tests.");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
