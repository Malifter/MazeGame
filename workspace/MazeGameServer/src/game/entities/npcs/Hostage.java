package game.entities.npcs;

import engine.Vector2f;
import engine.physics.Collisions;
import engine.physics.RigidBody;

public class Hostage extends Neutral {
    private static final int MAX_STRAY_DISTANCE = 20;
    private static final float SPEED = 1.2f;
    private Player following = null;
    
    public Hostage(String img, RigidBody rb) {
        super(img, rb);
    }
    
    @Override
    public void update(long elapsedTime) {
        // while in idle perhaps look around while in cell
        if(following != null) {
            if(following.isEnabled()) {
                if(Collisions.findDistance(rBody, following.getRigidBody().getLocation()) > MAX_STRAY_DISTANCE) {
                    Vector2f loc = following.getRigidBody().getLocation().add(new Vector2f(0, -1));
                    Vector2f direction = loc.sub(rBody.getMid());
                    rBody.setVelocity(direction.norm().mult(SPEED));
                    rBody.move(elapsedTime);
                } else {
                    rBody.setVelocity(0, 0);
                }
            } else {
                following.setFollower(null);
                following = null;
                rBody.enable();
            }
        }
    }
    
    @Override
    public void interact(Player player) {
        // has item / free'd me.. then follow
        if(following == null) {
            following = player;
            following.setFollower(this);
            rBody.disable();
        }
    }
}
