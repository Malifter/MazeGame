package game.entities.npcs;

import engine.Vertex2f;
import engine.physics.RigidBody;
import game.GameEngine;
import game.Inventory;
import game.enums.*;
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
    private int numBullets = 0;
    private boolean spawned = false;
    private Move firstMove = Move.NONE;
    private boolean movingRight = false;
    private boolean movingLeft = false;
    private boolean movingUp = false;
    private boolean movingDown = false;
    private Face facing = Face.RIGHT;
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
    private float speedRatio = 0.0f; // between 0 and 1
    private int isDamage = 0;
    private long currentTime = 0;
    // SPAWN LOCATION NEEDS EITHER INSTANCE OF SPAWNLOC OR LOCATION IN EXTERIOR
    private int imgArraySize = 0;
    private int playerID = 0;
    private Inventory inventory = new Inventory(this);
    private int lives;
    
    public Player(String img, RigidBody rb, int playerID) {
        super(img, rb);
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
        movingLeft = false;
        movingRight = false;
        movingUp = false;
        movingDown = false;
        firstMove = Move.NONE;
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
    
    public void nextAnimation(String arrayName, int frames) {  
        if(arrayName.equals("spawnArray") && spawned == false){
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
        Vertex2f target;
        switch (direction) {
            case RIGHT:
                target = rBody.getLocation().add(new Vertex2f(1, 0));
                break;
            case LEFT:
                target = rBody.getLocation().add(new Vertex2f(-1, 0));
                break;
            case UP:
                target = rBody.getLocation().add(new Vertex2f(0, -1));
                break;
            case DOWN:
                target = rBody.getLocation().add(new Vertex2f(0, 1));
                break;
            default:
                target = new Vertex2f();
                break;
        }
        shots.add(EntityFactory.createProjectile(rBody.getLocation(), target, this, ProjectileType.STRAIGHT));
        lastShotTime = GameEngine.getTime();
    }
    
    @Override
    public void update(Boolean[] inputs, long time) {
        movements(inputs, time);
        if(!isVuln){
            if(GameEngine.getTime() - currentTime > 750)
                isVuln = true;
        }
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
        //reloadSprite();
    }
    @Override
    public void takeDamage(int d) {
        if(isDamage == 0 && isVuln){
            //GameEngine.playSound(game.sound_hit);
            //setHealthPoints(getHealthPoints()-d);
            setHealthPoints(getHealthPoints()-0);//for testing purpose
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
    public void movements(Boolean[] inputs, long elapsedTime) {
        if(spawned) {
            if(!isDead){
                if(isVuln || flash <= 4){
                    if(isDamage == 0){
                        movingRight = false;
                        movingLeft = false;
                        movingUp = false;
                        movingDown = false;
                        float moveX = 0.0f;
                        float moveY = 0.0f;
                        float speed = SPEED * speedRatio;
                        if (inputs[Pressed.RIGHT.getValue()]) {
                            if(firstMove.compareTo(Move.NONE) == 0) firstMove = Move.RIGHT;
                            movingRight = true;
                            moveX += speed;
                        } else if (firstMove.compareTo(Move.RIGHT) == 0) firstMove = Move.NONE;
                        if (inputs[Pressed.LEFT.getValue()]) {
                            if(firstMove.compareTo(Move.NONE) == 0) firstMove = Move.LEFT;
                            movingLeft = true;
                            moveX -= speed;
                        } else if (firstMove.compareTo(Move.LEFT) == 0) firstMove = Move.NONE;
                        if (inputs[Pressed.UP.getValue()]) {
                            if(firstMove.compareTo(Move.NONE) == 0) firstMove = Move.UP;
                            movingUp = true;
                            moveY -= speed;
                        } else if (firstMove.compareTo(Move.UP) == 0) firstMove = Move.NONE;
                        if (inputs[Pressed.DOWN.getValue()]) {
                            if(firstMove.compareTo(Move.NONE) == 0) firstMove = Move.DOWN;
                            movingDown = true;
                            moveY += speed;
                        } else if (firstMove.compareTo(Move.DOWN) == 0) firstMove = Move.NONE;
                        
                        if(firstMove.compareTo(Move.NONE) == 0) {
                            if(speedRatio <= 0.05f) speedRatio = 0.0f;
                            if(speedRatio > 0.0f) speedRatio -= speedRatio * 0.2f;//Math.max(1.0f-(float)Math.sqrt((double) 1.0f-speedRatio), 0.0f);
                            speed = SPEED * speedRatio;
                            if(rBody.getDelta().x > 0.0f) moveX += speed;
                            else if(rBody.getDelta().x < 0.0f) moveX -= speed;
                            if(rBody.getDelta().y > 0.0f) moveY += speed;
                            else if(rBody.getDelta().y < 0.0f) moveY -= speed;
                        }
                        else {
                            if(speedRatio == 0.0f) speedRatio = 0.25f;
                            if(speedRatio < 1.0f) speedRatio += 0.15f * speedRatio;//Math.min((float)Math.sqrt((double) speedRatio), 1.0f);
                        }
                        rBody.move(moveX, moveY, elapsedTime);
                        
                        if (inputs[Pressed.FIRE.getValue()] && !isShooting) {
                            if(numBullets <= 2 && GameEngine.getTime() - lastShotTime > 100){
                                fire(facing); // need to change to fire in mouse direction instead of facing direction
                                numBullets++;
                            }
                            isShooting = true;
                            isFire = true;
                            shotTimer = 0;
                        }
                        else if(!inputs[Pressed.FIRE.getValue()] && isShooting) {
                            isShooting = false;
                        }

                        switch (firstMove) {
                            case RIGHT:
                                if(!movingLeft) {
                                    animateMovement(Move.RIGHT);
                                    facing = Face.RIGHT;
                                }
                                else if(movingLeft && movingUp && !movingDown) {
                                    imageIndex = 0;
                                    animateMovement(Move.UP);
                                    facing = Face.UP;
                                    firstMove = Move.UP;
                                }
                                else if(movingLeft && !movingUp && movingDown) {
                                    imageIndex = 0;
                                    animateMovement(Move.DOWN);
                                    facing = Face.DOWN;
                                    firstMove = Move.DOWN;
                                }
                                isFire = false;
                                break;
                            case LEFT:
                                if(!movingRight) {
                                    animateMovement(Move.LEFT);
                                    facing = Face.LEFT;
                                }
                                else if(movingRight && movingUp && !movingDown) {
                                    imageIndex = 0;
                                    animateMovement(Move.UP);
                                    facing = Face.UP;
                                    firstMove = Move.UP;
                                }
                                else if(movingRight && !movingUp && movingDown) {
                                    imageIndex = 0;
                                    animateMovement(Move.DOWN);
                                    facing = Face.DOWN;
                                    firstMove = Move.DOWN;
                                }
                                isFire = false;
                                break;
                            case UP:
                                if(!movingDown) {
                                    animateMovement(Move.UP);
                                    facing = Face.UP;
                                }
                                else if(movingDown && movingRight && !movingLeft) {
                                    imageIndex = 0;
                                    animateMovement(Move.RIGHT);
                                    facing = Face.RIGHT;
                                    firstMove = Move.RIGHT;
                                }
                                else if(movingDown && !movingRight && movingLeft) {
                                    imageIndex = 0;
                                    animateMovement(Move.LEFT);
                                    facing = Face.LEFT;
                                    firstMove = Move.LEFT;
                                }
                                isFire = false;
                                break;
                            case DOWN:
                                if(!movingUp) {
                                    animateMovement(Move.DOWN);
                                    facing = Face.DOWN;
                                }
                                else if(movingUp && movingRight && !movingLeft) {
                                    imageIndex = 0;
                                    animateMovement(Move.RIGHT);
                                    facing = Face.RIGHT;
                                    firstMove = Move.RIGHT;
                                }
                                else if(movingUp && !movingRight && movingLeft) {
                                    imageIndex = 0;
                                    animateMovement(Move.LEFT);
                                    facing = Face.LEFT;
                                    firstMove = Move.LEFT;
                                }
                                isFire = false;
                                break;
                            case NONE:
                                switch (facing) {
                                    case RIGHT:
                                        image = isFire ? animationPath+"shootingRight1.gif" : shotTimer > 10 ? animationPath+"standingRight.gif" : image;
                                        imgArraySize = 1;
                                        break;
                                    case LEFT:
                                        image = isFire ? animationPath+"shooting1.gif" : shotTimer > 10 ? animationPath+"standing.gif" : image;
                                        imgArraySize = 1;
                                        break;
                                    case UP:
                                        image = isFire ? animationPath+"climbshoot.gif" : shotTimer > 10 ? animationPath+"climbing1.gif" : image;
                                        imgArraySize = 1;
                                        break;
                                    case DOWN:
                                        image = isFire ? animationPath+"jumpshoot1.gif" : shotTimer > 10 ? animationPath+"jumping1.gif" : image;
                                        imgArraySize = 1;
                                        break;
                                }
                                isFire = false;
                                break;
                        }
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
    
    public void pickItem(Item item){
        if(item instanceof Consumable){
            Consumable cItem = (Consumable) item;
            cItem.consumed(this);
        }else if(item instanceof NotConsumable){
            NotConsumable ncItem = (NotConsumable) item;
            this.addToInventory(ncItem);
            if(ncItem instanceof Bomb) {
                ((Bomb) ncItem).startTimer(this);
                this.takeDamage(((Bomb) ncItem).getPower());
            }
            
        }
    }
    
    public void addToInventory(NotConsumable ncItem){
        this.getInventory().addItem(ncItem);
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
}
