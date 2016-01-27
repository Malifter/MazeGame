package game.entities.effects;

import engine.Vector2f;
import engine.physics.RigidBody;
import engine.serializable.SerializedObject;
import engine.serializable.SerializedObstacle;
import game.GameEngine;
import game.entities.Entity;
import game.enums.AnimationPath;
import game.enums.AnimationState;
import game.enums.EffectType;

public class Effect extends Entity {
    
    private static final long EFFECT_DURATION = 175; // Animator.ANIMATION_SPEED;
    private int effectTime = 0;

    public Effect(EffectType type, RigidBody rb) {
        super(type.getAnimPath(), rb);
        animState = AnimationState.DEATH;
        rBody.disable();
    }
    
    public boolean drawOnTop() {
        // TODO: As there are more effects, this might be something to think about.
        return animPath == AnimationPath.EXPLOSION || animPath == AnimationPath.DEATH_EFFECT;
    }

    @Override
    public void update(long elapsedTime) {
        effectTime += elapsedTime;
        if(effectTime >= EFFECT_DURATION) {
            disable();
        }
    }
    
    @Override
    public SerializedObject serialize() {
        return new SerializedObstacle(uuid, 100, animPath, animState, facing, new Vector2f(rBody.getLocation()),
                GameEngine.DEBUG ? new Vector2f(rBody.getMin()) : null,
                GameEngine.DEBUG ? new Vector2f(rBody.getMax()) : null,
                !isEnabled(), drawOnTop());
    }
}
