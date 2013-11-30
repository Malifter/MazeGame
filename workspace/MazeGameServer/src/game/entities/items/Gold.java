package game.entities.items;

import engine.physics.RigidBody;
import game.entities.npcs.Player;

public class Gold extends Consumable {
    public Gold(RigidBody rb) {
        super("items/gold/gold.gif/", rb);
    }
    public void consumed(Player player) {
        //player.addGold();
    }
}
