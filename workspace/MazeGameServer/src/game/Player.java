package game;

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
public class Player extends Entity {
    private static final long serialVersionUID = -8044414761618085107L;
    private MazeGameServer game;
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
    private float moveFactor = 10.0f;
    private float speedRatio = 0.0f; // between 0 and 1
    private int shotTimer = 0;
    private float lastShotTime;
    private static final int MAX_HEALTH = 28;
    private static final int BLASTER_DAMAGE = 1;
    private static final int ATTACK_RANGE = 168;
    private static final float SPEED = 1.3f;
    private int isDamage = 0;
    private long currentTime = 0;
    private float spawnX = 0;
    private float spawnY = 0;
    private int imgArraySize = 0;
    private int playerID = 0;
    
    public Player(Game g, String file, int iX, int iY, int x, int y, int w, int h, int lives, int playerID) {
        super(g, file, iX, iY, w, h);
        game = (MazeGameServer) g;
        imageIndex = imageIndex + 5; 
        image = spawnArray[0];
        imgArraySize = spawnArray.length;
        minX = x;
        minY = y;
        width = w;
        height = h;
        spawnX = iX;
        spawnY = iY;
        offsetX = Math.abs(imageX - minX);
        offsetY = Math.abs(imageY - minY);
        lastShotTime = 0;
        calculateBounds();
        setHealthPoints(MAX_HEALTH);
        this.lives = lives;
        this.playerID = playerID;
    }
    
    public float getSpawnX() {
        return spawnX;
    }
    
    public float getSpawnY() {
        return spawnY;
    }
    
    public int getPlayerID() {
        return playerID;
    }
    
