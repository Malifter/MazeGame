package game.entities.projectiles;

import engine.Vector2f;
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
    protected static final long serialVersionUID = -8262594565133507674L;
    protected static final int MIN_RANGE = 168;
    protected final int MAX_RANGE;
    protected int damage;
    protected Hostile source;

    /** The vertical speed at which the players shot moves */
    protected float moveSpeedX = 2.0f;
    
    //hardcoded for cannon
    protected float moveSpeedY = 2.0f;
    
    protected final static String projectilePath = "animations/projectiles/";
    
    Vector2f origin;
    
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
    public Projectile(String img, RigidBody rb, Face direction, Hostile hostile) {
        super(projectilePath+img, rb);
        source = hostile;
        source.addProjectile();
        origin = new Vector2f(source.getRigidBody().getLocation());
        damage = source.getDamage();
        MAX_RANGE = source.getRange() < MIN_RANGE ? MIN_RANGE : source.getRange();
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
    
    @Override
    public void disable() {
        super.disable();
        source.removeProjectile();
    }
    
    /**
     * Notification that this shot has collided with another
     * entity
     * 
     * @param other
     *            The other entity with which we've collided
     */
    public void collide(Hostile enemy) {
        if (!isEnabled()) {
            return;
        } else if(enemy == null) {
            disable();
          //GameEngine.playSound(game.sound_hit_environ);
        } else {
            disable();
            enemy.takeDamage(damage);
            //GameEngine.playSound(game.sound_hit_player);
        }
    }
    
    public void enableY() {
        rBody.setVelocity(rBody.getVelocity().x, -moveSpeedY);
    }
    
    public void disableY() {
        rBody.setVelocity(rBody.getVelocity().x, 0);
    }
    
    public boolean dangerousTo(Hostile hostile) {
        if(source.equals(hostile)) return false;
        else return true;
    }
}
