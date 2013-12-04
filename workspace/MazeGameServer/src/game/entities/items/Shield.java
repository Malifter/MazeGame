package game.entities.items;

import engine.physics.RigidBody;
import game.entities.npcs.Player;
import game.enums.ItemType;

public class Shield extends Consumable {
//    private int durability=10;
    
    public Shield(RigidBody rb) {
        super("items/shield/shield.gif/", rb);
    }
    
//    public void takeDamage(int damage, Player player){
//        durability = durability - damage;
//        if (durability <= 0) {
//            player.getInventory().removeItem(ItemType.SHIELD);
//        }
//    }

    @Override
    public void use(Player player) {
        System.out.println("shield is added");
        player.setShield(true);
        // TODO Auto-generated method stub
    }
}
