package game.entities.environment;

import engine.physics.RigidBody;
import game.enums.AnimationPath;

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

    public Pit(RigidBody rb) {
        super(AnimationPath.PIT, rb);
        blocking = true;
    }
}