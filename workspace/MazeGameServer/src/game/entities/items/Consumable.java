package game.entities.items;

import engine.physics.RigidBody;
import game.entities.npcs.Player;

public abstract class Consumable extends Item {
    public Consumable(String image, RigidBody rb) {
        super(image, rb);
    }
    
    public void pickUp(Player p){
        disable();
        use(p);
    }
}
