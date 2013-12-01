package game.entities.projectiles;

import engine.Vector2f;
import engine.physics.Collisions;
import engine.physics.RigidBody;
import game.entities.Entity;
import game.entities.environment.Obstacle;
import game.entities.environment.Pit;
import game.entities.npcs.Hostile;
import game.entities.npcs.Player;
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
    protected Hostile owner;

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
        owner = hostile;
        owner.addProjectile();
        origin = new Vector2f(owner.getRigidBody().getLocation());
        damage = owner.getDamage();
        MAX_RANGE = owner.getRange() < MIN_RANGE ? MIN_RANGE : owner.getRange();
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
        owner.removeProjectile();
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
        } else {
            disable();
            enemy.takeDamage(damage);
            //GameEngine.playSound(game.sound_hit_player);
        }
    }
    
    public void collide(Obstacle obstacle) {
        if(!(obstacle instanceof Pit)) {
            disable();
            //GameEngine.playSound(game.sound_hit_environ);
        }
    }
    
    public void collide() {
        disable();
        //GameEngine.playSound(game.sound_hit_environ);
    }
    
    public boolean dangerousTo(Hostile hostile) {
        if(owner.equals(hostile)) return false;
        else return true;
    }
    
    public boolean ownedByPlayer() {
        if(owner instanceof Player) {
            return true;
        }
        return false;
    }
}
