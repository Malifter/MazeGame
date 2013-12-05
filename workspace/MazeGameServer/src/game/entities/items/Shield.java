package game.entities.items;

import engine.physics.RigidBody;
import game.entities.npcs.Player;
import game.enums.AnimationPath;

public class Shield extends Consumable {
//    private int durability=10;
    
    public Shield(RigidBody rb) {
        super(AnimationPath.SHIELD, rb);
    }
    
//    public void takeDamage(int damage, Player player){
//        durability = durability - damage;
//        if (durability <= 0) {
//            player.getInventory().removeItem(ItemType.SHIELD);
//        }
//    }

    public void use(Player player) {
        player.addShield();
    }
}
