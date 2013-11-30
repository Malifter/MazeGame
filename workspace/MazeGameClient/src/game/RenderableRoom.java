package game;

import java.util.ArrayList;
import engine.Vertex2;

/*
* Classname:            Room.java
*
* Version information:  1.0
*
* Date:                 11/3/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

public class RenderableRoom {
    private ArrayList<RenderableEntity> foreground = new ArrayList<RenderableEntity>();
    private ArrayList<RenderableEntity> background = new ArrayList<RenderableEntity>();
    //private ArrayList<Entity> traps = new ArrayList<Entity>();
    
    public RenderableRoom() {
    }
    
    public void addToForeground(RenderableEntity tile) {
        foreground.add(tile);
    }
    
    public void addToBackground(RenderableEntity tile) {
        background.add(tile);
    }
    
    /*public void addTrap(Entity trap) {
        traps.add(trap);
    }
    public ArrayList<Entity> getTraps() {
        return traps;
    }*/
    
    public ArrayList<RenderableEntity> getBackground() {
        return background;
    }
    
    public ArrayList<RenderableEntity> getForeground() {
        return foreground;
    }
}
