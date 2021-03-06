package game.entities.npcs;

import java.util.ArrayList;

import engine.Vector2f;
import engine.physics.RigidBody;
import game.GameEngine;
import game.Inventory;
import game.MazeGameServer;
import game.enums.*;
import game.environment.Room;
import game.entities.EntityFactory;
import game.entities.items.Bomb;
import game.entities.items.Consumable;
import game.entities.items.Item;
import game.entities.items.NotConsumable;

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
 * Player: Mega Man entity is used for Mega Man game.
 * Each Mega Man entity has a direction and direction time for moving the
 * entity.
 */
public class Player extends Hostile {
    private final String animationPath = "animations/player/";
    private String imageArrayLeft[] = {"damage1.gif","damage2.gif","damage3.gif","damage4.gif"};
    private String imageArrayRight[] = {"damageRight1.gif","damageRight2.gif","damage3.gif","damage4.gif"};
    private String runningLeftArray[] = {"running1.gif","running2.gif","running3.gif","running4.gif"};
    private String runningRightArray[] = {"runningRight1.gif","runningRight2.gif","runningRight3.gif","runningRight4.gif"};
    private String runningUpArray [] = {"climbing1.gif","climbing2.gif","climbing1.gif","climbing2.gif"};
    private String runningDownArray [] = {"jumping1.gif", "jumpingRight1.gif","jumping1.gif", "jumpingRight1.gif"};
    private String spawnArray [] = {"spawn1.gif","spawn2.gif","spawn3.gif","spawn4.gif","spawn5.gif","standingRight.gif"};
    private String runningLeftShootArray [] = {"runshoot1.gif","runshoot2.gif","runshoot3.gif","runshoot4.gif"};
    private String runningRightShootArray [] = {"runshootRight1.gif","runshootRight2.gif","runshootRight3.gif","runshootRight4.gif"};
    private String deathArray [] = {"blackMM.gif","standingRight.gif","spawn3.gif","spawn2.gif","spawn1.gif"};
    private int isDying = 0;
    private int imageIndex = 0;
    private boolean spawned = false;
    private boolean isShooting = false;
    private boolean isFire = false;
    private boolean isVuln = true;
    private int flash = 0;
    private int shotTimer = 0;
    private float lastShotTime;
    private static final int MAX_HEALTH = 28;
    private static final int BLASTER_DAMAGE = 1;
    private static final int ATTACK_RANGE = 168;
    private static final float SPEED = 1.3f;
    private static final int MAX_PROJECTILES = 3;
    private float speedRatio = 0.0f; // between 0 and 1
    private int isDamage = 0;
    private long currentTime = 0;
    // SPAWN LOCATION NEEDS EITHER INSTANCE OF SPAWNLOC OR LOCATION IN EXTERIOR
    private int imgArraySize = 0;
    private int playerID = 0;
    private Inventory inventory = new Inventory();
    private int lives;
    private Hostage follower = null;
    private boolean hasShield = false;
    
    public Player(RigidBody rb, int playerID, Room room, Face dir) {
        super(playerID == 0 ? AnimationPath.PLAYER_1 : playerID == 1 ? AnimationPath.PLAYER_2 :
            playerID == 2 ? AnimationPath.PLAYER_3 : AnimationPath.PLAYER_4, rb, room, dir);
        imageIndex = imageIndex + 5; 
        this.image = animationPath+spawnArray[0];
        imgArraySize = spawnArray.length;
        lastShotTime = 0;
        setHealthPoints(MAX_HEALTH);
        this.lives = 3;
        this.playerID = playerID;
        range = ATTACK_RANGE;
        damage = BLASTER_DAMAGE;
    }
    
    public Inventory getInventory(){
        return inventory;
    }
    
    public int getPlayerID() {
        return playerID;
    }
    
    public void reset() {
        // SET SPAWN LOCATION
        imageIndex = imageIndex + 5; 
        image = animationPath+spawnArray[0];
        imgArraySize = spawnArray.length;
        lastShotTime = 0;
        setHealthPoints(MAX_HEALTH);
        spawned = false;
        isShooting = false;
        facing = Face.RIGHT;
        isFire = false;
        isVuln = true;
        flash = 0;
        shotTimer = 0;
        currentTime = 0;
        enable();
        isDying = 0;
        isDead = false;
    }
    
    public void spawn(int frames) {  
        if(spawned == false){
            imageIndex = imageIndex + 20; 
            if(imageIndex >= frames*100) {
                imageIndex = 0;
            }
            int test = imageIndex/100;
            image = animationPath+spawnArray[test];
            if(image.equals(animationPath+"spawn5.gif")){
                spawned = true;
            }
        }
    }
    
