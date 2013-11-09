package game;
import java.util.Random;

/*
* Classname:            Obstacle.java
*
* Version information:  1.0
*
* Date:                 10/30/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/


/**
 * Obstacle: can consist of damaging traps (i.e. spikes) or destructable rocks or objects in the way in general
 */
public class Obstacle extends Entity {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -4363795480195834006L;
    MazeGameServer game;
    private static final int COLLISION_DAMAGE = 10;
    private boolean destructable = false;
    private boolean dangerous = false;
    
    public Obstacle(Game g, String img, int iX, int iY, int x, int y, int w, int h) {
        super(g, img, iX, iY, w, h);
        game = (MazeGameServer) g;
        minX = x;
        minY = y;
        width = w;
        height = h;
        offsetX = Math.abs(imageX - minX);
        offsetY = Math.abs(imageY - minY);
        calculateBounds();
        setHealthPoints(0);
        damage = COLLISION_DAMAGE;
    }
    
    public void setDestructable(boolean destructable) {
        this.destructable = destructable;
    }
    
    public boolean isDestructable() {
        return destructable;
    }
    
    public boolean isDangerous() {
        return dangerous;
    }
    
    public void setDangerous(boolean dangerous) {
        this.dangerous = dangerous;
    }
}