    public void reset() {
        setMinX(spawnX);
        setMinY(spawnY);
        imageIndex = imageIndex + 5; 
        image = spawnArray[0];
        imgArraySize = spawnArray.length;
        lastShotTime = 0;
        calculateBounds();
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
        remove = false;
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
            image = spawnArray[test];
            if(image.equals("spawn5.gif")){
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
                    image = runningRightArray[imageIndex/100];
                    imgArraySize = runningRightArray.length;
                    break;
                case LEFT:
                    image = runningLeftArray[imageIndex/100];
                    imgArraySize = runningLeftArray.length;
                    break;
                case UP:
                    image = runningUpArray[imageIndex/100];
                    imgArraySize = runningUpArray.length;
                    break;
                case DOWN:
                    image = runningDownArray[imageIndex/100];
                    imgArraySize = runningDownArray.length;
                    break;
            }
        }
        else if(isFire) {
            switch (direction) {
                case RIGHT:
                    image = runningRightShootArray[imageIndex/100];
                    imgArraySize = runningRightShootArray.length;
                    break;
                case LEFT:
                    image = runningLeftShootArray[imageIndex/100];
                    imgArraySize = runningLeftShootArray.length;
                    break;
                case UP:
                    image = runningUpArray[imageIndex/100];
                    imgArraySize = runningUpArray.length;
                    break;
                case DOWN:
                    image = runningDownArray[imageIndex/100];
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
        switch (direction) {
            case RIGHT:
                shots.add(new ShotEntity(game, "shot.gif",(int) maxX,
                        (int) minY+8, direction, BLASTER_DAMAGE, ATTACK_RANGE));
                break;
            case LEFT:
                shots.add(new ShotEntity(game, "shot.gif",(int) minX,
                        (int) minY+8, direction, BLASTER_DAMAGE, ATTACK_RANGE));
                break;
            case UP:
                shots.add(new ShotEntity(game, "shot.gif",(int) maxX,
                        (int) minY+2, direction, BLASTER_DAMAGE, ATTACK_RANGE));
                break;
            case DOWN:
                shots.add(new ShotEntity(game, "shot.gif",(int) minX,
                        (int) minY+2, direction, BLASTER_DAMAGE, ATTACK_RANGE));
                break;
        }
        lastShotTime = GameEngine.getTime();
    }
    
    @Override
    public void update(long time) {
        movements(time);
        calculateBounds();
        if(!isVuln){
            if(GameEngine.getTime() - currentTime > 750)
                isVuln = true;
        }
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
        //reloadSprite();
    }
    @Override
    public void takeDamage(int d) {
        if(isDamage == 0 && isVuln){
            //GameEngine.playSound(game.sound_hit);
            setHealthPoints(getHealthPoints()-d);
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
    public void movements(long time) {
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
                        if (((MazeGameServer) game).inputs.get(playerID).get(GameEngine.Pressed.RIGHT.getValue())) {
                            if(firstMove.compareTo(Move.NONE) == 0) firstMove = Move.RIGHT;
                            movingRight = true;
                            //move((int) (speed * (time / moveFactor)), 0);
                            moveX += (SPEED * speedRatio * (time / moveFactor));
                        } else if (firstMove.compareTo(Move.RIGHT) == 0) firstMove = Move.NONE;
                        if (((MazeGameServer) game).inputs.get(playerID).get(GameEngine.Pressed.LEFT.getValue())) {
                            if(firstMove.compareTo(Move.NONE) == 0) firstMove = Move.LEFT;
                            movingLeft = true;
                            //move(-(int) (speed * (time / moveFactor)), 0);
                            moveX -= (SPEED * speedRatio * (time / moveFactor));
                        } else if (firstMove.compareTo(Move.LEFT) == 0) firstMove = Move.NONE;
                        if (((MazeGameServer) game).inputs.get(playerID).get(GameEngine.Pressed.UP.getValue())) {
                            if(firstMove.compareTo(Move.NONE) == 0) firstMove = Move.UP;
                            movingUp = true;
                            //move(0, -(int) (speed * (time / moveFactor)));
                            moveY -= (SPEED * speedRatio * (time / moveFactor));
                        } else if (firstMove.compareTo(Move.UP) == 0) firstMove = Move.NONE;
                        if (((MazeGameServer) game).inputs.get(playerID).get(GameEngine.Pressed.DOWN.getValue())) {
                            if(firstMove.compareTo(Move.NONE) == 0) firstMove = Move.DOWN;
                            movingDown = true;
                            //move(0, (int) (speed * (time / moveFactor)));
                            moveY += (SPEED * speedRatio * (time / moveFactor));
                        } else if (firstMove.compareTo(Move.DOWN) == 0) firstMove = Move.NONE;
                        if(firstMove.compareTo(Move.NONE) == 0) {
                            if(speedRatio <= 0.05f) speedRatio = 0.0f;
                            if(speedRatio > 0.0f) speedRatio -= speedRatio * 0.2f;//Math.max(1.0f-(float)Math.sqrt((double) 1.0f-speedRatio), 0.0f);
                            if(deltaX > 0.0f) moveX += (SPEED * speedRatio * (time / moveFactor));
                            else if(deltaX < 0.0f) moveX -= (SPEED * speedRatio * (time / moveFactor));
                            if(deltaY > 0.0f) moveY += (SPEED * speedRatio * (time / moveFactor));
                            else if(deltaY < 0.0f) moveY -= (SPEED * speedRatio * (time / moveFactor));
                        }
                        else {
                            if(speedRatio == 0.0f) speedRatio = 0.25f;
                            if(speedRatio < 1.0f) speedRatio += 0.15f * speedRatio;//Math.min((float)Math.sqrt((double) speedRatio), 1.0f);
                        }
                        move(moveX, moveY);
                        
                        if (((MazeGameServer) game).inputs.get(playerID).get(GameEngine.Pressed.FIRE.getValue()) && !isShooting) {
                            if(numBullets <= 2 && GameEngine.getTime() - lastShotTime > 100){
                                fire(facing); // need to change to fire in mouse direction instead of facing direction
                                numBullets++;
                            }
                            isShooting = true;
                            isFire = true;
                            shotTimer = 0;
                        }
                        else if(!((MazeGameServer) game).inputs.get(playerID).get(GameEngine.Pressed.FIRE.getValue()) && isShooting) {
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
                                        image = isFire ? "shootingRight1.gif" : shotTimer > 10 ? "standingRight.gif" : image;
                                        imgArraySize = 1;
                                        break;
                                    case LEFT:
                                        image = isFire ? "shooting1.gif" : shotTimer > 10 ? "standing.gif" : image;
                                        imgArraySize = 1;
                                        break;
                                    case UP:
                                        image = isFire ? "climbshoot.gif" : shotTimer > 10 ? "climbing1.gif" : image;
                                        imgArraySize = 1;
                                        break;
                                    case DOWN:
                                        image = isFire ? "jumpshoot1.gif" : shotTimer > 10 ? "jumping1.gif" : image;
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
                            image = imageArrayRight[imageIndex/100];
                        }
                        else if(facing.compareTo(Face.LEFT) == 0 || facing.compareTo(Face.DOWN) == 0) {
                            image = imageArrayLeft[imageIndex/100];
                        }
                        
                        if(isDamage >= 4){
                            isDamage = 0;
                            isVuln = false;
                        }
                    }
                    flash++;
                }
                else{
                    image = "damage4.gif";
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
                image = deathArray[imageIndex/100];         
                if(isDying >= 5){
                    isDying = 0;
                    remove = true;
                }
            }
        }
    }
}
