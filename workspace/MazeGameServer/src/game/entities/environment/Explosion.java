package game.entities.environment;

import engine.physics.Collisions;
import engine.physics.RigidBody;
import game.entities.Entity;
import game.entities.npcs.Hostile;
import game.enums.AnimationPath;

public class Explosion extends Entity {
    private static final long MAX_EXPLOSION_TIME = 250;
    private static final int EXPLOSION_DAMAGE_PER_TICK = 10;
    private static final int MAX_EXPLOSION_TICKS = 5;
    private static final int MIN_AREA_OF_EFFECT = 10;
    private static final int MAX_AREA_OF_EFFECT = 45;
    private int areaOfEffect = MIN_AREA_OF_EFFECT;
    private int explosionTick = 0;
    private int explosionTime = 0;
    protected Hostile owner;

    public Explosion(RigidBody rb, Hostile source) {
        super(null, rb);
        owner = source;
        rBody.setRadius(areaOfEffect);
    }
    
    public Hostile getSource() {
        return owner;
    }

    public void update(long elapsedTime) {
        if(isEnabled()) {
            explosionTime += elapsedTime;
            if(explosionTime > MAX_EXPLOSION_TIME) {
                explosionTick = MAX_EXPLOSION_TICKS;
                explosionTime = 0;
                disable();
            }
            explosionTick = (int) ((elapsedTime * MAX_EXPLOSION_TICKS) / MAX_EXPLOSION_TIME);
            areaOfEffect = (int) (MIN_AREA_OF_EFFECT + ((MAX_AREA_OF_EFFECT - MIN_AREA_OF_EFFECT)*(explosionTime/(float)MAX_EXPLOSION_TIME)));
            rBody.setRadius(areaOfEffect);
        }
    }
    
    public int getExplosionDamage(Entity entity) {
        if(explosionTick < MAX_EXPLOSION_TICKS) {
            return EXPLOSION_DAMAGE_PER_TICK;
        } else {
            return 0;
        }
    }
    
    public boolean inRange(Entity entity) {
        if(Collisions.findDistance(entity.getRigidBody(), this.getRigidBody()) <= areaOfEffect) {
            return true;
        }
        return false;
    }
}
