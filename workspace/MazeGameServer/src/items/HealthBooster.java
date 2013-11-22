package items;

import game.Game;
import game.Player;
public class HealthBooster extends Consumable {
    
    public HealthBooster(Game g, int x, int y, float w,
            float h) {
        super(g, "imagepath", x, y, w, h);
        // TODO Auto-generated constructor stub
    }

    @Override
    void consumeItem() {
        // TODO Auto-generated method stub
        //player.addHealth();
    }
}
