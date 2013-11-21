package items;

import game.Game;
import game.Player;

public class Shield extends NotConsumable {
    int strength;
    
    public Shield(Game g, String anImage, int x, int y, float w, float h,
            int itemId, String name, Player p) {
        super(g, anImage, x, y, w, h, itemId, name, p);
        // TODO Auto-generated constructor stub
    }
    
    public void getAttacked(int damage){

        strength = strength - damage;
        if (strength <= 0) 
            isDestroyed = true;

        }
}
