package game.entities.items;

import engine.physics.RigidBody;
import game.entities.npcs.Player;
import game.enums.AnimationPath;

public abstract class Consumable extends Item {
    
    public Consumable(AnimationPath ap, RigidBody rb) {
        super(ap, rb);
    }
    
    public void pickUp(Player p){
        disable();
        use(p);
    }
}
