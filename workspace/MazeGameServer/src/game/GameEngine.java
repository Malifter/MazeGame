package game;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import org.lwjgl.Sys;
import engine.serializable.SerializedObject;
import game.enums.Pressed;

/*
* Classname:            GameEngine.java
*
* Version information:  1.0
*
* Date:                 10/30/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

/**
 * GameEngine: Implementation of a game loop. Runs a game implementing the
 * IGame interface.
 */
public class GameEngine {
    public static boolean playingGame = true;
    private static long timerTicksPerSecond = Sys.getTimerResolution();
    private static long lastLoopTime = getTime();
    
    public static ArrayList<ReentrantLock> inputLocks = new ArrayList<ReentrantLock>();
    private static ArrayList<ReentrantLock> updateLocks = new ArrayList<ReentrantLock>();
    private static ArrayList<ArrayList<Boolean>> inputs = new ArrayList<ArrayList<Boolean>>();
    private static ArrayList<List<SerializedObject>> updates = new ArrayList<List<SerializedObject>>();
    
    /**
     * Constructor: Private constructor to prevent instantiation.
     */
    private GameEngine(){}
    
    public static void init() {
        // init inputs / sounds ? (need ID's for sound)
    }
    
    /**
     * run: runs the game loop on a game implementing the IGame interface.
     * 
     * @param aGame
     *            the game to run.
     * @throws Exception
     */
    public static void run() throws Exception {
        MazeGameServer.init();
        (new Thread((new GameServer()))).start(); // temp implementation of server, needs to have a pre-game lobby first
        gameLoop();
    }
    
    /**
     * gameLoop: a timer-based game loop to run the game
     */
    private static void gameLoop() {        
        // Game loop runs while the player is playing
        lastLoopTime = getTime();
        while (playingGame) {
            
            // resets inputs
            resetInputs();
            
            long delta = getTime() - lastLoopTime;
            if(delta < 16) {
                try {
                    Thread.sleep(16 - delta);
                } catch (InterruptedException wait) {
                }
                delta = 16;
            }
            lastLoopTime = getTime();
            
            // Copy over inputs from clients
            getInputs();
            // Update the world
            MazeGameServer.update(delta);
            // Send updates to server
            setUpdates();

            if (MazeGameServer.isDone()) {
                playingGame = false;
            }
        }
    }
    
    /**
     * Get the high resolution time in milliseconds
     * 
     * @return The high resolution time in milliseconds
     */
    public static long getTime() {
        // we get the "timer ticks" from the high resolution timer
        // multiply by 1000 so our end result is in milliseconds
        // then divide by the number of ticks in a second giving
        // us a nice clear time in milliseconds
        return (Sys.getTime() * 1000) / timerTicksPerSecond;
    }
    
    /**
     * resetInputs: resets local engine inputs
     */
    private static void resetInputs() {
        for(int playerID = 0; playerID < MazeGameServer.numPlayers; playerID++) {
            inputLocks.get(playerID).lock();
            for(int i = 0; i < Pressed.SIZE; i++) {
                inputs.get(playerID).set(i, false);
            }
            inputLocks.get(playerID).unlock();
        }
    }
    
    /**
     * getInputs: sets local game inputs with local engine inputs
     */
    public static void getInputs() {
        for(int playerID = 0; playerID < MazeGameServer.numPlayers; playerID++) {
            inputLocks.get(playerID).lock();
            for(int i = 0; i < Pressed.SIZE; i++) {
                MazeGameServer.inputs.get(playerID).set(i, inputs.get(playerID).get(i));
            }
            inputLocks.get(playerID).unlock();
        }
    }
    
    /**
     * setInputs: client inputs are stored in local engine inputs
     */
    public static void setInputs(List<Pressed> pressed, int playerID) {
        inputLocks.get(playerID).lock();
        for(Pressed p: pressed) {
            inputs.get(playerID).set(p.getValue(), true);
        }
        inputLocks.get(playerID).unlock();
    }
    
    /**
     * setUpdates: engine updates are set from local game updates
     */
    private static void setUpdates() {
        for(int playerID = 0; playerID < MazeGameServer.numPlayers; playerID++) {
            updateLocks.get(playerID).lock();
            updates.get(playerID).clear();
            updates.get(playerID).addAll(MazeGameServer.updates.get(playerID));
            updateLocks.get(playerID).unlock();
        }
    }
    
    /**
     * getUpdates: playerHandler grabs updates from engine to send to client
     */
    public static List<SerializedObject> getUpdates(int playerID) {
        List<SerializedObject> copy = new ArrayList<SerializedObject>();
        updateLocks.get(playerID).lock();
        copy.addAll(updates.get(playerID));
        updateLocks.get(playerID).unlock();
        return copy;
    }
    
    public static void newPlayerConnected(int playerID) {
        MazeGameServer.joinNewPlayer(playerID);
        if(MazeGameServer.numPlayers < MazeGameServer.NUM_PLAYERS) {
            initNewInputs();
            initNewUpdates();
        }
    }
    
    private static void initNewUpdates() {
        updateLocks.add(new ReentrantLock());
        updates.add(new ArrayList<SerializedObject>());
    }
    
    private static void initNewInputs() {
        ArrayList<Boolean> newInputs = new ArrayList<Boolean>();
        for(int j = 0; j < Pressed.SIZE; j++) {
            newInputs.add(false);
        }
        inputLocks.add(new ReentrantLock());
        inputs.add(newInputs);
    }
}
