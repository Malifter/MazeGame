package tests;

/*
* Classname:            Obstacle.java
*
* Version information:  1.0
*
* Date:                 11/8/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import game.Game;
import game.GameEngine;
import game.MazeGameServer;
import game.Obstacle;

import org.junit.Test;

/**
 * JUnit tests for the Chest class. These tests will not include anything from the superclass Entity.
 * ChestTest: <add description>
 */
public class ObstacleTest {
    
    public Game game; // just a temporary game object to perform our tests on
    public String image;
    public int xPos;
    public int yPos;
    public Obstacle obstacle;
    
    public void main(String [] args) {
    }
    
    @Test
    public void testObstacleConstructor() {
        initiateTestVariables();
        // linked Door is an optional parameter, so test with no linked door
        obstacle = null;
        obstacle = new Obstacle(game, image, xPos, yPos, xPos, yPos, xPos, yPos);
        assertNotNull(this.obstacle);
    }
    
    @Test
    public void testObstacleBounds() {
        obstacle = null;
        obstacle = new Obstacle(game, image, xPos, yPos, xPos, yPos, xPos, yPos);
        assertEquals((int)obstacle.getMinX(), xPos);
        assertEquals((int)obstacle.getMinY(), yPos);
        assertEquals((int)obstacle.getImageX(), xPos);
        assertEquals((int)obstacle.getImageY(), yPos);
    }
    
    @Test
    public void testGetAndSetDestructable() {
        initiateTestVariables();
        obstacle = null;
        obstacle = new Obstacle(game, image, xPos, yPos, xPos, yPos, xPos, yPos);
        obstacle.setDestructable(true);
        assertEquals(obstacle.isDestructable(), true);
        obstacle.setDestructable(false);
        assertEquals(obstacle.isDestructable(), false);
    }
    
    @Test
    public void testGetAndSetDangerous() {
        initiateTestVariables();
        obstacle = null;
        obstacle = new Obstacle(game, image, xPos, yPos, xPos, yPos, xPos, yPos);
        obstacle.setDangerous(true);
        assertEquals(obstacle.isDangerous(), true);
        obstacle.setDangerous(false);
        assertEquals(obstacle.isDangerous(), false);
    }
    
    public void initiateTestVariables() {
        try {
            game = new MazeGameServer(new GameEngine());
            image = "obstacleImage";
            xPos = 232;
            yPos = 72;
        } catch(Exception e) {
            System.out.println("Variable instantiation failed. Aborting JUnit tests.");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