    public void animateMovement(Move direction){
        imageIndex = imageIndex + 10; 
        if(imageIndex >= imgArraySize*100 && (isFire || shotTimer >= 10)) {
            imageIndex = 0;
        }
        if(!isFire && shotTimer >= 10){
            switch (direction) {
                case RIGHT:
                    image = animationPath+runningRightArray[imageIndex/100];
                    imgArraySize = runningRightArray.length;
                    break;
                case LEFT:
                    image = animationPath+runningLeftArray[imageIndex/100];
                    imgArraySize = runningLeftArray.length;
                    break;
                case UP:
                    image = animationPath+runningUpArray[imageIndex/100];
                    imgArraySize = runningUpArray.length;
                    break;
                case DOWN:
                    image = animationPath+runningDownArray[imageIndex/100];
                    imgArraySize = runningDownArray.length;
                    break;
            }
        }
        else if(isFire) {
            switch (direction) {
                case RIGHT:
                    image = animationPath+runningRightShootArray[imageIndex/100];
                    imgArraySize = runningRightShootArray.length;
                    break;
                case LEFT:
                    image = animationPath+runningLeftShootArray[imageIndex/100];
                    imgArraySize = runningLeftShootArray.length;
                    break;
                case UP:
                    image = animationPath+runningUpArray[imageIndex/100];
                    imgArraySize = runningUpArray.length;
                    break;
                case DOWN:
                    image = animationPath+runningDownArray[imageIndex/100];
                    imgArraySize = runningDownArray.length;
                    break;
            }
        }
    }
    
    /**
     * fire: shoot a bullet
     */
    public void fire(Face direction) {
        //GameEngine.playSound(game.sound_shot);
        
        // the purpose of this switch statement only modifies offset if needed, so in the end duplicate code.
        Vector2f target;
        switch (direction) {
            case RIGHT:
                target = rBody.getLocation().add(new Vector2f(1, 0));
                break;
            case LEFT:
                target = rBody.getLocation().add(new Vector2f(-1, 0));
                break;
            case UP:
                target = rBody.getLocation().add(new Vector2f(0, -1));
                break;
            case DOWN:
                target = rBody.getLocation().add(new Vector2f(0, 1));
                break;
            default:
                target = new Vector2f();
                break;
        }
        room.addProjectile(EntityFactory.createProjectile(rBody.getLocation(), target, this, ProjectileType.STRAIGHT));
        lastShotTime = GameEngine.getTime();
    }
    
    @Override
    public void update(long elapsedTime) {
        spawn(6);
        faceMouse(MazeGameServer.mice.get(playerID));
        handleItems(MazeGameServer.inputs.get(playerID));
        handleInputs(MazeGameServer.inputs.get(playerID), elapsedTime);
        if(!isVuln){
            if(GameEngine.getTime() - currentTime > 750)
                isVuln = true;
        }
        //reloadSprite();
    }
    
    public void faceMouse(Vector2f loc) {
        Vector2f dir = loc.sub(rBody.getLocation());
        if(Math.abs(dir.y) > Math.abs(dir.x)) {
            // Up or Down
            if(dir.y > 0) {
                facing = Face.DOWN;
            } else {
                facing = Face.UP;
            }
        } else {
            // Right or Left
            if(dir.x > 0) {
                facing = Face.RIGHT;
            } else {
                facing = Face.LEFT;
            }
        }
    }
    
    @Override
    public void takeDamage(int d) {
        if(isDamage == 0 && isVuln){
            //GameEngine.playSound(game.sound_hit);
            //setHealthPoints(getHealthPoints()-d);
            if(getShield()){//if have shield, take shield away
                setShield(false);
            }else{
                System.out.println(this.getInventory().getItem().toString());
                setHealthPoints(getHealthPoints()-0);//for testing purpose
            }
            isDamage = 1;
            imageIndex = 0;
            currentTime = GameEngine.getTime();
        }
        if(getHealthPoints() == 0 && !isDead) {
            //GameEngine.playSound(game.sound_dead);
            isDead = true;
            isDying = 1;
            imageIndex = 0;
            currentTime = GameEngine.getTime();
        }
    }
    
