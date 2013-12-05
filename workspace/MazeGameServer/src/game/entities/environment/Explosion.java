package game.entities.environment;

import engine.physics.Collisions;
import engine.physics.RigidBody;
import game.entities.Entity;
import game.enums.AnimationPath;

public class Explosion extends Entity {
    private static final long MAX_EXPLOSION_TIME = 1250;
    private static final int EXPLOSION_DAMAGE_PER_TICK = 50;
    private static final int MAX_EXPLOSION_TICKS = 1;
    private static final int MIN_AREA_OF_EFFECT = 25;
    private static final int MAX_AREA_OF_EFFECT = 60;
    private int areaOfEffect = MIN_AREA_OF_EFFECT;
    private int explosionTick = 0;
    private int explosionTime = 0;

    public Explosion(RigidBody rb) {
        super(AnimationPath.EXPLOSION, rb);
    }

    public void update(long elapsedTime) {
        if(isEnabled()) {
            explosionTime += elapsedTime;
            if(explosionTime > MAX_EXPLOSION_TIME) {
                explosionTick = MAX_EXPLOSION_TICKS;
                explosionTime = 0;
                disable();
            }
            explosionTick++;
            areaOfEffect = (int) (MIN_AREA_OF_EFFECT + ((MAX_AREA_OF_EFFECT - MIN_AREA_OF_EFFECT)*(explosionTime/(float)MAX_EXPLOSION_TIME)));
        }
    }
    
    public int getExplosionDamage(Entity entity) {
        if(Collisions.findDistance(entity.getRigidBody(), this.getRigidBody()) <= areaOfEffect && explosionTick < MAX_EXPLOSION_TICKS) {
            return EXPLOSION_DAMAGE_PER_TICK;
        } else {
            return 0;
        }
    }
}
