package game.entities.items;

import engine.physics.RigidBody;

public class DoorKey extends NotConsumable {
    public DoorKey(RigidBody rb) {
        super("items/dkey/dkey.gif/", rb);
    }
    
    public DoorKey getKey(){
        return this;
    }
}