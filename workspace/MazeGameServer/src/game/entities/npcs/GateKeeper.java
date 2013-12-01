package game.entities.npcs;

import engine.Vector2f;
import engine.physics.Collisions;
import engine.physics.RigidBody;
import game.entities.environment.Portal;

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
    private static final long serialVersionUID = 5788568491539815734L;
    private Portal myPortal;
    private Vector2f origin;
    private static final int MAX_STRAY_DISTANCE = 8;
    private static final int MAX_STRAY_TIME = 1000;
    private long strayTime = 0;
    //private ArrayList<Items> // This will either be set manually or randomly selected on construction.
    
    /**
     * This will change
     * Constructor: <add description>
     * @param g - instance of the game - will be removed later
     * @param image - image of the gatekeeper
     * @param iX - image location
     * @param iY
     * @param x - finds center of solid body
     * @param y
     * @param w - width and height of solid body
     * @param h
     * @param room - room it's spawned in
     */
    public GateKeeper(String image, RigidBody rb, Portal myPortal) {
        super(image, rb);
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
            rBody.setVelocity(direction.norm());
            rBody.move(elapsedTime);
        } else {
            rBody.setVelocity(0, 0);
        }
        // Here we'd animate the idle animation
        
        // Don't worry about this for now,
        // I'll try to create an Animator class
        // or you can try to code it like how it's done
        // in in the Player class.
        
        // You would have to use images we already have
        // unless you wan't to find some online.
        
        // Potentially in the future if a gatekeeper
        // has some dynamic physics that causes them
        // to be moved by the player or by an explosion
        // maybe they can be updated to move back to their
        // original position and face direction
        // this is not needed for now
    }

    @Override
    public void interact(Player player) {
        negotiate(player);
    }

    // This might be moved later if I apply a strategy pattern to the collisions.
    // This happens when a collision is detected between the player and the gatekeeper.
    public void negotiate(Player player) {
        if(!myPortal.isActivated()){
            
            // NOTE: Player doesn't yet have an inventory or items, so use psuedo
            // code if necessary or comment out the code you think should be placed here.
            
            //items = player.getItems() //get the item list from players
            //if (items matches the negociator's wish list)
            //{
                //remove player's item from inventory
                //open warmwhoe
            //}
            //else{
                //remove a certain amount of gold from player
                //open warmwhoe
            //}
            //else{
                //reduce player's health
                //open warmwhoe
            //}
            //push player so that collision stops  
            
            
            // if player has myItem selected
                // activate myPortal
            // else if player has gold selected
                // activate myPortal
            // else player has neither of those selected
                // damage player
                // possibly activate myPortal (up for discussion) - but I like this idea
            // anything else that should be here
            activateWarmHole();
        }
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
