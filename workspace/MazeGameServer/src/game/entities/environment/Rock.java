package game.entities.environment;

import engine.physics.RigidBody;

/*
* Classname:            Rock.java
*
* Version information:  1.0
*
* Date:                 12/1/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

/**
 * Rock: rock
 */
public class Rock extends Obstacle {
    private static final long serialVersionUID = -4363795480195834006L;
    
    public Rock(String image, RigidBody rb) {
        super(image, rb);
        dangerous = false;
        destructable = true;
        blocking = true;
    }
}