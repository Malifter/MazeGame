package game.entities.items;

import engine.physics.RigidBody;
import game.entities.npcs.Player;
import game.enums.AnimationPath;
import game.enums.ItemType;

public class CellKey extends NotConsumable {
    public CellKey(RigidBody rb) {
        super(AnimationPath.CKEY, rb);
    }
    
    public void pickUp(Player player) {
        disable();
        player.getInventory().addItem(ItemType.CKEY);
    }
    
    public void use(Player p) {
        // do nothing
    }
}