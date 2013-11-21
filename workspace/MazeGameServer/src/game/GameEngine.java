package game;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import net.java.games.input.*;

import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import engine.serializable.SerializedObject;

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
    
    public ArrayList<ReentrantLock> inputLocks = new ArrayList<ReentrantLock>();
    private ReentrantLock updateLock;
    
    /** the number of frames per second that we want to run the game at */
    private final int FPS = 60;
    
    /** flag indicating that we're playing the game */
    public boolean playingGame = true;
    
    /** the game to run */
    public Game theGame;
    
    /** a list of inputs to listen for */
    private ArrayList<ArrayList<Boolean>> gameInputs;
    
    //public ArrayList<ArrayList<Integer>> clientInputs;
    
    private List<SerializedObject> objectsToUpdate;
    
    /** the number of timer ticks per second */
    private static long timerTicksPerSecond = Sys.getTimerResolution();
    
    /**
     * The time at which the last rendering looped started from the point
     * of view of the game logic
     */
    private long lastLoopTime = getTime();

    /** The time since the last record of fps */
    private long                    lastFpsTime;

    /** The recorded fps */
    private int                     fps;
    
    public static enum Pressed {
        RIGHT(0), LEFT(1), UP(2), DOWN(3), FIRE(4), ESCAPE(5), PAUSE(6), START_GAME(7), 
        SELECT_FORWARD(8), SELECT_BACKWARD(9);
        private final int value;
        private Pressed(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    };
    
    /**
     * Constructor: Constructor for the game engine. Sets up the inputs.
     */
    public GameEngine() {
        // init inputs / sounds ? (need ID's for sound)
        //clientInputs = new ArrayList<ArrayList<Integer>>();
        gameInputs = new ArrayList<ArrayList<Boolean>>();
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
    public void run(Game aGame) throws Exception {
        theGame = aGame;
        gameInputs = theGame.initInputs();
        (new Thread((new GameServer(this)))).start(); // temp implementation of server, needs to have a pre-game lobby first
        gameLoop();
        theGame.shutdown();
    }
    
    /**
     * gameLoop: a timer-based game loop to run the game
     */
    private void gameLoop() {        
        // Game loop runs while the player is playing
        lastLoopTime = getTime();
        while (playingGame) {
            
            // get inputs
            getInputs();
            
            long delta = getTime() - lastLoopTime;
            if(delta < 16) {
                try {
                    Thread.sleep(16 - delta);
                } catch (InterruptedException wait) {
                }
                delta = 16;
            }
            
            lastLoopTime = getTime();
            lastFpsTime += delta;
            fps++;
            
            if (lastFpsTime > 1000) {
                Display.setTitle("(FPS: " + fps + ")");
                lastFpsTime = 0;
                fps = 0;
            }
            
            // Update the world
            setUpdates(theGame.update(delta)); // generate serialized objects

            if (theGame.isDone()) {
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
    private void getInputs() {
        for(int playerID = 0; playerID < gameInputs.size(); playerID++) {
            for(int i = 0; i < gameInputs.get(playerID).size(); i++) {
                gameInputs.get(playerID).set(i, false);
            }
        }
            /*
            inputLocks.get(playerID).lock();
            for(Integer key: clientInputs.get(playerID)) {
                gameInputs.get(playerID).set(key, true);
            }
            inputLocks.get(playerID).unlock();
            */
    }
    
    public void setInputs(List<Pressed> inputs, int playerID) {
        inputLocks.get(playerID).lock();
        //clientInputs.get(playerID).clear();
        for(Pressed p: inputs) {
            //clientInputs.get(playerID).add(p.getValue());
            gameInputs.get(playerID).set(p.getValue(), true);
        }
        inputLocks.get(playerID).unlock();
    }
    
    private void setUpdates(List<SerializedObject> objectsToUpdate) {
        updateLock.lock();
        this.objectsToUpdate = objectsToUpdate;
        updateLock.unlock();
    }
    
    public List<SerializedObject> getUpdates() {
        List<SerializedObject> objectsToUpdate = new ArrayList<SerializedObject>();
        updateLock.lock();
        objectsToUpdate.addAll(this.objectsToUpdate);
        updateLock.unlock();
        return objectsToUpdate;
    }
}
