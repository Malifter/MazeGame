package game.entities.items;

import engine.physics.RigidBody;

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
}
