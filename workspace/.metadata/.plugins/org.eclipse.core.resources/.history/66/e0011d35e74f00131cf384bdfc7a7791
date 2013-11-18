package game;

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
public class GateKeeper extends Entity {
    private static final long serialVersionUID = 5788568491539815734L;
    private Portal myPortal;
    private Entity myItem; // TEMPORARY TO ALLOW CODE TO COMPILE
    //private ArrayList<Items>                       // This will either be set manually or randomly selected on construction.
    
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
    public GateKeeper(Game g, String image, int iX, int iY, int x, int y, int w, int h, Portal myPortal) {
        super(g, image, iX, iY, w, h);
        game = (MazeGameServer) g;
        minX = iX-4;
        minY = iY-4;
        width = 100;
        height = 100;
        offsetX = -Math.abs(imageX - minX);
        offsetY = -Math.abs(imageY - minY);
        //calculateBounds();
        this.myPortal = myPortal;
    }
    
    @Override
    public void update(long time) {
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
    
    public boolean contains(Entity player) {
        if(player.getMaxX() >= this.getMaxX())
            return false;
        if(player.getMinX() <=  this.getMinX())
            return false;
        if(player.getMaxY() >=  this.getMaxY())
            return false;
        if(player.getMinY() <=  this.getMinY())
            return false;
        return true;
    }
    
    // This might be moved later if I apply a strategy pattern to the collisions.
    // This happens when a collision is detected between the player and the gatekeeper.
    public boolean negotiate(Entity player) {
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
        
        
        System.out.println("Negociating is going on!!!");
        // if player has myItem selected
            // activate myPortal
        // else if player has gold selected
            // activate myPortal
        // else player has neither of those selected
            // damage player
            // possibly activate myPortal (up for discussion) - but I like this idea
        // anything else that should be here
        player.setMinX(50);
        player.setMinY(50);
        activateWarmHole();
        return false;
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
