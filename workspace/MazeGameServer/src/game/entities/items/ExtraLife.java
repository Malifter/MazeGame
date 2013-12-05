package game.entities.items;

import engine.physics.RigidBody;
import game.entities.npcs.Player;
import game.enums.AnimationPath;

public class ExtraLife extends Consumable {
    public ExtraLife(RigidBody rb) {
        super(AnimationPath.ELIFE, rb);
    }
    
    public void use(Player player) {
        player.addLife();
    }
}
