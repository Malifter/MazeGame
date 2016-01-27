package game.entities.npcs;

import engine.Vector2f;
import engine.physics.Collisions;
import engine.physics.RigidBody;
import game.entities.Entity;
import game.enums.AnimationPath;
import game.enums.AnimationState;
import game.enums.Face;
import game.environment.Room;

/*
* Classname:            Chaser.java
*
* Version information:  1.0
*
* Date:                 12/4/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

/**
 * Chaser
 */
public class Chaser extends Hostile {
    private static final int MAX_HEALTH = 100;
    private static final int COLLISION_DAMAGE = 20;
    private static final int AGGRO_RANGE = 55;//65
    private static final int MAX_OUT_OF_RANGE_TIME = 2000;
    private static final float SPEED = 0.4f;
    private int outOfRangeTime = 0;
    private Player target = null;
    
    public Chaser(RigidBody rb, Room room, Face face) {
        super(AnimationPath.CHASER, rb, room, face);
        health = MAX_HEALTH;
        damage = COLLISION_DAMAGE;
        attackRange = 0;
        aggroRange = AGGRO_RANGE;
        flying = true;
    }
    
    @Override
    public void update(long elapsedTime) {
        if(dead) {
            disable();
        } else {
            determineActions(elapsedTime);   
        }
    }
    
    @Override
    public void takeDamage(Entity source, int d) {
        super.takeDamage(source, d);
        if(target == null && source instanceof Player) {
            target = (Player) source;
        }
        outOfRangeTime = 0;
    }
    
    // TODO: Make it so that the chaser can aggro a player who shoots them from out of range.
    private void determineActions(long elapsedTime) {
        // find closest player
        if(target != null && target.getRoom() != room) {
            target = null;
            outOfRangeTime = 0;
        }
        if(target == null) {
            float minDist = Float.MAX_VALUE;
            for(Player p: room.getPlayers()) {
                float dist = Collisions.findDistance(p.getRigidBody(), rBody);
                if(dist < minDist) {
                    minDist = dist;
                    if(minDist <= AGGRO_RANGE) {
                        target = p;
                        outOfRangeTime = 0;
                    }
                }
            }
        }
        if(target == null) return;
        
        // chase if target found
        if(outOfRangeTime < MAX_OUT_OF_RANGE_TIME) {
            if(Collisions.findDistance(target.getRigidBody(), rBody) <= AGGRO_RANGE) {
                outOfRangeTime = 0;
            } else {
                outOfRangeTime += elapsedTime;
            }
            // find facing direction
            Vector2f direction = target.getRigidBody().getLocation().sub(rBody.getLocation());
            if(Math.abs(direction.y) > Math.abs(direction.x)) {
                // Up or Down
                if(direction.y > 0) {
                    facing = Face.DOWN;
                } else {
                    facing = Face.UP;
                }
            } else {
                // Right or Left
                if(direction.x > 0) {
                    facing = Face.RIGHT;
                } else {
                    facing = Face.LEFT;
                }
            }
            rBody.setVelocity(direction.norm().mult(SPEED));
            rBody.move(elapsedTime);
            animState = AnimationState.RUN;
        } else {
            target = null;
            outOfRangeTime = 0;
            animState = AnimationState.IDLE;
        }
    }
}
