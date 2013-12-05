package game.entities.npcs;

import engine.Vector2f;
import engine.physics.RigidBody;
import game.entities.EntityFactory;
import game.enums.AnimationPath;
import game.enums.AnimationState;
import game.enums.Face;
import game.enums.ProjectileType;
import game.environment.Room;

/*
* Classname:            FlyingBag.java
*
* Version information:  1.0
*
* Date:                 12/4/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

/**
 * Flying Bag
 */
public class FlyingBag extends Hostile {
    private static final int MAX_HEALTH = 100;
    private static final int COLLISION_DAMAGE = 20;
    private static final int AGGRO_RANGE = 128;
    private static final long MAX_ACTION_TIME = 1000;
    private static final long ATTACK_INTERVAL = 1000;
    private static final long MAX_ATTACK_INTERVAL = 3000;
    private static final float SPEED = 0.8f;
    private long attackTime = 0;
    private long idleTime = 0;
    private long moveTime = 0;
    
    public FlyingBag(RigidBody rb, Room room, Face face) {
        super(AnimationPath.FLYBAG, rb, room, face);
        health = MAX_HEALTH;
        damage = COLLISION_DAMAGE;
        range = AGGRO_RANGE;
        flying = true;
    }
    
    /**
     * fire: shoot a bullet
     */
    public void fire() {
        //GameEngine.playSound(game.sound_shot);
        room.addProjectile(EntityFactory.createProjectile(rBody.getLocation(), null, Face.UP, this, ProjectileType.DIAGONAL));
        room.addProjectile(EntityFactory.createProjectile(rBody.getLocation(), null, Face.DOWN, this, ProjectileType.DIAGONAL));
        room.addProjectile(EntityFactory.createProjectile(rBody.getLocation(), null, Face.LEFT, this, ProjectileType.DIAGONAL));
        room.addProjectile(EntityFactory.createProjectile(rBody.getLocation(), null, Face.RIGHT, this, ProjectileType.DIAGONAL));
    }
    
    @Override
    public void update(long elapsedTime) {
        if(dead) {
            disable();
        } else {
            determineActions(elapsedTime);   
        }
    }
    
    private void determineActions(long elapsedTime) {
        if(animState.equals(AnimationState.IDLE)) {
            idleTime += elapsedTime;
            attackTime += elapsedTime;
            // been idle for too long, so attempt to move
            if(idleTime >= MAX_ACTION_TIME) {
                determineDirection();
                idleTime = 0;
                animState = AnimationState.RUN;
            }
            // minimum attack rate
            if(attackTime >= ATTACK_INTERVAL) {
                if(Math.random() > 0.7) {
                    fire();
                    attackTime = 0;
                }
            }
            // maximum attack rate
            if(attackTime >= MAX_ATTACK_INTERVAL) {
                fire();
                attackTime = 0;
            }
        } else if(animState.equals(AnimationState.RUN)) {
            moveTime += elapsedTime;
            // been moving for too long, so attempt o go idle
            if(moveTime >= MAX_ACTION_TIME) {
                if(Math.random() > 0.5f) {
                    animState = AnimationState.IDLE;
                }
                moveTime = 0;
            }
            rBody.move(elapsedTime);
        }
    }
    
    private void determineDirection() {
        // choose random direction to move in
        Face newFace = Face.randomAnyFace();
        Vector2f direction = new Vector2f();
        switch(newFace) {
            case RIGHT:
                facing = Face.RIGHT;
                direction.x = SPEED;
                break;
            case LEFT:
                facing = Face.LEFT;
                direction.x = -SPEED;
                break;
            case UP:
                facing = Face.UP;
                direction.y = -SPEED;
                break;
            case DOWN:
                facing = Face.DOWN;
                direction.y = SPEED;
                break;
            case NONE:
                animState = AnimationState.IDLE;
                break;
        }
        rBody.setVelocity(direction);
    }
}
