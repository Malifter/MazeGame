package game.entities.items;

import engine.physics.RigidBody;
import game.entities.npcs.Player;
import game.enums.ItemType;

public class DisguiseTool extends NotConsumable {
    
    public DisguiseTool(RigidBody rb) {
        super("items/tool/tool.gif/", rb);
    } 
    
    public void pickUp(Player player) {
        System.out.println("disguishtool picked up");
        disable();
        player.getInventory().addItem(ItemType.TOOL);
    }
    
    @Override
    public void use(Player p) {
        // TODO Auto-generated method stub
    }    
    
}
