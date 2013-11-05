package game;

/*
* Classname:            CannonEntity.java
*
* Version information:  1.0
*
* Date:                 10/30/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

import java.util.ArrayList;
import java.util.Random;

import engine.SerializedObject;
import engine.physics.Collisions;


/**
 * ShieldGuyEntity: Shield Guy entity is used for Shield Guy game.
 * Each Shield Guy entity has a direction and direction time for moving the
 * entity.
 */
public class CannonEntity extends Entity {
    MazeGameServer game;
    private float direction = 0;
    private long directionTime = 0;
    private String imageArray[] = {"\\enemies\\cannon1floor.gif","\\enemies\\cannon3floor.gif"};
    private String imageArrayRight[] = {"\\enemies\\cannon1floorRight.gif","\\enemies\\cannon3floorRight.gif"};
    private String dieArray[] = {"\\enemies\\cannon1floorDie.gif","\\enemies\\cannon2floorDie.gif","damage3.gif"};
    private String dieArrayRight[] = {"\\enemies\\cannon1floorRightDie.gif","\\enemies\\cannon2floorRightDie.gif","damage3.gif"};
    private int imageIndex = 0;
    private int numBullets;
    private boolean spawned = false;
    private boolean isShoot = false;
    private boolean facingRight = true;
    private boolean isFire = false;
    private int shotTimer = 0;
    private float lastShotTime;
    private static final int MAX_HEALTH = 5;
    private static final int BLASTER_DAMAGE = 2;
    private static final int COLLISION_DAMAGE = 5;
    private static final int AGGRO_RANGE = 168;
    private Room room;
    private int isDying = 0;
    private long currentTime = 0;
    private boolean angleShot = false;
    
    public CannonEntity(Game g, String file, int iX, int iY, int x, int y, int w, int h, Room room) {
        super(g, file, iX, iY, w, h);
        game = (MazeGameServer) g;
        imageIndex = imageIndex + 10; 
        image = imageArray[0];
        minX = x;
        minY = y;
        width = w;
        height = h;
        offsetX = Math.abs(imageX - minX);
        offsetY = Math.abs(imageY - minY);
        lastShotTime = 0;
        calculateBounds();
        setHealthPoints(MAX_HEALTH);
        isDead = false;
        damage = COLLISION_DAMAGE;
        this.room = room;
        numBullets = 0;
    }
    
    /**
     * fire: shoot a bullet
     */
    public void fire(boolean isRight) {
        //GameEngine.playSound(game.sound_shot);
        if(isRight && !angleShot) {
            image = imageArrayRight[0];
            shots.add(new ShotEntity(game, "\\enemies\\cannonShot.gif",(int) maxX,
                    (int) minY+8, Face.RIGHT, BLASTER_DAMAGE, AGGRO_RANGE));
            shots.get(shots.size()-1).disableY();
            angleShot = true;
        }
        else if(!isRight && !angleShot) {
            image = imageArray[0];
            shots.add(new ShotEntity(game, "\\enemies\\cannonShot.gif",(int) minX,
                    (int) minY+8, Face.LEFT, BLASTER_DAMAGE, AGGRO_RANGE));
            shots.get(shots.size()-1).disableY();
            angleShot = true;
        }
        else if(isRight && angleShot) {
            image = imageArrayRight[1];
            shots.add(new ShotEntity(game, "\\enemies\\cannonShot.gif",(int) maxX-12,
                    (int) minY, Face.RIGHT, BLASTER_DAMAGE, AGGRO_RANGE));
            shots.get(shots.size()-1).enableY();
            angleShot = false;
        }
        else if(!isRight && angleShot) {
            image = imageArray[1];
            shots.add(new ShotEntity(game, "\\enemies\\cannonShot.gif",(int) minX+4,
                    (int) minY, Face.LEFT, BLASTER_DAMAGE, AGGRO_RANGE));
            shots.get(shots.size()-1).enableY();
            angleShot = false;
        }
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
            if(shots.get(i).needsDelete()) {
                shots.remove(i);
                numBullets--;
            }
            else {
                shots.get(i).update(time);
                i++;
            }
        }
    }
    
    public void deathAnimate() {
            isDying = 1;
            currentTime = GameEngine.getTime();
    }
    
    public void movements(long time) {
        if(!isDead) {
            Entity player = null;
            float minDist = 99999; // tmp replace later
            for(Entity p: room.getPlayers()) {
                float dist = Collisions.findDistance(p, this);
                if(dist < minDist) {
                    minDist = dist;
                    player = p;
                }
            }
            if(player == null) return;
            if(Math.abs(player.getMidX()-midX) < AGGRO_RANGE && Math.abs(player.getMidY()-midY) < AGGRO_RANGE) {
                if(player.getMidX()-midX > 0) {
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
            else {
                if(player.getMidX()-midX > 0) {
                    facingRight = true;
                    image = "\\enemies\\cannon1floorRight.gif";
                }
                else {
                    facingRight = false;
                    image = "\\enemies\\cannon1floor.gif";
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
                remove = true;
            }
        }
    }
}
