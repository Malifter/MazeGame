package tests;

/*
* Classname:            HealthBooster.java
*
* Version information:  1.0
*
* Date:                 12/4/2013
*
* Copyright notice:     Copyright (c) 2013 Rovshen Nazarov
*/

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import engine.Vector2f;
import engine.Vector2i;
import engine.physics.RigidBody;
import game.entities.Entity;
import game.entities.items.*;
import game.entities.EntityFactory;
import game.entities.environment.Door;
import game.entities.npcs.Player;
import game.enums.Side;
import game.environment.Room;
import game.levelloader.LevelLoader;
import game.GameEngine;
import game.MazeGameServer;
import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * JUnit tests for the HealthBooster class. 
 */



public class HealthBoosterTest {
    
    public HealthBooster boost;
    public String img;

    public Room room;

    public Player player;
    public RigidBody rb;
    public Side side;
    public int roomLayout = 0;
    public boolean locked;
    public Vector2i location;
    public int playerID = 0;
    
    public void main(String [] args) {
    }
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }
    
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }
    
    @Test
    public void testUse() {
        initiateTestVariables();
        boost = null;
        boost = new HealthBooster(rb);
        int currHealthPoints = player.getHealthPoints() + 5;
        boost.use(player);
        int newHealthPoints = player.getHealthPoints();
        assertEquals(currHealthPoints, newHealthPoints);
    }
    
    @Test
    public void testHealthBooster() {
        initiateTestVariables();
        boost = null;
        boost = new HealthBooster(rb);
        assertNotNull(this.boost);
    }
    
    public void initiateTestVariables() {
        try {
            
            //location.addEq(new Vector2i(LevelLoader.TILESIZE/2, LevelLoader.TILESIZE/2));

            rb = new RigidBody(new Vector2i(0,0), 24, 24);
            player = new Player(img, rb, playerID , room); 
            img = null;
           
        } catch(Exception e) {
            System.out.println("Variable instantiation failed. Aborting JUnit tests.");
            e.printStackTrace();
            System.exit(1);
        }
    }
    
}
