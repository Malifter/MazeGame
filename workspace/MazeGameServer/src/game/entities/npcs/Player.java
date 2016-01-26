package game.entities.npcs;

import java.util.ArrayList;

import engine.Vector2f;
import engine.physics.RigidBody;
import game.Inventory;
import game.MazeGameServer;
import game.enums.*;
import game.environment.Interior;
import game.environment.Room;
import game.entities.Entity;
import game.entities.EntityFactory;

/*
* Classname:            Player.java
*
* Version information:  1.0
*
* Date:                 10/30/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

/**
 * Player:
 */
public class Player extends Hostile {
    private static final int MAX_HEALTH = 100;//100;
    private static final int BLASTER_DAMAGE = 20;
    private static final int ATTACK_RANGE = 168;
    private static final float SPEED = 0.8f;
    //private static final int MAX_PROJECTILES = 5;
    private static final long MAX_INVUNERABLE_TIME = 1000;
    private static final long SHOOT_INTERVAL = 300;
    private long shootTime = 0;
    private boolean shootPressed = false;
    private float speedRatio = 0.25f; // between 0 and 1
    private int playerID = 0;
    private int lives = 3;
    private boolean shielded = false;
    private long invulnerableTime = 0;
    private boolean invulnerable = true;
    private Hostage follower = null;
    private Inventory inventory = new Inventory();
    private boolean sliding = false;
    
    public Player(RigidBody rb, int playerID, Room room, Face dir) {
        super(playerID == 0 ? AnimationPath.PLAYER_1 : playerID == 1 ? AnimationPath.PLAYER_2 :
            playerID == 2 ? AnimationPath.PLAYER_3 : AnimationPath.PLAYER_4, rb, room, dir);
        this.playerID = playerID;
        attackRange = ATTACK_RANGE;
        damage = BLASTER_DAMAGE;
        health = MAX_HEALTH;
    }
    
    public Inventory getInventory(){
        return inventory;
    }
    
    public int getPlayerID() {
        return playerID;
    }
    
    public void reset() {
        health = MAX_HEALTH;
        facing = Face.DOWN;
        invulnerable = true;
        invulnerableTime = 0;
        dead = false;
        shielded = false;
        follower = null;
        speedRatio = 0.25f;
        sliding = false;
        shootTime = 0;
        shootPressed = false;
        enable();
        rBody.setLocation(MazeGameServer.level.getExterior().getPlayerSpawns().get(playerID));
    }
    
    /**
     * fire: shoot a bullet
     */
    public void fire() {
        //GameEngine.playSound(game.sound_shot);
        room.addProjectile(EntityFactory.createProjectile(rBody.getLocation(), null, facing, this, ProjectileType.STRAIGHT));
    }
    
    @Override
    public void update(long elapsedTime) {
        if(dead) {
            disable();
        }
        else {
            faceMouse(MazeGameServer.mice.get(playerID));
            handleItemInputs(MazeGameServer.inputs.get(playerID));
            handleMoveInputs(MazeGameServer.inputs.get(playerID), elapsedTime);
            handleFireInputs(MazeGameServer.inputs.get(playerID), elapsedTime);
            
            if(invulnerable) {
                invulnerableTime += elapsedTime;
                if(invulnerableTime > MAX_INVUNERABLE_TIME) {
                    invulnerableTime = 0;
                    invulnerable = false;
                }
            }
        }
    }
    
    public void faceMouse(Vector2f loc) {
        if(room instanceof Interior) {
            Interior interior = (Interior) room;
            loc.addEq(new Vector2f(interior.getCenter()));
            loc.subEq(rBody.getLocation());
        }
        if(Math.abs(loc.y) > Math.abs(loc.x)) {
            // Up or Down
            if(loc.y > 0) {
                facing = Face.DOWN;
            } else {
                facing = Face.UP;
            }
        } else {
            // Right or Left
            if(loc.x > 0) {
                facing = Face.RIGHT;
            } else {
                facing = Face.LEFT;
            }
        }
    }
    
    @Override
    public void takeDamage(Entity source, int d) {
        if (!invulnerable) {
            if (shielded) {
                //GameEngine.playSound(game.sound_hit_shield);
                shielded = false;
            }
            else {
                //GameEngine.playSound(game.sound_hit);
            	setHealth(getHealth()-d);
                invulnerable = true;
            }
        }
        if(getHealth() == 0 && !dead) {
            //GameEngine.playSound(game.sound_dead);
            dead = true;
        }
    }
    
