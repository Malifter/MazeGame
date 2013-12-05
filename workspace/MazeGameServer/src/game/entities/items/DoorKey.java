package game.entities.items;

import engine.physics.RigidBody;
import game.entities.npcs.Player;
import game.enums.AnimationPath;
import game.enums.ItemType;

public class DoorKey extends NotConsumable {
    public DoorKey(RigidBody rb) {
        super(AnimationPath.DKEY, rb);
    }

    public void pickUp(Player player) {
        disable();
        player.getInventory().addItem(ItemType.DKEY);
    }
    
    public void use(Player p) {
        // do nothing
    }
}