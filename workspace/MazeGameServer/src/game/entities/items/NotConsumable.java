package game.entities.items;

import engine.physics.RigidBody;
import game.entities.npcs.Player;

public abstract class NotConsumable extends Item{
    public NotConsumable(String img, RigidBody rb) {
        super(img, rb);
    }
    
//    public void pickUp(Player player){
//        disable();
//        player.getInventory().addItem(ItemType.);
//    }
}
