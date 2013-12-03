package engine.render;

import game.Game;

public class GUI {
    // Variables
    // Map of items
    // health
    // lives
    
    // all sprites necessary for rendering GUI
    // list of sprites for all items
    // render a black box (not sprite/image) on top portion... you do this via open gl calls..
    // render health bar (not sprite/image)
    
    
    // render order matters:
    // black box
    // all stuff on top
    
    //could render on top of the camera, openGL how to have overlayer interface
    
    private GUI() {}
    
    public static void init() {
        // init all sprites and stuff here
        // new sprites = Game.getDisplay().getSprite(/*image path*/);
    }
    
    public static void populate(/*player specific arguments here*/) {
        // items, health, lives
        // populate variables with data (mostly quantity)
        // quantities will be rendered via openGL (not images) // look this up
    }
    
    public static void draw() {
        // will call draw function for each sprite
        //sprite.draw(position x, position y);
    }
}