    public void handleInputs(ArrayList<Boolean> inputs, long elapsedTime) {
        if(spawned) {
            if(!isDead){
                if(isVuln || flash <= 4){
                    if(isDamage == 0){
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
                            if(speedRatio == 0.0f) speedRatio = 0.25f;
                            if(speedRatio < 1.0f) speedRatio += 0.15f * speedRatio;//Math.min((float)Math.sqrt((double) speedRatio), 1.0f);
                        }
                        else {
                            if(speedRatio <= 0.05f) speedRatio = 0.0f;
                            if(speedRatio > 0.0f) speedRatio -= speedRatio * 0.2f;//Math.max(1.0f-(float)Math.sqrt((double) 1.0f-speedRatio), 0.0f);
                            speed = SPEED * speedRatio;
                            if(rBody.getDelta().x > 0.0f) moveX += speed;
                            else if(rBody.getDelta().x < 0.0f) moveX -= speed;
                            if(rBody.getDelta().y > 0.0f) moveY += speed;
                            else if(rBody.getDelta().y < 0.0f) moveY -= speed;
                            if(speedRatio == 0.0f) speedRatio = 0.25f;
                            if(speedRatio < 1.0f) speedRatio += 0.15f * speedRatio;//Math.min((float)Math.sqrt((double) speedRatio), 1.0f);
                        }
                        rBody.move(moveX, moveY, elapsedTime);
                        
                        if (inputs.get(Pressed.FIRE.getValue()) && !isShooting) {
                            if(numProjectiles < MAX_PROJECTILES && GameEngine.getTime() - lastShotTime > 100){
                                fire(facing); // need to change to fire in mouse direction instead of facing direction
                            }
                            isShooting = true;
                            isFire = true;
                            shotTimer = 0;
                        }
                        else if(!inputs.get(Pressed.FIRE.getValue()) && isShooting) {
                            isShooting = false;
                        }
                        
                        animate();
                        isFire = false;
                        shotTimer++;
                    }
                    else{
                        long seconds = GameEngine.getTime() - currentTime;
                        if(seconds > (1333/6)){
                            isDamage++;
                            currentTime = GameEngine.getTime();
                        }
                        imageIndex = imageIndex + 10; 
                        if(imageIndex >= 400) {
                            imageIndex = 0;
                        }
                        if(facing.compareTo(Face.RIGHT) == 0 || facing.compareTo(Face.UP) == 0) {
                            image = animationPath+imageArrayRight[imageIndex/100];
                        }
                        else if(facing.compareTo(Face.LEFT) == 0 || facing.compareTo(Face.DOWN) == 0) {
                            image = animationPath+imageArrayLeft[imageIndex/100];
                        }
                        
                        if(isDamage >= 4){
                            isDamage = 0;
                            isVuln = false;
                        }
                    }
                    flash++;
                }
                else{
                    image = animationPath+"damage4.gif";
                    flash = 0;
                }
            }
            else{
                long seconds = GameEngine.getTime() - currentTime;
                if(seconds > (1666/6)){
                    isDying++;
                    currentTime = GameEngine.getTime();
                }
                imageIndex = imageIndex + 10; 
                if(imageIndex >= 500) {
                    imageIndex = 0;
                }
                image = animationPath+deathArray[imageIndex/100];         
                if(isDying >= 5){
                    isDying = 0;
                    disable();
                }
            }
        }
    }
    
    public void animate() {
        switch(facing) {
            case RIGHT:
                if(rBody.getDelta().x != 0 || rBody.getDelta().y != 0) {
                    animateMovement(Move.RIGHT);
                } else {
                    image = isFire ? animationPath+"shootingRight1.gif" : shotTimer > 10 ? animationPath+"standingRight.gif" : image;
                    imgArraySize = 1;
                }
                break;
            case LEFT:
                if(rBody.getDelta().x != 0 || rBody.getDelta().y != 0) {
                    animateMovement(Move.LEFT);
                } else {
                    image = isFire ? animationPath+"shooting1.gif" : shotTimer > 10 ? animationPath+"standing.gif" : image;
                    imgArraySize = 1;
                }
                break;
            case UP:
                if(rBody.getDelta().x != 0 || rBody.getDelta().y != 0) {
                    animateMovement(Move.UP);
                } else {
                    image = isFire ? animationPath+"climbshoot.gif" : shotTimer > 10 ? animationPath+"climbing1.gif" : image;
                    imgArraySize = 1;
                }
                break;
            case DOWN:
                if(rBody.getDelta().x != 0 || rBody.getDelta().y != 0) {
                    animateMovement(Move.DOWN);
                } else {
                    image = isFire ? animationPath+"jumpshoot1.gif" : shotTimer > 10 ? animationPath+"jumping1.gif" : image;
                    imgArraySize = 1;
                }
                break;
        }
    }
    
    
    public void handleItems(ArrayList<Boolean> inputs){
        if(inputs.get(Pressed.SELECT_FORWARD.getValue())){
            this.getInventory().selectNextItem();
        }
        if(inputs.get(Pressed.SELECT_BACKWARD.getValue())){
            this.getInventory().selectPrevItem();
        }
        if(inputs.get(Pressed.USE_ITEM.getValue())){
            this.getInventory().useSelectedItem(this);
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
        return hasShield;
    }
    
    public void setShield(boolean hasShield){
        this.hasShield = hasShield;
    }
}
