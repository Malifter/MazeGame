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
    private static final int AGGRO_RANGE = 65;
    private static final float SPEED = 0.5f;
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
    }
    
    // TODO: Make it so that the chaser can aggro a player who shoots them from out of range.
    private void determineActions(long elapsedTime) {
        // find closest player
        if(target != null && target.getRoom() != room) {
            target = null;
        }
        if(target == null) {
            float minDist = Float.MAX_VALUE;
            for(Player p: room.getPlayers()) {
                float dist = Collisions.findDistance(p.getRigidBody(), rBody);
                if(dist < minDist) {
                    minDist = dist;
                    target = p;
                }
            }
        }
        if(target == null) return;
        
        // chase if target found
        if(Collisions.findDistance(rBody, target.getRigidBody()) <= AGGRO_RANGE) {
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
            animState = AnimationState.IDLE;
        }
    }
}
