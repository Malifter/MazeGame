package game.entities.items;

import engine.physics.RigidBody;
import game.entities.npcs.Player;
import game.enums.AnimationPath;
import game.enums.ItemType;

public class DisguiseTool extends NotConsumable {
    
    public DisguiseTool(RigidBody rb) {
        super(AnimationPath.TOOL, rb);
    } 
    
    public void pickUp(Player player) {
        disable();
        player.getInventory().addItem(ItemType.TOOL);
    }
    
    public void use(Player p) {
        // do nothing
    }    
}
