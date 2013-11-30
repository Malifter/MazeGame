package game.entities.projectiles;

import engine.Vertex2f;
import engine.physics.Collisions;
import engine.physics.RigidBody;
import game.entities.Entity;
import game.entities.npcs.Hostile;
import game.enums.Face;

/*
* Classname:            ShotEntity.java
*
* Version information:  1.0
*
* Date:                 10/30/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

/**
 * A projectile
 */
public class Projectile extends Entity {
    private static final long serialVersionUID = -8262594565133507674L;
    private static final int MIN_RANGE = 168;
    private final int MAX_RANGE;
    private int damage;

    /** The vertical speed at which the players shot moves */
    private float moveSpeedX = 2.0f;
    
    //hardcoded for cannon
    private float moveSpeedY = 2.0f;
    
    private final static String projectilePath = "animations/projectiles/";
    
    Vertex2f origin;
    
    /**
     * Create a new shot from the player
     * 
     * @param game
     *            The game in which the shot has been created
     * @param sprite
     *            The sprite representing this shot
     * @param x
     *            The initial x location of the shot
     * @param y
     *            The initial y location of the shot
     */
    public Projectile(String img, RigidBody rb, Face direction, int damage, int range) {
        super(projectilePath+img, rb);
        origin = new Vertex2f(rBody.getLocation());
        this.damage = damage;
        if(range < MIN_RANGE) {
            MAX_RANGE = MIN_RANGE*MIN_RANGE;
        }
        else {
            MAX_RANGE = range*range;
        }
        switch(direction) {
            case RIGHT:
                rBody.setVelocity(moveSpeedX, 0);
                break;
            case LEFT:
                rBody.setVelocity(-moveSpeedX, 0);
                break;
            case UP:
                rBody.setVelocity(0, -moveSpeedY);
                break;
            case DOWN:
                rBody.setVelocity(0, moveSpeedY);
                break;
        }
        if(image == null) image = projectilePath+"shot.gif";
    }
    
    @Override
    public void update(long time) {
        rBody.move(time);
        
        // if shot off the screen, remove
        if (Collisions.findDistance(rBody, origin) > MAX_RANGE) {
            disable();
        }
    }
    
    /**
     * Notification that this shot has collided with another
     * entity
     * 
     * @param other
     *            The other entity with which we've collided
     */
    public void bulletHit(Hostile enemy) {
        if (!isEnabled()) {
            return;
        }
        disable();
        enemy.takeDamage(damage);
        //GameEngine.playSound(game.sound_hit);
    }
    
    public void enableY() {
        rBody.setVelocity(rBody.getVelocity().x, -moveSpeedY);
    }
    
    public void disableY() {
        rBody.setVelocity(rBody.getVelocity().x, 0);
    }
}
