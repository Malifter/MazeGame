package game.entities.items;

import engine.physics.RigidBody;
import game.entities.npcs.Player;
import game.enums.AnimationPath;
import game.enums.ItemType;

public class Bomb extends NotConsumable {

    
    public Bomb(RigidBody rb) {
        super(AnimationPath.BOMB, rb);
    }
    
    public void pickUp(Player player) {
        disable();
        player.getInventory().addItem(ItemType.BOMB);
    }
    
    public void use(Player p) {
        // do nothing
    }
}


