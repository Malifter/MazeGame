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
import engine.Vector2i;
import engine.physics.RigidBody;
import game.entities.environment.Obstacle;

import org.junit.Test;

/**
 * JUnit tests for the Chest class. These tests will not include anything from the superclass Entity.
 * ChestTest: <add description>
 */
public class ObstacleTest {
    public static final int COLLISION_DAMAGE = 10;
    public boolean destructable = false;
    public boolean dangerous = false;
    public boolean blocking = false;
    public boolean openable = false;
    public boolean moveable = false;
    public Obstacle obstacle;
    public RigidBody rBody = new RigidBody(new Vector2i(0,0), 0, 0); 
    public String image;
    public void main(String [] args) {
    }
    
    @Test
    public void testObstacleConstructor() {
        // linked Door is an optional parameter, so test with no linked door
        obstacle = null;
        obstacle = new Obstacle(image, rBody);
        assertNotNull(this.obstacle);
    }
    

    @Test
    public void testIsDangerous() {
        obstacle = null;
        obstacle = new Obstacle(image, rBody);
        assertNotNull(this.obstacle);
        assertEquals(obstacle.isDangerous(),false);
    }
    
    @Test
    public void testisOpenable() {
        obstacle = null;
        obstacle = new Obstacle(image, rBody);
        assertNotNull(this.obstacle);
        assertEquals(obstacle.isOpenable(),false);
    }
    
    @Test
    public void testIsMoveable() {
        obstacle = null;
        obstacle = new Obstacle(image, rBody);
        assertNotNull(this.obstacle);
        assertEquals(obstacle.isMoveable(),false);
    }
    
    public void destroy() {
        obstacle = null;
        obstacle = new Obstacle(image, rBody);
        assertNotNull(this.obstacle);
        destructable = true;
        obstacle.destroy();
        assertEquals(obstacle.isEnabled(), false);
    }
}
