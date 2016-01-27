package game.entities.projectiles;

import engine.Vector2f;
import engine.physics.Collisions;
import engine.physics.RigidBody;
import game.entities.npcs.Hostile;
import game.enums.AnimationPath;
import game.enums.Face;

public class Directed extends Projectile {
    private Vector2f origin;
    protected static final float SPEED = 1.1f;

    public Directed(RigidBody rb, Face direction, Hostile hostile) {
        super(AnimationPath.PROJECTILE_1, rb, hostile);
        origin = new Vector2f(owner.getRigidBody().getLocation());
        damage = owner.getDamage();

        float speedX = SPEED * owner.getRigidBody().getVelocity().x * 0.5f;
        float speedY = SPEED * owner.getRigidBody().getVelocity().y * 0.5f;
        switch(direction) {
            case RIGHT:
                rBody.setVelocity(SPEED, speedY);
                break;
            case LEFT:
                rBody.setVelocity(-SPEED, speedY);
                break;
            case UP:
                rBody.setVelocity(speedX, -SPEED);
                break;
            case DOWN:
                rBody.setVelocity(speedX, SPEED);
                break;
            case NONE:
                disable();
                break;
        }
    }

    @Override
    public void update(long elapsedTime) {
        rBody.move(elapsedTime);
        
        if(Collisions.findDistance(rBody, origin) > MAX_RANGE) {
            collide();
        }
    }

}
