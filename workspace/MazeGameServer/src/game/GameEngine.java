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
    
    public static ArrayList<ReentrantLock> inputLocks = new ArrayList<ReentrantLock>();
    private static ReentrantLock updateLock;
    
    
    /** flag indicating that we're playing the game */
    public static boolean playingGame = true;
    
    //public ArrayList<ArrayList<Integer>> clientInputs;
    
    private static List<SerializedObject> objectsToUpdate;
    
    /** the number of timer ticks per second */
    private static long timerTicksPerSecond = Sys.getTimerResolution();
    
    /**
     * The time at which the last rendering looped started from the point
     * of view of the game logic
     */
    private static long lastLoopTime = getTime();
    
    /**
     * Constructor: Private constructor to prevent instantiation.
     */
    private GameEngine(){}
    
    public static void init() {
        // init inputs / sounds ? (need ID's for sound)
        //clientInputs = new ArrayList<ArrayList<Integer>>();
        //gameInputs = new ArrayList<ArrayList<Boolean>>();
        //clientInputs.add(new ArrayList<Integer>());
        inputLocks.add(new ReentrantLock());
        updateLock = new ReentrantLock();
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
            
            // Update the world
            setUpdates(MazeGameServer.update(delta)); // generate serialized objects

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
     * getInput: Get a list of the input components to track
     */
    private static void resetInputs() {
        for(int playerID = 0; playerID < inputLocks.size(); playerID++) {
            inputLocks.get(playerID).lock();
            for(int i = 0; i < Pressed.SIZE; i++) {
                MazeGameServer.inputs[playerID][i] = false;
            }
            inputLocks.get(playerID).unlock();
        }
            /*
            inputLocks.get(playerID).lock();
            for(Integer key: clientInputs.get(playerID)) {
                gameInputs.get(playerID).set(key, true);
            }
            inputLocks.get(playerID).unlock();
            */
    }
    
    public static void setInputs(List<Pressed> inputs, int playerID) {
        inputLocks.get(playerID).lock();
        //clientInputs.get(playerID).clear();
        for(Pressed p: inputs) {
            //clientInputs.get(playerID).add(p.getValue());
            MazeGameServer.inputs[playerID][p.getValue()] = true;
        }
        inputLocks.get(playerID).unlock();
    }
    
    private static void setUpdates(List<SerializedObject> updates) {
        updateLock.lock();
        objectsToUpdate = updates;
        updateLock.unlock();
    }
    
    public static List<SerializedObject> getUpdates() {
        List<SerializedObject> updates = new ArrayList<SerializedObject>();
        updateLock.lock();
        updates.addAll(objectsToUpdate);
        updateLock.unlock();
        return updates;
    }
    
    public static void newPlayerConnected(int playerID) {
        MazeGameServer.joinNewPlayer(playerID);
        //engine.clientInputs.add(new ArrayList<Integer>());
        GameEngine.inputLocks.add(new ReentrantLock());
    }
}
