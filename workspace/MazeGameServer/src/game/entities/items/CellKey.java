package game.entities.items;

import engine.physics.RigidBody;

public class CellKey extends NotConsumable {
    public CellKey(RigidBody rb) {
        super("items/ckey/ckey.gif/", rb);
    }
    
    public CellKey getKey(){
        return this;
    }
}