package game;
/*
* Classname:            EnvironmentTile.java
*
* Version information:  1.0
*
* Date:                 10/30/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

/**
 * EnvironmentTile: Level background tile
 */
public class EnvironmentTile extends Entity {
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 4081052719686701412L;
    public static final int TILESIZE = 16;
    

    /**
     * Constructor
     * @param g
     * @param anImage
     * @param x
     * @param y
     */
    public EnvironmentTile(Game g, String anImage, int x, int y) {
        super(g,anImage,x,y);
    }
    
}
