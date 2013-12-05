package game.entities.npcs;

import java.util.Random;
import engine.physics.Collisions;
import engine.physics.RigidBody;
import game.GameEngine;
import game.entities.EntityFactory;
import game.enums.ProjectileType;
import game.environment.Room;

/*
* Classname:            WoodManEntity.java
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
public class Woodman extends Hostile {
    private String imageArray[] = {"\\animations\\woodman\\woodman1.gif","\\animations\\woodman\\woodman2.gif"};
    private String imageArrayRight[] = {"\\animations\\woodman\\woodman1Right.gif","\\animations\\woodman\\woodman2Right.gif"};
    private String dieArray[] = {"\\animations\\woodman\\woodmanDeath1.gif","\\animations\\woodman\\woodmanDeath2.gif","\\animations\\woodman\\woodmanDeath3.gif","\\animations\\woodman\\damage3.gif","\\animations\\woodman\\damage4.gif"};
    private String dieArrayRight[] = {"\\animations\\woodman\\woodmanDeathRight1.gif","\\animations\\woodman\\woodmanDeathRight2.gif","\\animations\\woodman\\woodmanDeathRight3.gif","\\animations\\woodman\\damage3.gif","\\animations\\woodman\\damage4.gif"};
    private int imageIndex = 0;
    private boolean isJump = false;
    private boolean facingRight = true;
    private boolean isFire = false;
    private int shotTimer = 0;
    private float lastShotTime;
    private static final int MAX_HEALTH = 20;
    private static final int COLLISION_DAMAGE = 5;
    private static final int AGGRO_RANGE = 192;
    private static final int MAX_PROJECTILES = 1;
    private int isDying = 0;
    private long currentTime = 0;
    private Player target = null;
    
    public Woodman(String file, RigidBody rb, Room room) {
        super(file, rb, room);
        imageIndex = imageIndex + 10; 
        image = imageArray[0];
        lastShotTime = 0;
        setHealthPoints(MAX_HEALTH);
        isDead = false;
        damage = COLLISION_DAMAGE;
        range = AGGRO_RANGE;
    }
    
    /*public void nextAnimation(String arrayName, int frames) {  
        if(arrayName.equals("spawnArray") && spawned == false){
            imageIndex = imageIndex + 20; 
            if(imageIndex >= frames*100) {
                imageIndex = 0;
            }
            int test = imageIndex/100;
            image = imageArray[test];
            if(image.equals("standingRight.gif")){
                spawned = true;
            }
        }
    }*/
    
    /*public void jump(){
        imageIndex = imageIndex + 10; 
        image = "shieldguyJump.gif";
    }*/

    public void setIsJump(boolean isJump) {
        this.isJump = isJump;
    }

    public boolean getIsJump() {
        return isJump;
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
    }
    
    public void deathAnimate() {
            isDying = 1;
            currentTime = GameEngine.getTime();
    }
    
    public void movements(long elapsedTime) {
        if(!isDead) {
            // find closest player
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
                
                if(facingRight) {
                    rBody.setVelocity(1, 0);
                }
                else {
                    rBody.setVelocity(-1, 0);
                }
                rBody.move(elapsedTime);

                Random generator = new Random( GameEngine.getTime() );
                int rand = generator.nextInt() % 100;
                if ((rand >= 80 && numProjectiles < MAX_PROJECTILES) || GameEngine.getTime() - lastShotTime > 2000) {
                    if(GameEngine.getTime() - lastShotTime > 200){
                        fire(facingRight);
                    }
                    isFire = true;
                    shotTimer = 0;
                }
            }
            else {
                rBody.setVelocity(0, 0);
            }

            imageIndex = imageIndex + 10;
            if(imageIndex >= 200) {
                imageIndex = 0;
            }
            if(facingRight && shotTimer >= 15) {
                image = imageArrayRight[imageIndex/100];     
            } else if(shotTimer >= 15) {
                image = imageArray[imageIndex/100];
            }
            
            if(isFire){
                isFire = false;
                if(facingRight) {
                    image = "\\animations\\woodman\\woodman4Right.gif";
                }
                else if(!facingRight) {
                    image = "\\animations\\woodman\\woodman4.gif";
                }
            }
            shotTimer++;
        }
        else {
            long seconds = GameEngine.getTime() - currentTime;
            if(seconds > (1333/6)){
                isDying++;
                currentTime = GameEngine.getTime();
            }
            imageIndex = imageIndex + 10; 
            if(imageIndex >= 400) {
                imageIndex = 0;
            }
            if(facingRight) {
                image = dieArrayRight[imageIndex/100];
            }
            else if(!facingRight) {
                image = dieArray[imageIndex/100];
            }
            
            if(isDying >= 4){
                disable();
                //game.setWin(true);
                // WIN
            }
        }
    }
}
