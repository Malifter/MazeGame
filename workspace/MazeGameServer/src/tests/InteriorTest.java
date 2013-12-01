package tests;

/*
* Classname:            InteriorTest.java
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

import engine.Vector2i;
import game.entities.Entity;
import game.entities.environment.Door;
import game.entities.environment.Tile;
import game.entities.environment.Spikes;
import game.entities.environment.Door.Side;
import game.entities.npcs.Player;
import game.entities.npcs.Woodman;
import game.environment.Interior;
import game.Game;
import game.GameEngine;
import game.MazeGameServer;

import org.junit.Test;

/**
 * JUnit tests for the Door class. These tests will not include anything from the superclass Entity.
 * Eventually Door will be separated from the Entity superclass.
 * DoorTest: <add description>
 */
public class InteriorTest {
    public Game game; // just a temporary game object to perform our tests on
    public int xPos;
    public int yPos;
    public Interior Interior;
    public ArrayList<Entity> players = new ArrayList<Entity>();
    public ArrayList<Entity> foreground = new ArrayList<Entity>();
    public ArrayList<Entity> background = new ArrayList<Entity>();
    public ArrayList<Entity> traps = new ArrayList<Entity>();
    public ArrayList<Door> doors = new ArrayList<Door>();
    public ArrayList<Entity> enemies = new ArrayList<Entity>();
    
    public void main(String [] args) {
    }
    
    @Test
    public void testInteriorConstructor() {
        initiateTestVariables();
        Interior = null;
        Interior = new Interior(new Vector2i(xPos, yPos), xPos);
        assertNotNull(this.Interior);
    }
    
    @Test
    public void testInteriorLocationAndCenter() {
        Interior = null;
        Vector2i location = new Vector2i(xPos, yPos);
        Vector2i center = new Vector2i(xPos + (Interior.WIDTH/2), yPos + (Interior.HEIGHT/2));
        Interior = new Interior(location, xPos);
        assertEquals(Interior.getLocation(), location);
        assertEquals(Interior.getCenter(), center);
    }
    
    @Test
    public void testInteriorAddAndGetForeground() {
        initiateTestVariables();
        Interior = null;
        Interior = new Interior(new Vector2i(xPos, yPos), xPos);
        for(int i = 0; i < foreground.size(); i++) {
            Interior.addToForeground(foreground.get(i));
        }
        assertArrayEquals(Interior.getForeground().toArray(), foreground.toArray());
    }
    
    @Test
    public void testInteriorAddAndGetTraps() {
        initiateTestVariables();
        Interior = null;
        Interior = new Interior(new Vector2i(xPos, yPos), xPos);
        for(int i = 0; i < traps.size(); i++) {
            Interior.addTrap(traps.get(i));
        }
        assertArrayEquals(Interior.getTraps().toArray(), traps.toArray());
    }
    
    @Test
    public void testInteriorAddAndGetEnemies() {
        initiateTestVariables();
        Interior = null;
        Interior = new Interior(new Vector2i(xPos, yPos), xPos);
        for(int i = 0; i < enemies.size(); i++) {
            Interior.addEnemy(enemies.get(i));
        }
        assertArrayEquals(Interior.getEnemies().toArray(), enemies.toArray());
    }
    
    @Test
    public void testInteriorRemoveEnemy() {
        initiateTestVariables();
        Interior = null;
        Interior = new Interior(new Vector2i(xPos, yPos), xPos);
        for(Entity e: enemies) {
            Interior.addEnemy(e);
        }
        assertArrayEquals(Interior.getEnemies().toArray(), enemies.toArray());
        for(Entity e: enemies) {
            Interior.removeEnemy(e);
        }
        assertEquals(Interior.getPlayers().isEmpty(), true);
    }
    
    @Test
    public void testInteriorAddAndGetPlayers() {
        initiateTestVariables();
        Interior = null;
        Interior = new Interior(new Vector2i(xPos, yPos), xPos);
        for(int i = 0; i < players.size(); i++) {
            Interior.addPlayer(players.get(i));
        }
        assertArrayEquals(Interior.getPlayers().toArray(), players.toArray());
    }
    
    @Test
    public void testInteriorGetNumberOfPlayers() {
        initiateTestVariables();
        Interior = null;
        Interior = new Interior(new Vector2i(xPos, yPos), xPos);
        for(int i = 0; i < players.size(); i++) {
            Interior.addPlayer(players.get(i));
        }
        assertEquals(Interior.numPlayers(), players.size());
    }
    
    @Test
    public void testInteriorRemovePlayer() {
        initiateTestVariables();
        Interior = null;
        Interior = new Interior(new Vector2i(xPos, yPos), xPos);
        for(Entity p: players) {
            Interior.addPlayer(p);
        }
        assertArrayEquals(Interior.getPlayers().toArray(), players.toArray());
        for(Entity p: players) {
            Interior.removePlayer(p);
        }
        assertEquals(Interior.getPlayers().isEmpty(), true);
    }
    
    @Test
    public void testInteriorAddAndGetDoors() {
        initiateTestVariables();
        Interior = null;
        Interior = new Interior(new Vector2i(xPos, yPos), xPos);
        for(int i = 0; i < doors.size(); i++) {
            Interior.addDoor(doors.get(i));
        }
        assertArrayEquals(Interior.getDoors().toArray(), doors.toArray());
    }
    
    public void initiateTestVariables() {
        try {
            game = new MazeGameServer(new GameEngine());
            xPos = 240;
            yPos = 144;
            doors.add(new Door(game, "door1", xPos + 120, yPos + 8, new Vector2i(xPos + 120, yPos + 8 + Door.TILESIZE), null, null, Side.TOP));
            doors.add(new Door(game, "door2", xPos + 120, yPos + 136, new Vector2i(xPos + 120, yPos + 136 - Door.TILESIZE), null, null, Side.BOTTOM));
            doors.add(new Door(game, "door3", xPos + 232, yPos + 72, new Vector2i(xPos + 232 - Door.TILESIZE, yPos + 72), null, null, Side.RIGHT));
            doors.add(new Door(game, "door4", xPos + 8, yPos + 72, new Vector2i(xPos + 8 + Door.TILESIZE, yPos + 72), null, null, Side.LEFT));
            for(int i = 0; i < 4; i++) {
                background.add(new Tile(game, "bg"+i, xPos + (i*Tile.TILESIZE), yPos));
                foreground.add(new Tile(game, "fg"+i, xPos, yPos + (i*Tile.TILESIZE)));
                traps.add(new Spikes(game, "spike"+1, i, i, i, i, i, i));
                enemies.add(new Woodman(game, "woodman"+1, i, i, i, i, i, i, null));
                players.add(new Player(game, "player", xPos + 104 + (i*10), yPos + 72, xPos + 104 + (i*10), yPos + 72, 10, 10, 1, 0));
            }
        } catch(Exception e) {
            System.out.println("Variable instantiation failed. Aborting JUnit tests.");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
