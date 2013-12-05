package game.entities.items;

import engine.physics.RigidBody;
import game.entities.npcs.Player;
import game.enums.ItemType;

public class CellKey extends NotConsumable {
    public CellKey(RigidBody rb) {
        super("items/ckey/ckey.gif/", rb);
    }
    
    public void pickUp(Player player) {
        System.out.println("Ckey picked up");
        disable();
        player.getInventory().addItem(ItemType.CKEY);
    }
}