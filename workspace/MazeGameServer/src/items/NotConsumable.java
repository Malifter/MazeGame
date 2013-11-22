package items;

import game.Game;
import game.Player;

public abstract class NotConsumable extends Item{
    public NotConsumable(Game g, String image, int x, int y, float w, float h) {
        super(g, image, x, y, w, h);
        // TODO Auto-generated constructor stub
    }
    
    public void addToInventory(){
        //remove the image from the game
    }
}
