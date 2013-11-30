package game;

/*
* Classname:            GameRunner.java
*
* Version information:  1.0
*
* Date:                 10/30/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

/**
 * GameRunner: Uses a game engine to run a realization of the IGame
 * interface
 */
public class GameRunner {
    /**
     * main: Runs a game using the game engine.
     * 
     * @param args
     */
    public static void main(String[] args) {
        try {
            GameEngine.init();
            GameEngine.run();
        } catch (Exception e) {
            System.out.println("Could not run game");
            e.printStackTrace();
        }
        
    }
    
}
