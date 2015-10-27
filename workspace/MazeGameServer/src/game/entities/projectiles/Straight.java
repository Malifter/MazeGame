package game.entities.projectiles;

import engine.Vector2f;
import engine.physics.Collisions;
import engine.physics.RigidBody;
import game.entities.npcs.Hostile;
import game.enums.AnimationPath;
import game.enums.Face;

/*
* Classname:            Straight.java
*
* Version information:  1.0
*
* Date:                 12/4/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

/**
 * A projectile
 */
public class Straight extends Projectile {
    private Vector2f origin;
    protected static final float SPEED = 1.1f;
    
    /**
     * Straight
     */
    public Straight(RigidBody rb, Face direction, Hostile hostile) {
        super(AnimationPath.PROJECTILE_1, rb, hostile);
        // TODO: change to attach point location
        origin = new Vector2f(owner.getRigidBody().getLocation());
        damage = owner.getDamage();

        switch(direction) {
            case RIGHT:
                rBody.setVelocity(SPEED, 0);
                break;
            case LEFT:
                rBody.setVelocity(-SPEED, 0);
                break;
            case UP:
                rBody.setVelocity(0, -SPEED);
                break;
            case DOWN:
                rBody.setVelocity(0, SPEED);
                break;
            case NONE:
                disable();
                break;
        }
    }
    
    @Override
    public void update(long time) {
        rBody.move(time);
        
        // if shot off the screen, remove
        if(owner != null && Collisions.findDistance(rBody, owner.getRigidBody()) > MAX_RANGE) {
            collide();
        } else if(owner == null && Collisions.findDistance(rBody, origin) > MAX_RANGE) {
            collide();
        }
    }
}
