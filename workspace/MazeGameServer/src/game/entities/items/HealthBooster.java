package game.entities.items;

import engine.physics.RigidBody;
import game.entities.npcs.Player;

public class HealthBooster extends Consumable {
    
    public HealthBooster(RigidBody rb) {
        super("items/booster/booster.gif/", rb);
    }

    public void consumed(Player player) {
        super.consumed(player);
        //player.addHealth();
    }
}
