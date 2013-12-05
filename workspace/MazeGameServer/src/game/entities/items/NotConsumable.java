package game.entities.items;

import engine.physics.RigidBody;
import game.enums.AnimationPath;

public abstract class NotConsumable extends Item{
    
    public NotConsumable(AnimationPath ap, RigidBody rb) {
        super(ap, rb);
    }
}
