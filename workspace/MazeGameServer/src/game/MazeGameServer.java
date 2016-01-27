package game;

import java.util.ArrayList;
import java.util.List;
import engine.Vector2f;
import engine.serializable.SerializeFactory;
import engine.serializable.SerializedObject;
import game.entities.Entity;
import game.entities.EntityFactory;
import game.enums.Face;
import game.enums.GameState;
import game.enums.Pressed;
import game.levelloader.Level;
import game.levelloader.LevelLoader;

/*
* Classname:            MazeGameServer.java
*
* Version information:  1.0
*
* Date:                 10/30/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

/**
 * MazeGameServer: This is our Mega Man game
 */
public class MazeGameServer {
    private static boolean done = false;
    public static final int NUM_PLAYERS = 4;
    public static int numPlayers = 0;
    public static ArrayList<ArrayList<Boolean>> inputs = new ArrayList<ArrayList<Boolean>>();
    public static ArrayList<Vector2f> mice = new ArrayList<Vector2f>();
    public static ArrayList<List<SerializedObject>> updates = new ArrayList<List<SerializedObject>>();
    public static ArrayList<GameState> states = new ArrayList<GameState>();
    public final static Level level = LevelLoader.generateRandomLevel(LevelLoader.LevelSize.SMALL);
    
    /**
     * Constructor: Private to prevent instantiation.
     * 
     * @param e
     */
    private MazeGameServer(){}
    
    public static void init() {

    }
    
    public static ArrayList<Entity> getEntities() {
        ArrayList<Entity> tmp = new ArrayList<Entity>();
        return tmp;
    }
    
    /*private static void initSounds() {
        /*sound_hit = GameEngine.addSound("hit.wav");
        sound_shot = GameEngine.addSound("shot.wav");
        sound_spawn = GameEngine.addSound("spawn.wav");
        sound_deflect = GameEngine.addSound("deflect.wav");
        sound_dead = GameEngine.addSound("dead.wav");
        BGM_quickman = GameEngine.addSound("music/quickmanBGM.wav");
    }*/
    
    /*
     * (non-Javadoc)
     * @see IGame#update(long)
     */
    public static void update(long elapsedTime) {
        clearUpdates();
        // Update and serialize the world
        level.update(elapsedTime);
        level.applyCollisions();
        if(updates.size() > 0) {
            level.serialize();
            // game state
            for(int playerID = 0; playerID < numPlayers; playerID++) {
                updates.get(playerID).add(SerializeFactory.serialize(MazeGameServer.states.get(playerID)));
            }
        }
        
        /*if((GameEngine.getTime()-timeBGM) > 38000) {
            timeBGM = GameEngine.getTime();
        }*/
    }
    
    /**
     * isDone: Return is done; true if the game is done (time to exit);
     * false otherwise.
     * 
     * @return isDone
     */
    public static boolean isDone() {
        return done;
    }
    
    public static void clearUpdates() {
        updates.clear();
        for(int playerID = 0; playerID < numPlayers; playerID++) {
            updates.add(new ArrayList<SerializedObject>());
        }
    }
    
    // TODO: possibly adding player here without having a locking mechanism might cause concurrency issues if a player joins when someone else is outside
    public static void joinNewPlayer(int playerID) {
        if(numPlayers < NUM_PLAYERS) {
            Vector2f spawnLocation = new Vector2f(level.getExterior().getPlayerSpawns().get(playerID));
            level.getExterior().addPlayer(EntityFactory.createPlayer(Face.DOWN, spawnLocation, playerID, level.getExterior()));
            initNewInputs();
            initNewUpdates();
            numPlayers++;
        }
    }
    
    private static void initNewUpdates() {
        updates.add(new ArrayList<SerializedObject>());
        states.add(GameState.PLAYING);
    }
    
    private static void initNewInputs() {
        ArrayList<Boolean> newInputs = new ArrayList<Boolean>();
        for(int j = 0; j < Pressed.SIZE; j++) {
            newInputs.add(false);
        }
        inputs.add(newInputs);
        mice.add(new Vector2f());
    }
    
    public static void hostageSaved(int playerID) {
        for(int i = 0; i < numPlayers; i++) {
            if(i == playerID) {
                states.set(i, GameState.WIN);
            } else {
                states.set(i, GameState.LOSE);
            }
        }
    }
}

