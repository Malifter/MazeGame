package game;
import java.io.Serializable;
import java.util.ArrayList;

/*
* Classname:            Entity.java
*
* Version information:  1.0
*
* Date:                 10/30/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

/**
 * IDrawable: Interface for entity objects.
 */
public abstract class Entity implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1222272463309629286L;
    protected float dx;
    protected float dy;
    protected float imageX;
    protected float imageY;
    protected float maxX;
    protected float maxY;
    protected String image;
    protected Sprite sprite;
    protected Game game;
    protected float radius;
    protected float midX;
    protected float midY;
    protected float minX;
    protected float minY;
    protected float width;
    protected float height;
    protected float velY;
    protected float velX;
    protected float offsetX;
    protected float offsetY;
    protected float deltaX;
    protected float deltaY;
    protected ArrayList<Entity> shots;
    private int health;
    protected boolean remove;
    protected int damage;
    protected int lives;
    protected boolean isDead = false;
    public static enum Move {NONE, RIGHT, LEFT, UP, DOWN};
    public static enum Face {RIGHT, LEFT, UP, DOWN};
    
    public Entity(Game g, String anImage, int x, int y) {
        imageX = x;
        imageY = y;
        minX = x;
        minY = y;
        game = g;
        velY = 0;
        velX = 0;
        offsetX = 0;
        offsetY = 0;
        deltaX = 0;
        deltaY = 0;
        damage = 0;
        sprite = game.getDisplay().getSprite(anImage);
        width = sprite.getWidth();
        height = sprite.getHeight();
        shots = new ArrayList<Entity>();
        calculateBounds();
        health = -1;
        lives = -1;
        remove = false;
    }
    
    protected void calculateBounds() {
        imageX = minX - offsetX;
        imageY = minY - offsetY;
        maxX = minX + width;
        maxY = minY + height;
        midX = ((minX+maxX)/2.0f);
        midY = ((minY+maxY)/2.0f);
        float xd = midX - maxX;
        float yd = midY - maxY;
        radius = (float) Math.sqrt(xd*xd + yd*yd);
    }
    
    public void update(long time) {
        
    }
    
    public void setHealthPoints(int hp) {
        if(hp < 0) {
            this.health = 0;
        }
        else {
            this.health = hp;
        }
    }

    public int getHealthPoints() {
        return health;
    }
    
    public void checkHealth() {
        if(health == 0) {
            remove = true;
        }
    }
    
    public void setDamage(int d) {
        damage = d;
    }
    
    public int getDamage() {
        return damage;
    }
    
    public void reloadSprite() {
        sprite = game.getDisplay().getSprite(image);
    }
    
    public void setGame(Game aGame) {
        game = aGame;
    }
    
    public void setSprite(Sprite aSprite) {
        sprite = aSprite;
    }
    
    /**
     * draw: draws an object to a canvas
     */
    public void draw() {
        sprite.draw((int) imageX, (int) imageY);
    }
    
    /**
     * Request that this entity move itself based on a certain amount
     * of time passing.
     * 
     * @param delta
     *            The amount of time that has passed in milliseconds
     */
    public void move(long delta) {
        // update the location of the entity based on move speeds
        minX += (delta * dx) / 1000;
        minY += (delta * dy) / 1000;
        imageX += (delta * dx) / 1000;
        imageY += (delta * dy) / 1000;
        calculateBounds();
    }
    
    public void move(float x, float y) {
        deltaX = x;
        deltaY = y;
        minX += x;
        minY += y;
        imageX += x;
        imageY += y;
        calculateBounds();
    }
    
    public float getMaxX() {
        return maxX;
    }
    
    public void setMinX(float posX) {
        this.minX = posX;
        calculateBounds();
    }
    
    public float getMaxY() {
        return maxY;
    }
    
    public void setMinY(float posY) {
        this.minY = posY;
        calculateBounds();
    }
    
    public float getMidX() {
        return midX;
    }
    
    public float getMidY() {
        return midY;
    }

    public float getMinX() {
        return minX;
    }
    
    public float getMinY() {
        return minY;
    }
    
    public float getDeltaX() {
        return deltaX;
    }
    
    public float getDeltaY() {
        return deltaY;
    }
    
    public float getImageX() {
        return imageX;
    }
    public float getImageY() {
        return imageY;
    }
    
    public float getRadius() {
        return radius;
    }
    
    public Sprite getSprite() {
        return sprite;
    }

    public void setShots(ArrayList<Entity> shots) {
        this.shots = shots;
    }

    public ArrayList<Entity> getShots() {
        return shots;
    }
    
    public boolean needsDelete() {
        return remove;
    }
    
    public void takeDamage(int d) {
        setHealthPoints(health-d);
    }
    
    public boolean isDead() {
        return isDead;
    }
    
    public int getLives() {
        return lives;
    }
    
    public void setLives(int l) {
        lives = l;
    }
    
    //hardcoded for now
    public void enableY() {
    }
    
    public void disableY() {
    }
}
