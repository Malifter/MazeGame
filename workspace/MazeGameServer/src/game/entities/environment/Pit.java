package game.entities.environment;

import engine.physics.RigidBody;

/*
* Classname:            Pit.java
*
* Version information:  1.0
*
* Date:                 12/1/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

/**
 * Pit: pit
 */
public class Pit extends Obstacle {
    private static final long serialVersionUID = -4363795480195834006L;
    
    public Pit(String image, RigidBody rb) {
        super(image, rb);
        dangerous = false;
        destructable = false;
        blocking = true;
        openable = false;
    }
}