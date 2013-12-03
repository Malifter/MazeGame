package game.entities.items;

import engine.physics.RigidBody;
import game.entities.npcs.Player;
import game.enums.ItemType;

public class Shield extends NotConsumable {
    private int durability=10;
    
    public Shield(RigidBody rb) {
        super("items/shield/shield.gif/", rb);
    }
    
    public void takeDamage(int damage, Player player){
        durability = durability - damage;
        if (durability <= 0) {
            player.getInventory().removeItem(ItemType.SHIELD);
        }
    }

    @Override
    public void use(Player p) {
        // TODO Auto-generated method stub
    }
    
    public void pickUp(Player player) {
        disable();
        player.getInventory().addItem(ItemType.SHIELD);
    }
}
