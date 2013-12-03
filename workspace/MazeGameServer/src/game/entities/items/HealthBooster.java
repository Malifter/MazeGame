package game.entities.items;

import engine.physics.RigidBody;
import game.entities.npcs.Player;

public class HealthBooster extends Consumable {
    private static final int HEALTHBOOST=5;
    public HealthBooster(RigidBody rb) {
        super("items/booster/booster.gif/", rb);
    }
    
    public void use(Player player) {
        disable();
        player.setHealthPoints(player.getHealthPoints()+HEALTHBOOST);
    }
}
