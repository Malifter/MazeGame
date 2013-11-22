package items;

import game.Game;
import game.Player;

public class DoorKey extends NotConsumable {
    public DoorKey(Game g, int x, int y, float w,
            float h) {
        super(g, "imagepath", x, y, w, h);
        // TODO Auto-generated constructor stub
    }
    
    public DoorKey getKey(){
        return this;
    }
}