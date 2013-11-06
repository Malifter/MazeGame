package game;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import engine.serializable.SerializedObject;

/*
* Classname:            Game.java
*
* Version information:  1.0
*
* Date:                 10/30/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

/**
 * IGame: Game interface
 */
public abstract class Game implements Serializable {
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -4174358208259933141L;
    protected transient GameEngine engine;
    protected boolean isDone;
    
    public Game(GameEngine e) {
        engine = e;
        isDone = false;
    }
    
    /**
     * isDone: Return is done; true if the game is done (time to exit);
     * false otherwise.
     * 
     * @return isDone
     */
    public boolean isDone() {
        return isDone;
    }
    
    /**
     * init: initializes the game and returns a list of the inputs the
     * game is interested in
     * 
     * @return a list of the inputs the game will use
     */
    public ArrayList<ArrayList<Boolean>> initInputs() {
        return null;
    }
    
    public void shutdown() {
      
    }
    
    /**
     * update: updates the Game's world.
     * 
     * @param time
     *            <add description>
     */
    public List<SerializedObject> update(long time) {
        return null;
    }
    
    /**
     * reInitFromSave: Reinitialize entities after restoring from
     * serialized save file. Stuff like reloading graphics, time-sensitive
     * data, etc.
     */
    protected void reInitFromSave() {
        
    }
    
    /**
     * getDrawables: Returns a list of the entities in the game.
     * 
     * @return
     */
    public ArrayList<Entity> getEntities() {
        return null;
    }
    
    /**
     * Notification that the player has died.
     */
    public void notifyDeath() {
        isDone = true;
    }
}
