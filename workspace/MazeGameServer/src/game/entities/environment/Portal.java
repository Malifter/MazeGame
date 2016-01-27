package game.entities.environment;

import engine.physics.RigidBody;
import game.MazeGameServer;
import game.entities.npcs.Player;
import game.enums.AnimationPath;
import game.enums.AnimationState;
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
    public Portal(RigidBody rb, RigidBody zone, Room room, Side side) {
        super(AnimationPath.PORTAL, rb, zone, room, side);
        deactivate();
    }
    
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
        animState = AnimationState.ACTIVE;
    }
    
    public void deactivate() {
        activated = false;
        rBody.enable();
        animState = AnimationState.IDLE;
    }
    
    public boolean isActivated() {
        return activated;
    }
}
