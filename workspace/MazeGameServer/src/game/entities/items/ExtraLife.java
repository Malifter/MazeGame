package game.entities.items;

import engine.physics.RigidBody;
import game.entities.npcs.Player;

public class ExtraLife extends Consumable {
    public ExtraLife(RigidBody rb) {
        super("items/elife/elife.gif/", rb);
    }
    

    public void use(Player player) {
        System.out.println("life is added");
        player.addLife();
    }
}
