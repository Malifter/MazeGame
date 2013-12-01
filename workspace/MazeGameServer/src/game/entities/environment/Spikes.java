package game.entities.environment;

import engine.physics.RigidBody;

/*
* Classname:            SpikeEntity.java
*
* Version information:  1.0
*
* Date:                 10/30/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

/**
 * SpikeEntity: spike traps
 */
public class Spikes extends Obstacle {
    private static final long serialVersionUID = -4363795480195834006L;
    
    public Spikes(String image, RigidBody rb) {
        super(image, rb);
        dangerous = true;
        destructable = false;
        blocking = false;
    }
}