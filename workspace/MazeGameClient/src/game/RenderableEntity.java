package game;

import engine.Vector2i;
import engine.render.Sprite;

/*
* Classname:            RenderableEntity.java
*
* Version information:  1.0
*
* Date:                 10/26/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

/**
 * IDrawable: Interface for entity objects.
 */
public class RenderableEntity {
    public static final int TILESIZE = 16;
    private Sprite sprite;
    private Vector2i location;
    
    public RenderableEntity(String img, Vector2i loc) {
        sprite = Game.getDisplay().getSprite(img);
        location = loc;
    }
    
    public void draw() {
        sprite.draw(location.x, location.y);
    }
}
