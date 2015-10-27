package game.entities.environment;

import engine.physics.RigidBody;
import game.enums.AnimationPath;
import game.enums.AnimationState;

public class Corpse extends Obstacle {

    public Corpse(AnimationPath ap, RigidBody rb) {
        super(ap, rb);
        moveable = true;
        heavy = true;
        animState = AnimationState.CORPSE;
    }

}
