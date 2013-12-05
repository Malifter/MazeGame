package game.entities.npcs;


import java.util.Random;

import engine.Vector2f;
import engine.physics.Collisions;
import engine.physics.RigidBody;
import game.entities.environment.Portal;
import game.enums.*;

/*
* Classname:            Gatekeeper.java
*
* Version information:  1.0
*
* Date:                 11/6/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

/**
 * Gatekeeper activates a portal if given the appropriate items
 */
public class GateKeeper extends Neutral {
    private Portal myPortal;
    private Vector2f origin;
    private static final int MAX_STRAY_DISTANCE = 8;
    private static final int MAX_STRAY_TIME = 1000;
    private long strayTime = 0;
    private int itemIndex = 0;
    private static ItemType[] VALUES = ItemType.values();
    private static final Random RANDOM = new Random();
    private static final int SIZE = VALUES.length;
    private static int damage = 2;
    private static Face originalFace;
    
    //private ArrayList<Items> // This will either be set manually or randomly selected on construction.
    
    /**
     * Constructor: <add description>
     */
    public GateKeeper(RigidBody rb, Portal myPortal, Face face) {
        super(AnimationPath.GATEKEEPER, rb, face);
        originalFace = face;
        origin = new Vector2f(rb.getLocation());
        this.myPortal = myPortal;
    }
    


    @Override
    public void update(long elapsedTime) {
        if(Collisions.findDistance(rBody, origin) > MAX_STRAY_DISTANCE) {
            strayTime += elapsedTime;
        } else {
            strayTime = 0;
        }
        if(strayTime > MAX_STRAY_TIME) {
            // travel back to origin
            Vector2f direction = origin.sub(rBody.getLocation());
            if(Math.abs(direction.y) > Math.abs(direction.x)) {
                // Up or Down
                if(direction.y > 0) {
                    facing = Face.DOWN;
                } else {
                    facing = Face.UP;
                }
            } else {
                // Right or Left
                if(direction.x > 0) {
                    facing = Face.RIGHT;
                } else {
                    facing = Face.LEFT;
                }
            }
            rBody.setVelocity(direction.norm());
            rBody.move(elapsedTime);
            animState = AnimationState.RUN;
        } else {
            facing = originalFace;
            animState = AnimationState.IDLE;
            rBody.setVelocity(0, 0);
        }
    }

    @Override
    public void interact(Player player) {
        negotiate(player);
    }

    public void negotiate(Player player) {
        ItemType myItem = randomItem(); 
        if(!myPortal.isActivated()){
            if (player.getInventory().getItem().equals(myItem)) {
                activateWarmHole();
            }
            else if (player.getInventory().getItem().equals(ItemType.GOLD)) {
                activateWarmHole();
            }
            else 
            {
                player.takeDamage(damage);
                activateWarmHole();
            }
            
        }
    }
    
    private ItemType randomItem(){
        ItemType item;
        do {
            item = VALUES[RANDOM.nextInt(SIZE)];

        } while(item.getIndex() == -1);
        System.out.println("!!!!!!!!!!!!!!!!!"+item.toString());
        return item;
        
    }
    
    public Portal getPortal() {
        return this.myPortal;
    }
    
    public void activateWarmHole(){
        this.myPortal.activate();
    }
    
    public void deactivateWarmHole(){
        this.myPortal.deactivate();
    }
    
   
}
