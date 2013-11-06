package game;

import engine.serializable.SerializedObject;

/**
 * HealthBar: Level background tile
 */
public class HealthBar extends Entity {
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 4081052719686701412L;
    private Entity player;
    

    /**
     * Constructor
     * @param g
     * @param anImage
     * @param x
     * @param y
     */
    public HealthBar(Game g, String anImage, Entity p) {
        super(g,anImage, (int) p.getMinX(), (int) p.getMinY(), 5, 20);
        player = p;
    }
    
    @Override
    public void update(long time) {
        setMinX(player.getMidX()-149.5f);
        setMinY(player.getMidY()-20.0f);
    }
    
}
