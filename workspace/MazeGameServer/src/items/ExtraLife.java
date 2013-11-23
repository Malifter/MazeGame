package items;

import game.Game;
import game.Player;
public class ExtraLife extends Consumable {
    public ExtraLife(Game g, int x, int y, float w,
            float h) {
        super(g, "items/elife/elife.gif/", x, y, w, h);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void consumed(Player player) {
        // TODO Auto-generated method stub
        
    }
}
