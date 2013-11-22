package items;

import game.Game;
import game.Player;

public abstract class Consumable extends Item{
    public Consumable(Game g, String image, int x, int y, float w, float h) {
        super(g, image, x, y, w, h);
        // TODO Auto-generated constructor stub
    }
    
    abstract void consumeItem();
    private void consume(){
        //remove the image from the game
    }
}
