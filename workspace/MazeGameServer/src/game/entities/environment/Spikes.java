package game.entities.environment;

import engine.physics.RigidBody;
import game.enums.AnimationPath;

/*
* Classname:            Spikes.java
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
    
    public Spikes(RigidBody rb) {
        super(AnimationPath.SPIKES, rb);
        dangerous = true;
    }
}