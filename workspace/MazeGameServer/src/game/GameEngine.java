package game;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.lwjgl.Sys;

import engine.Vector2f;
import engine.serializable.SerializedInputs;
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
    public static final boolean DEBUG = false; // XXX: Disable when not needed, currently debugs bounding boxes
    public static boolean playingGame = true;
    private static long timerTicksPerSecond = Sys.getTimerResolution();
    private static long lastLoopTime = getTime();
    public static ArrayList<ReentrantLock> inputLocks = new ArrayList<ReentrantLock>();
    private static ArrayList<ReentrantLock> updateLocks = new ArrayList<ReentrantLock>();
    private static ArrayList<ArrayList<Boolean>> inputs = new ArrayList<ArrayList<Boolean>>();
    private static ArrayList<Vector2f> mice = new ArrayList<Vector2f>();
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
            
            long elapsedTime = getTime() - lastLoopTime;
            if(elapsedTime < 16) {
                try {
                    Thread.sleep(16 - elapsedTime);
                } catch (InterruptedException wait) {
                }
                elapsedTime = 16;
             // XXX: Attempt to deal with lag spikes where enemies/the player would end up outside of room bounds.
            } else if(elapsedTime > 32) elapsedTime = 32;
            lastLoopTime = getTime();
            
            // Copy over inputs from clients
            getInputs();
            // Update the world
            MazeGameServer.update(elapsedTime);
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
     * getInputs: sets local game inputs with local engine inputs
     */
    public static void getInputs() {
        for(int playerID = 0; playerID < MazeGameServer.numPlayers; playerID++) {
            inputLocks.get(playerID).lock();
            try {
                for(int i = 0; i < Pressed.SIZE; i++) {
                    MazeGameServer.inputs.get(playerID).set(i, inputs.get(playerID).get(i));
                }
                // If a new vector isn't created, then it gets manipulated by the player
                // and causes synchronization errors.
                MazeGameServer.mice.set(playerID, new Vector2f(mice.get(playerID)));
            } finally {
                inputLocks.get(playerID).unlock();
            }
        }
    }
    
    /**
     * setInputs: client inputs are stored in local engine inputs
     */
    public static void setInputs(SerializedInputs sInputs, int playerID) {
        inputLocks.get(playerID).lock();
        try {
            for(int i = 0; i < Pressed.SIZE; i++) {
                inputs.get(playerID).set(i, false);
            }
            if(sInputs != null && sInputs.getPressed() != null) {
                for(Pressed p: sInputs.getPressed()) {
                    inputs.get(playerID).set(p.getValue(), true);
                }
                mice.set(playerID, sInputs.getMouseLocation());
            }
        } finally {
            inputLocks.get(playerID).unlock();
        }
    }
    
    /**
     * setUpdates: engine updates are set from local game updates
     */
    private static void setUpdates() {
        for(int playerID = 0; playerID < MazeGameServer.numPlayers; playerID++) {
            updateLocks.get(playerID).lock();
            updates.get(playerID).clear();
            if(MazeGameServer.updates.size() > 0) {
                updates.get(playerID).addAll(MazeGameServer.updates.get(playerID));
            }
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
        if(MazeGameServer.numPlayers < MazeGameServer.NUM_PLAYERS) {
            initNewInputs();
            initNewUpdates();
            MazeGameServer.joinNewPlayer(playerID);
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
        mice.add(new Vector2f());
    }
}
