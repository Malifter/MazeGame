package game.entities.environment;

import engine.physics.RigidBody;
import game.enums.AnimationPath;

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

    public Rock(RigidBody rb) {
        super(AnimationPath.ROCK, rb);
        destructable = true;
        blocking = true;
    }
}