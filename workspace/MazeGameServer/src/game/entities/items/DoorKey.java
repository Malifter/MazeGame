package game.entities.items;

import engine.physics.RigidBody;
import game.entities.npcs.Player;
import game.enums.ItemType;

public class DoorKey extends NotConsumable {
    public DoorKey(RigidBody rb) {
        super("items/dkey/dkey.gif/", rb);
    }

    @Override
    public void pickUp(Player player) {
        disable();
        player.getInventory().addItem(ItemType.DKEY);
    }
}