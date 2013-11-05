package tests;

/*
* Classname:            RoomTest.java
*
* Version information:  1.0
*
* Date:                 11/4/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import engine.Position;
import game.Door;
import game.Door.Side;
import game.Entity;
import game.EnvironmentTile;
import game.Game;
import game.GameEngine;
import game.MazeGameServer;
import game.Player;
import game.Room;
import game.SpikeEntity;
import game.WoodManEntity;

import org.junit.Test;

/**
 * JUnit tests for the Door class. These tests will not include anything from the superclass Entity.
 * Eventually Door will be separated from the Entity superclass.
 * DoorTest: <add description>
 */
public class RoomTest {
    public Game game; // just a temporary game object to perform our tests on
    public int xPos;
    public int yPos;
    public Room room;
    public ArrayList<Entity> players = new ArrayList<Entity>();
    public ArrayList<Entity> foreground = new ArrayList<Entity>();
    public ArrayList<Entity> background = new ArrayList<Entity>();
    public ArrayList<Entity> traps = new ArrayList<Entity>();
    public ArrayList<Door> doors = new ArrayList<Door>();
    public ArrayList<Entity> enemies = new ArrayList<Entity>();
    
    public void main(String [] args) {
    }
    
    @Test
    public void testRoomConstructor() {
        initiateTestVariables();
        room = null;
        room = new Room(new Position<Integer, Integer>(xPos, yPos));
        assertNotNull(this.room);
    }
    
    @Test
    public void testRoomLocationAndCenter() {
        room = null;
        Position<Integer, Integer> location = new Position<Integer, Integer>(xPos, yPos);
        Position<Integer, Integer> center = new Position<Integer, Integer>(xPos + (Room.WIDTH/2), yPos + (Room.HEIGHT/2));
        room = new Room(location);
        assertEquals(room.getLocation(), location);
        assertEquals(room.getCenter(), center);
    }
    
    @Test
    public void testRoomAddAndGetForeground() {
        initiateTestVariables();
        room = null;
        room = new Room(new Position<Integer, Integer>(xPos, yPos));
        for(int i = 0; i < foreground.size(); i++) {
            room.addToForeground(foreground.get(i));
        }
        assertArrayEquals(room.getForeground().toArray(), foreground.toArray());
    }
    
    @Test
    public void testRoomAddAndGetBackground() {
        initiateTestVariables();
        room = null;
        room = new Room(new Position<Integer, Integer>(xPos, yPos));
        for(int i = 0; i < background.size(); i++) {
            room.addToBackground(background.get(i));
        }
        assertArrayEquals(room.getBackground().toArray(), background.toArray());
    }
    
    @Test
    public void testRoomAddAndGetTraps() {
        initiateTestVariables();
        room = null;
        room = new Room(new Position<Integer, Integer>(xPos, yPos));
        for(int i = 0; i < traps.size(); i++) {
            room.addTrap(traps.get(i));
        }
        assertArrayEquals(room.getTraps().toArray(), traps.toArray());
    }
    
    @Test
    public void testRoomAddAndGetEnemies() {
        initiateTestVariables();
        room = null;
        room = new Room(new Position<Integer, Integer>(xPos, yPos));
        for(int i = 0; i < enemies.size(); i++) {
            room.addEnemy(enemies.get(i));
        }
        assertArrayEquals(room.getEnemies().toArray(), enemies.toArray());
    }
    
    @Test
    public void testRoomRemoveEnemy() {
        initiateTestVariables();
        room = null;
        room = new Room(new Position<Integer, Integer>(xPos, yPos));
        for(Entity e: enemies) {
            room.addEnemy(e);
        }
        assertArrayEquals(room.getEnemies().toArray(), enemies.toArray());
        for(Entity e: enemies) {
            room.removeEnemy(e);
        }
        assertEquals(room.getPlayers().isEmpty(), true);
    }
    
    @Test
    public void testRoomAddAndGetPlayers() {
        initiateTestVariables();
        room = null;
        room = new Room(new Position<Integer, Integer>(xPos, yPos));
        for(int i = 0; i < players.size(); i++) {
            room.addPlayer(players.get(i));
        }
        assertArrayEquals(room.getPlayers().toArray(), players.toArray());
    }
    
    @Test
    public void testRoomGetNumberOfPlayers() {
        initiateTestVariables();
        room = null;
        room = new Room(new Position<Integer, Integer>(xPos, yPos));
        for(int i = 0; i < players.size(); i++) {
            room.addPlayer(players.get(i));
        }
        assertEquals(room.numPlayers(), players.size());
    }
    
    @Test
    public void testRoomRemovePlayer() {
        initiateTestVariables();
        room = null;
        room = new Room(new Position<Integer, Integer>(xPos, yPos));
        for(Entity p: players) {
            room.addPlayer(p);
        }
        assertArrayEquals(room.getPlayers().toArray(), players.toArray());
        for(Entity p: players) {
            room.removePlayer(p);
        }
        assertEquals(room.getPlayers().isEmpty(), true);
    }
    
    @Test
    public void testRoomAddAndGetDoors() {
        initiateTestVariables();
        room = null;
        room = new Room(new Position<Integer, Integer>(xPos, yPos));
        for(int i = 0; i < doors.size(); i++) {
            room.addDoor(doors.get(i));
        }
        assertArrayEquals(room.getDoors().toArray(), doors.toArray());
    }
    
    public void initiateTestVariables() {
        try {
            game = new MazeGameServer(new GameEngine());
            xPos = 240;
            yPos = 144;
            doors.add(new Door(game, "door1", xPos + 120, yPos + 8, new Position<Integer, Integer>(xPos + 120, yPos + 8 + Door.TILESIZE), null, null, Side.TOP));
            doors.add(new Door(game, "door2", xPos + 120, yPos + 136, new Position<Integer, Integer>(xPos + 120, yPos + 136 - Door.TILESIZE), null, null, Side.BOTTOM));
            doors.add(new Door(game, "door3", xPos + 232, yPos + 72, new Position<Integer, Integer>(xPos + 232 - Door.TILESIZE, yPos + 72), null, null, Side.RIGHT));
            doors.add(new Door(game, "door4", xPos + 8, yPos + 72, new Position<Integer, Integer>(xPos + 8 + Door.TILESIZE, yPos + 72), null, null, Side.LEFT));
            for(int i = 0; i < 4; i++) {
                background.add(new EnvironmentTile(game, "bg"+i, xPos + (i*EnvironmentTile.TILESIZE), yPos));
                foreground.add(new EnvironmentTile(game, "fg"+i, xPos, yPos + (i*EnvironmentTile.TILESIZE)));
                traps.add(new SpikeEntity(game, "spike"+1, i, i, i, i, i, i));
                enemies.add(new WoodManEntity(game, "woodman"+1, i, i, i, i, i, i, null));
                players.add(new Player(game, "player", xPos + 104 + (i*10), yPos + 72, xPos + 104 + (i*10), yPos + 72, 10, 10, 1, 0));
            }
        } catch(Exception e) {
            System.out.println("Variable instantiation failed. Aborting JUnit tests.");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
