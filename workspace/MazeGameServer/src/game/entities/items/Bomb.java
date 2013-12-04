package game.entities.items;


import engine.physics.RigidBody;
import game.entities.npcs.Player;
import game.enums.ItemType;

public class Bomb extends NotConsumable {

    
    public Bomb(RigidBody rb) {
        super("items/bomb/bomb.gif/", rb);
    }
    
    public void pickUp(Player player) {
        System.out.println("bomb picked up");
        disable();
        player.getInventory().addItem(ItemType.BOMB);
    }
    
    public void use(Player p) {
        // TODO Auto-generated method stub
        
    }
}


