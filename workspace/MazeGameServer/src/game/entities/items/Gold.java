package game.entities.items;

import engine.physics.RigidBody;
import game.entities.npcs.Player;
import game.enums.ItemType;

public class Gold extends NotConsumable {
    public Gold(RigidBody rb) {
        super("items/gold/gold.gif/", rb);
    }

    @Override
    public void use(Player player) {
        disable();
        player.getInventory().addItem(ItemType.GOLD);        
    }

    @Override
    public void pickUp(Player player) {
        disable();
        player.getInventory().addItem(ItemType.GOLD);
    }
}
