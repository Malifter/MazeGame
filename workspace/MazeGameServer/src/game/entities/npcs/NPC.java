package game.entities.npcs;

import engine.physics.RigidBody;
import game.entities.Entity;
import game.enums.AnimationPath;
import game.enums.Face;

public abstract class NPC extends Entity {
    
    public NPC(AnimationPath ap, RigidBody rb, Face facing) {
        super(ap, rb);
        this.facing = facing;
    }
}
