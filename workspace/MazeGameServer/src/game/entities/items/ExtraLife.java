package game.entities.items;

import engine.physics.RigidBody;
import game.entities.npcs.Player;

public class ExtraLife extends Consumable {
    public ExtraLife(RigidBody rb) {
        super("items/elife/elife.gif/", rb);
    }

    @Override
    public void consumed(Player player) {

    }
}
