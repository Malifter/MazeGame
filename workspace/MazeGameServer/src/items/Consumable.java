package items;

import game.Game;
import game.Player;

public abstract class Consumable extends Item{
    public Consumable(Game g, String anImage, int x, int y, float w,
            float h, int itemId, String name, Player player) {
        super(g, anImage, x, y, w, h, itemId, name, player);
        // TODO Auto-generated constructor stub
    }
    
    abstract void consumeItem();
    private void consume(){
        //remove the image from the game
    }
}
