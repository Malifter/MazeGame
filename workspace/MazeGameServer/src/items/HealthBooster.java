package items;

import game.Game;
import game.Player;
public class HealthBooster extends Consumable {
    
    public HealthBooster(Game g, int x, int y, float w,
            float h) {
        super(g, "items/booster/booster.gif/", x, y, w, h);
        // TODO Auto-generated constructor stub
    }

    void consumed(Player player) {
        // TODO Auto-generated method stub
        //player.addHealth();
    }
}
