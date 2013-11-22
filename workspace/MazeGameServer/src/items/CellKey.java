package items;

import game.Game;
import game.Player;

public class CellKey extends NotConsumable {
    public CellKey(Game g, int x, int y, float w,
            float h) {
        super(g, "imagepath", x, y, w, h);
        // TODO Auto-generated constructor stub
    }
    
    public CellKey getKey(){
        return this;
    }
}