    public void handleMoveInputs(ArrayList<Boolean> inputs, long elapsedTime) {
        float moveX = 0.0f;
        float moveY = 0.0f;
        float speed = SPEED * speedRatio;
        if (inputs.get(Pressed.RIGHT.getValue())) {
            moveX += speed;
        }
        if (inputs.get(Pressed.LEFT.getValue())) {
            moveX -= speed;
        }
        if (inputs.get(Pressed.UP.getValue())) {
            moveY -= speed;
        }
        if (inputs.get(Pressed.DOWN.getValue())) {
            moveY += speed;
        }
        
        if(moveX != 0 || moveY != 0) {
            if(speedRatio < 1.0f) speedRatio += 0.15f * speedRatio;//Math.min((float)Math.sqrt((double) speedRatio), 1.0f);
            if(!sliding) sliding = true;
            animState = AnimationState.RUN;
        }
        else if(sliding) {
            if(speedRatio > 0.0f) speedRatio -= speedRatio * 0.1f;//Math.max(1.0f-(float)Math.sqrt((double) 1.0f-speedRatio), 0.0f);
            if(speedRatio <= 0.05f) {
                speedRatio = 0.0f;
                sliding = false;
            }
            speed = SPEED * speedRatio;
            if(rBody.getDelta().x > 0.0f) moveX += speed;
            else if(rBody.getDelta().x < 0.0f) moveX -= speed;
            if(rBody.getDelta().y > 0.0f) moveY += speed;
            else if(rBody.getDelta().y < 0.0f) moveY -= speed;
            if(!sliding) speedRatio = 0.25f;
            animState = AnimationState.IDLE;
        }
        
        // TODO: in animator decide if we want the running animation to complete it's cycle before switching to idle
        // Also we need to decide which state changes should affect animations instantly. For example if you're running
        // And then decide to shoot, then the shooting+running animation should play at the same frame as the
        // running animation by itself. Meaning if it was on frame 2, then we should use that frame when switching
        // For Idle, we should finish the Run animation then let him slide to a stop in idle etc.
        // Other animations should be instant, for instance jumping should override the current frame
        
        rBody.move(moveX, moveY, elapsedTime);
    }
    
    public void handleFireInputs(ArrayList<Boolean> inputs, long elapsedTime) {
        shootTime += elapsedTime;
        if (inputs.get(Pressed.FIRE.getValue()) && !shootPressed) {
            if(/*numProjectiles < MAX_PROJECTILES &&*/ shootTime > SHOOT_INTERVAL){
                fire();
                shootTime = 0;
            }
            shootPressed = false;
        } else if(!inputs.get(Pressed.FIRE.getValue())) {
            shootPressed = false;
        }
    }
    
    private boolean pressedUse=false, pressedBackward=false, pressedForward = false;
    public void handleItemInputs(ArrayList<Boolean> inputs) {
            if(inputs.get(Pressed.SELECT_FORWARD.getValue()) && !pressedForward){
                pressedForward = true;
                this.getInventory().selectNextItem();
            }else if(!inputs.get(Pressed.SELECT_FORWARD.getValue())) {
                pressedForward = false;
            }
            
            
            if(inputs.get(Pressed.SELECT_BACKWARD.getValue()) && !pressedBackward){
                pressedBackward = true;
                this.getInventory().selectPrevItem();
            }else if(!inputs.get(Pressed.SELECT_BACKWARD.getValue())) {
                pressedBackward = false;
            }
            
            if(inputs.get(Pressed.USE_ITEM.getValue()) && !pressedUse){
                pressedUse = true;
                this.getInventory().useSelectedItem(this, room);
            } else if(!inputs.get(Pressed.USE_ITEM.getValue())) {
                pressedUse = false;
            }
    }
    
    public int getLives() {
        return lives;
    }
    
    public void addLife() {
        lives++;
    }
    
    public void removeLife() {
        lives--;
    }
    
    public void setFollower(Hostage hostage) {
        follower = hostage;
    }
    
    public Hostage getFollower() {
        return follower;
    }
    
    public boolean hasFollower() {
        if(follower != null) {
            return true;
        }
        return false;
    }
    
    public boolean getShield(){
        return shielded;
    }
    
    public void addShield(){
        this.shielded = true;
    }
}
