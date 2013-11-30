package game.entities.environment;

import engine.physics.RigidBody;
import game.entities.Entity;

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

    /**
     * Constructor
     * @param g
     * @param anImage
     * @param x
     * @param y
     */
    public Tile(String img, RigidBody rb) {
        super(img, rb);
    }
}
