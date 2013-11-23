package items;

import game.Game;
import game.Player;
public class Gold extends Consumable {
    public Gold(Game g, int x, int y, float w,
            float h) {
        super(g, "items/gold/gold.gif/", x, y, w, h);
        // TODO Auto-generated constructor stub
    }
    public void consumed(Player player) {
        // TODO Auto-generated method stub
        //player.addGold();
    }
}
