package items;

import game.Game;
import game.Player;

public class Shield extends NotConsumable {
    private int strength;
    private boolean isDestroyed;
    public Shield(Game g, int x, int y, float w,float h) {
        super(g, "imagepath", x, y, w, h);
        isDestroyed = false;
        // TODO Auto-generated constructor stub
    }
    
    public void getAttacked(int damage){
        strength = strength - damage;
        if (strength <= 0) 
            isDestroyed = true;
        }
}
