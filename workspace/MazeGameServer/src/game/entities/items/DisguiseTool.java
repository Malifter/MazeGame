package game.entities.items;

import engine.physics.RigidBody;

public class DisguiseTool extends NotConsumable {
    
    public DisguiseTool(RigidBody rb) {
        super("items/tool/tool.gif/", rb);
    } 
}
