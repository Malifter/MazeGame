package items;

import game.Game;
import game.Player;
public class ExtraLife extends Consumable {
    public ExtraLife(Game g, int x, int y, float w,
            float h) {
        super(g, "imagepath", x, y, w, h);
        // TODO Auto-generated constructor stub
    }

    @Override
    void consumeItem() {
        // TODO Auto-generated method stub
        //player.addLife();
    }
}
