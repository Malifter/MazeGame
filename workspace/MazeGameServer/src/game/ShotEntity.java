package game;

import engine.SerializedObject;

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
public class ShotEntity extends Entity {
    
    private static final int MIN_RANGE = 168;

    /** The vertical speed at which the players shot moves */
    private float moveSpeedX = 250;
    
    //hardcoded for cannon
    private float moveSpeedY = 250;
    
    /** The game in which this entity exists */
    private MazeGameServer game;
    
    private static int range;
    
    int startX;
    int startY;
    
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
    public ShotEntity(Game g, String img, int x, int y, Face direction, int damage, int range) {
        super(g, img, x, y, 8, 6);
        this.startX = x;
        this.startY = y;
        this.damage = damage;
        remove = false;
        if(range < MIN_RANGE) {
            this.range = MIN_RANGE;
        }
        else {
            this.range = range;
        }
        switch(direction) {
            case RIGHT:
                dx = moveSpeedX;
                dy = 0;
                break;
            case LEFT:
                dx = -moveSpeedX;
                dy = 0;
                break;
            case UP:
                dy = -moveSpeedY;
                dx = 0;
                break;
            case DOWN:
                dy = moveSpeedY;
                dx = 0;
                break;
        }
        if(image == null) image = "shot.gif";
        this.game = (MazeGameServer) g;
        
    }
    
    /**
     * Reinitializes this entity, for reuse
     * 
     * @param x
     *            new x coordinate
     * @param y
     *            new y coordinate
     */
    public void reinitialize(int x, int y) {
        setMinX(x);
        setMinY(y);
        remove = false;
    }
    
    @Override
    public void update(long time) {
        move(time);
    }
    
    /**
     * Request that this shot moved based on time elapsed
     * 
     * @param delta
     *            The time that has elapsed since last move
     */
    public void move(long delta) {
        // proceed with normal move
        super.move(delta);
        
        // if shot off the screen, remove
        if (range < Math.abs(startX - midX)) {
            remove = true;
        }
        else if(range < Math.abs(startY - midY)) {
            remove = true;
        }
    }
    
    /**
     * Notification that this shot has collided with another
     * entity
     * 
     * @param other
     *            The other entity with which we've collided
     */
    public void bulletHit(Entity enemy) {
        if (remove) {
            return;
        }
        remove = true;
        enemy.takeDamage(damage);
        //GameEngine.playSound(game.sound_hit);
    }
    
    @Override
    public void enableY() {
        dy = -moveSpeedY;
    }
    
    @Override
    public void disableY() {
        dy = 0;
    }
}
