package game.entities.projectiles;

import engine.physics.RigidBody;
import game.entities.Entity;
import game.entities.EntityFactory;
import game.entities.environment.Obstacle;
import game.entities.environment.Pit;
import game.entities.npcs.Hostile;
import game.entities.npcs.Player;
import game.enums.AnimationPath;
import game.enums.EffectType;
import game.enums.Sound;

/*
* Classname:            Projectile.java
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
public abstract class Projectile extends Entity {
    protected static final int MIN_RANGE = 100;
    protected final int MAX_RANGE;
    protected int damage;
    protected Hostile owner;
    
    /**
     * Projectile
     */
    public Projectile(AnimationPath ap, RigidBody rb, Hostile hostile) {
        super(ap, rb);
        owner = hostile;
        owner.addProjectile();
        MAX_RANGE = owner.getAttackRange() < MIN_RANGE ? MIN_RANGE : owner.getAttackRange();
        owner.getRoom().addSound(Sound.SHOT);
    }
    
    @Override
    public void disable() {
        if(isEnabled()) {
            super.disable();
            owner.removeProjectile();
        }
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
            collide();
            enemy.takeDamage(owner, damage);
        }
    }
    
    public void collide(Obstacle obstacle) {
        if(!(obstacle instanceof Pit)) {
            if (!isEnabled()) {
                return;
            } else {
                collide();
            }
        }
    }
    
    public void collide() {
        if(!isEnabled()) {
            disable();
        } else {
            disable();
            if(animPath == AnimationPath.PROJECTILE_2) {
                owner.getRoom().addEffect(EntityFactory.createEffect(EffectType.PROJECTILE_2, this));
            } else {
                owner.getRoom().addEffect(EntityFactory.createEffect(EffectType.PROJECTILE_1, this));
            }
            //GameEngine.playSound(game.sound_hit_environ);
            owner.getRoom().addSound(Sound.HIT);
        }
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
