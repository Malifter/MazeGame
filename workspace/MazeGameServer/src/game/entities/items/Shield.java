package game.entities.items;

import engine.physics.RigidBody;
import game.entities.npcs.Player;
import game.enums.ItemType;

public class Shield extends NotConsumable {
    private int strength;
    private boolean isDestroyed;
    
    public Shield(RigidBody rb) {
        super("items/shield/shield.gif/", rb);
        isDestroyed = false;
    }
    
    public void getAttacked(int damage){
        strength = strength - damage;
        if (strength <= 0) {
            isDestroyed = true;
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
