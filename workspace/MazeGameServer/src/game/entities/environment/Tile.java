package game.entities.environment;

import engine.physics.RigidBody;
import game.entities.Entity;
import game.enums.AnimationPath;

/*
* Classname:            EnvironmentTile.java
*
* Version information:  1.0
*
* Date:                 10/30/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

/**
 * EnvironmentTile: Level background tile
 */
public class Tile extends Entity {

    public Tile(RigidBody rb) {
        super(AnimationPath.NONE, rb);
    }
    
    public void update(long elapsedTime) {
        // do nothing
    }
}
