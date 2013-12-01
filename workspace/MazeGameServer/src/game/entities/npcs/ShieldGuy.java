package game.entities.npcs;

import java.util.Random;
import engine.physics.Collisions;
import engine.physics.RigidBody;
import game.GameEngine;
import game.entities.EntityFactory;
import game.enums.ProjectileType;
import game.environment.Room;

/*
* Classname:            ShieldGuyEntity.java
*
* Version information:  1.0
*
* Date:                 10/30/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

/**
 * ShieldGuyEntity: Shield Guy entity is used for Shield Guy game.
 * Each Shield Guy entity has a direction and direction time for moving the
 * entity.
 */
public class ShieldGuy extends Hostile {
    private final static String animationPath = "animations/shieldguy/";
    private String dieArray[] = {"shieldguyDie1.gif","shieldguyDie2.gif","damage3.gif"};
    private String dieArrayRight[] = {"shieldguyDieRight1.gif","shieldguyDieRight2.gif","damage3.gif"};
    private int imageIndex = 0;
    private boolean facingRight = true;
    private boolean isFire = false;
    private int shotTimer = 0;
    private float lastShotTime;
    private static final int MAX_HEALTH = 5;
    //private static final int BLASTER_DAMAGE = 3;
    private static final int COLLISION_DAMAGE = 5;
    private static final int AGGRO_RANGE = 128;
    private static final int MAX_PROJECTILES = 2;
    private boolean isBlocking = false;
    private float blockTimer = 0;
    private int isDying = 0;
    private long currentTime = 0;
    private Player target = null;
    
    public ShieldGuy(String file, RigidBody rb, Room room) {
        super(file, rb, room);
        imageIndex = imageIndex + 10; 
        image = animationPath+"shieldguy1.gif";
        lastShotTime = 0;
        setHealthPoints(MAX_HEALTH);
        isDead = false;
        damage = COLLISION_DAMAGE;
        range = AGGRO_RANGE;
    }
    
    /**
     * fire: shoot a bullet
     */
    public void fire(boolean isRight) {
        //GameEngine.playSound(game.sound_shot);
        room.addProjectile(EntityFactory.createProjectile(rBody.getLocation(), target.getRigidBody().getLocation(), this, ProjectileType.STRAIGHT));
        lastShotTime = GameEngine.getTime();
    }
    
    @Override
    public void takeDamage(int d) {
        //meaning not shielded up
        if(!isBlocking) {
            setHealthPoints(getHealthPoints()-d);
            //GameEngine.playSound(((MazeGameServer)game).sound_hit);
        } else {
            // BLOCKING SOUND
            //GameEngine.playSound(((MazeGameServer)game).sound_deflect);
        }
        if(getHealthPoints() == 0 && isDying == 0) {
            isDead = true;
            imageIndex = 0;
            deathAnimate();
        }
    }
    
    @Override
    public void update(long time) {
        movements(time);
    }
    
    public void deathAnimate() {
            isDying = 1;
            currentTime = GameEngine.getTime();
    }
    
    public void movements(long time) {
        if(!isDead) {
            target = null;
            float minDist = Float.MAX_VALUE; // tmp replace later
            for(Player p: room.getPlayers()) {
                float dist = Collisions.findDistance(p.getRigidBody(), rBody);
                if(dist < minDist) {
                    minDist = dist;
                    target = p;
                }
            }
            if(target == null) return;
            if(Collisions.findDistance(rBody, target.getRigidBody()) <= AGGRO_RANGE) {
                if(target.getRigidBody().getLocation().x - rBody.getLocation().x > 0) {
                    facingRight = true;
                }
                else {
                    facingRight = false;
                }
                Random generator = new Random( GameEngine.getTime() );
                int rand = generator.nextInt() % 100;
                if ((rand >= 80 && numProjectiles < MAX_PROJECTILES) || GameEngine.getTime() - lastShotTime > 2000) {
                    if(GameEngine.getTime() - lastShotTime > 200){
                        fire(facingRight);
                        blockTimer = GameEngine.getTime();
                        isBlocking = false;
                    }
                    isFire = true;
                    shotTimer = 0;
                }
                if(!isBlocking) {
                    if(GameEngine.getTime() - blockTimer > 800) {
                        isBlocking = true;
                        blockTimer = 0;
                    }
                }
            }
            else {
                isBlocking = true;
            }
            
            if(isBlocking){
                if(facingRight) {
                    image = animationPath+"shieldguyRight1.gif";
                }
                else if(!facingRight) {
                    image = animationPath+"shieldguy1.gif";
                }
            }
            else if(isFire && !isBlocking){
                isFire = false;
                if(facingRight) {
                    image = animationPath+"shieldguyRight3.gif";
                }
                else if(!facingRight) {
                    image = animationPath+"shieldguy3.gif";
                }
            }
            shotTimer++;
        }
        else {
            long seconds = GameEngine.getTime() - currentTime;
            if(seconds > (1000/6)){
                isDying++;
                currentTime = GameEngine.getTime();
            }
            imageIndex = imageIndex + 10; 
            if(imageIndex >= 300) {
                imageIndex = 0;
            }
            if(facingRight) {
                image = animationPath+dieArrayRight[imageIndex/100];
            }
            else if(!facingRight) {
                image = animationPath+dieArray[imageIndex/100];
            }
            
            if(isDying >= 3){
                disable();
            }
        }
    }
}
