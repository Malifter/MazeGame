package game.entities.items;

import engine.physics.RigidBody;
import game.entities.npcs.Player;
import game.enums.AnimationPath;
import game.enums.ItemType;

public class Gold extends NotConsumable {
    public Gold(RigidBody rb) {
        super(AnimationPath.GOLD, rb);
    }

    public void pickUp(Player player) {
        disable();
        player.getInventory().addItem(ItemType.GOLD);
    }
    
    public void use(Player player) {
        // do nothing
    }
}
