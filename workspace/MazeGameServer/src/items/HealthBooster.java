package items;

import game.Game;
import game.Player;
public class HealthBooster extends Consumable {
    
    public HealthBooster(Game g, String anImage, int x, int y, float w,
            float h, int itemId, String name, Player p) {
        super(g, anImage, x, y, w, h, itemId, name, p);
        // TODO Auto-generated constructor stub
    }

    @Override
    void consumeItem() {
        // TODO Auto-generated method stub
        //player.addHealth();
    }
}
