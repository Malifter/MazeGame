package tests;

/*
* Classname:            ChestTest.java
*
* Version information:  1.0
*
* Date:                 11/8/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import engine.Vector2i;
import engine.physics.RigidBody;
import game.GameEngine;
import game.MazeGameServer;
import game.entities.environment.Chest;
import game.environment.Interior;
import game.environment.Room;

import org.junit.Test;

/**
 * JUnit tests for the Chest class. These tests will not include anything from the superclass Entity.
 * ChestTest: <add description>
 */
public class ChestTest {
    
    public String image;
    public int xPos;
    public int yPos;
    public Chest chest;
    public Interior room;
    public RigidBody rb;
    
    public void main(String [] args) {
    }
    
    @Test
    public void testChestConstructor() {
        initiateTestVariables();
       

        chest = new Chest(rb, room);
        assertNotNull(this.chest);
    }
    
    @Test
    public void testGenerateChestContents() {
        initiateTestVariables();
       
        chest = new Chest(rb, room);
        
        chest.generateContents();
        assertNotNull(chest.getContents());
    }
    
    @Test
    public void testDropChestContentsAndLocks() {
        initiateTestVariables();
        
        chest = new Chest(rb, room);
        chest.lock();
        assertEquals(chest.dropContents(), false);
        chest.unlock();
        assertEquals(chest.dropContents(), true);
    }
    
    public void initiateTestVariables() {
        try {
            rb = new RigidBody(new Vector2i(0, 0), 24, 24);
            image = null;
            room = new Interior(new Vector2i(0, 0), 0);
        } catch(Exception e) {
            System.out.println("Variable instantiation failed. Aborting JUnit tests.");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
