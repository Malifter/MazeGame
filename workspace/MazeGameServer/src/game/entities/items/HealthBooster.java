package game.entities.items;

import engine.physics.RigidBody;
import game.entities.npcs.Player;
import game.enums.AnimationPath;

public class HealthBooster extends Consumable {
    private static final int HEALTHBOOST=5;
    public HealthBooster(RigidBody rb) {
        super(AnimationPath.BOOSTER, rb);
    }
    
    public void use(Player player) {
        player.setHealth(player.getHealth()+HEALTHBOOST);
    }
}
