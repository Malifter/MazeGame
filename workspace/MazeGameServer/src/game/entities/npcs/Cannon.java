package game.entities.npcs;

/*
* Classname:            CannonEntity.java
*
* Version information:  1.0
*
* Date:                 10/30/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

import engine.physics.Collisions;
import engine.physics.RigidBody;
import game.GameEngine;
import game.MazeGameServer;
import game.entities.EntityFactory;
import game.enums.ProjectileType;
import game.environment.Room;


/**
 * ShieldGuyEntity: Shield Guy entity is used for Shield Guy game.
 * Each Shield Guy entity has a direction and direction time for moving the
 * entity.
 */
public class Cannon extends Hostile {
    MazeGameServer game;
    private String imageArray[] = {"\\animations\\cannon\\cannon1floor.gif","\\animations\\cannon\\cannon3floor.gif"};
    private String imageArrayRight[] = {"\\animations\\cannon\\cannon1floorRight.gif","\\animations\\cannon\\cannon3floorRight.gif"};
    private String dieArray[] = {"\\animations\\cannon\\cannon1floorDie.gif","\\animations\\cannon\\cannon2floorDie.gif","\\animations\\cannon\\damage3.gif"};
    private String dieArrayRight[] = {"\\animations\\cannon\\cannon1floorRightDie.gif","\\animations\\cannon\\cannon2floorRightDie.gif","\\animations\\cannon\\damage3.gif"};
    private int imageIndex = 0;
    private int numBullets;
    private boolean facingRight = true;
    private int shotTimer = 0;
    private float lastShotTime;
    private static final int MAX_HEALTH = 5;
    //private static final int BLASTER_DAMAGE = 2;
    private static final int COLLISION_DAMAGE = 5;
    private static final int AGGRO_RANGE = 168;
    private static final int AGGRO_RANGE_SQUARED = AGGRO_RANGE * AGGRO_RANGE;
    private Room room;
    private int isDying = 0;
    private long currentTime = 0;
    private boolean angleShot = false;
    private Player target = null;
    
    public Cannon(String file, RigidBody rb, Room room) {
        super(file, rb);
        imageIndex = imageIndex + 10; 
        image = imageArray[0];
        lastShotTime = 0;
        setHealthPoints(MAX_HEALTH);
        isDead = false;
        damage = COLLISION_DAMAGE;
        this.room = room;
        numBullets = 0;
        range = AGGRO_RANGE;
    }
    
    /**
     * fire: shoot a bullet
     */
    public void fire(boolean isRight) {
        //GameEngine.playSound(game.sound_shot);
        shots.add(EntityFactory.createProjectile(rBody.getLocation(), target.getRigidBody().getLocation(), this, ProjectileType.STRAIGHT));
        if(angleShot) {
            if (isRight) image = imageArrayRight[1]; 
            else image = imageArray[1];
            shots.get(shots.size()-1).enableY();
        } else {
            if (isRight) image = imageArrayRight[0]; 
            else image = imageArray[0];
            shots.get(shots.size()-1).disableY();
        }
        angleShot = !angleShot;
        lastShotTime = GameEngine.getTime();
    }
    
    @Override
    public void takeDamage(int d) {
        setHealthPoints(getHealthPoints()-d);
        //GameEngine.playSound(((MazeGameServer)game).sound_hit);
        if(getHealthPoints() == 0 && isDying == 0) {
            isDead = true;
            imageIndex = 0;
            deathAnimate();
        }
    }
    
    @Override
    public void update(long time) {
        movements(time);
        
        int i = 0;
        while(i < shots.size()) {
            if(shots.get(i).isEnabled()) {
                shots.get(i).update(time);
                i++;
            }
            else {
                shots.remove(i);
                numBullets--;
            }
        }
    }
    
    public void deathAnimate() {
            isDying = 1;
            currentTime = GameEngine.getTime();
    }
    
    public void movements(long time) {
        if(!isDead) {
            target = null;
            float minDist = Float.MAX_VALUE;
            for(Player p: room.getPlayers()) {
                float dist = Collisions.findDistance(p.getRigidBody(), rBody);
                if(dist < minDist) {
                    minDist = dist;
                    target = p;
                }
            }
            if(target == null) return;
            if(Collisions.findDistance(rBody, target.getRigidBody()) <= AGGRO_RANGE_SQUARED) {
                if(target.getRigidBody().getLocation().x - rBody.getLocation().x > 0) {
                    facingRight = true;
                }
                else {
                    facingRight = false;
                }
                if(numBullets <= 0 && GameEngine.getTime() - lastShotTime > 500){
                    fire(facingRight);
                    numBullets++;
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
                image = dieArrayRight[imageIndex/100];
            }
            else if(!facingRight) {
                image = dieArray[imageIndex/100];
            }
            
            if(isDying >= 3){
                disable();
            }
        }
    }
}
