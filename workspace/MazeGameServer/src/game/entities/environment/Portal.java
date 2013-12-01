package game.entities.environment;

import engine.physics.RigidBody;
import game.MazeGameServer;
import game.entities.npcs.Player;
import game.enums.Side;
import game.environment.Interior;
import game.environment.Room;

import java.util.Random;

/*
* Classname:            Portal.java
*
* Version information:  1.0
*
* Date:                 11/6/2013
*
* Copyright notice:     Copyright (c) 2013 Lizhu Ma & Qiaoli Hu
*/

/**
 * EnvironmentTile: Level background tile
 */
public class Portal extends Entry {
    private boolean activated = false;
    private Random random = new Random();
    
    /**
     * Constructor
     * @param g - This is the game instance, will be removed later (forced to have from entity).
     * @param anImage - Which image we'd like to render
     * @param x - the starting location x
     * @param y - the starting location y
     */
    public Portal(String img, RigidBody rb, Room room, Side side) {
        super(img, rb);
        this.room = room;
        activated = false;
        this.side = side;
    }
    
    @Override
    public void update(long time) {
        // Animate the idle animation for
        // an enabled portal
        
        // Don't worry about this for now,
        // I'll try to create an Animator class
        // or you can try to code it like how it's done
        // in in the Player class.
        
        // The only problem is we have no animations
        // to use for this unless you just use
        // some of the assets we already have.
    }
    
    @Override
    public boolean transport(Player player) {
        if(isActivated() && contains(player)) {
            Interior destRoom = selectRandomRoom();
            player.getRigidBody().setLocation(destRoom.getCenter());
            destRoom.addPlayer(player);
            deactivate();
            return true;
        } else return false;
    }
    
    private Interior selectRandomRoom() {
        int rnd = random.nextInt(MazeGameServer.level.getRooms().size());
        Interior rndRoom = MazeGameServer.level.getRooms().get(rnd);
        if(rndRoom.equals(room)) {
            if(rnd == 0) rnd++;
            else rnd--;
        }
        rndRoom = MazeGameServer.level.getRooms().get(rnd);
        return rndRoom;
    }
    
    public void activate() {
        activated = true;
        rBody.disable();
    }
    
    public void deactivate() {
        activated = false;
        rBody.enable();
    }
    
    public boolean isActivated() {
        return activated;
    }
}
