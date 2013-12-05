package game.entities.npcs;

import engine.Vector2f;
import engine.physics.Collisions;
import engine.physics.RigidBody;
import game.enums.AnimationPath;
import game.enums.AnimationState;
import game.enums.Face;

public class Hostage extends Neutral {
    private static final int MAX_STRAY_DISTANCE = 20;
    private static final long MAX_FACE_TIME = 3000;
    private static final float SPEED = 1.2f;
    private Player following = null;
    private long idleTime = 0;
    
    public Hostage(RigidBody rb) {
        super(AnimationPath.HOSTAGE, rb, Face.randomFace());
    }
    
    @Override
    public void update(long elapsedTime) {
        if(following != null) {
            if(following.isEnabled()) {
                if(Collisions.findDistance(rBody, following.getRigidBody().getLocation()) > MAX_STRAY_DISTANCE) {
                    Vector2f loc = following.getRigidBody().getLocation().add(new Vector2f(0, -1));
                    Vector2f direction = loc.sub(rBody.getMid());
                    if(Math.abs(direction.y) > Math.abs(direction.x)) {
                        // Up or Down
                        if(direction.y > 0) {
                            facing = Face.DOWN;
                        } else {
                            facing = Face.UP;
                        }
                    } else {
                        // Right or Left
                        if(direction.x > 0) {
                            facing = Face.RIGHT;
                        } else {
                            facing = Face.LEFT;
                        }
                    }
                    rBody.setVelocity(direction.norm().mult(SPEED));
                    rBody.move(elapsedTime);
                    animState = AnimationState.RUN;
                } else {
                    rBody.setVelocity(0, 0);
                    animState = AnimationState.IDLE;
                }
            } else {
                following.setFollower(null);
                following = null;
                rBody.enable();
                animState = AnimationState.IDLE;
            }
        } else {
            idleTime += elapsedTime;
            if(idleTime > MAX_FACE_TIME) {
                facing = Face.randomFace();
                idleTime = 0;
            }
            animState = AnimationState.IDLE;
        }
    }
    
    @Override
    public void interact(Player player) {
        if(following == null) {
            following = player;
            following.setFollower(this);
            rBody.disable();
            facing = Face.DOWN;
        }
    }
}
