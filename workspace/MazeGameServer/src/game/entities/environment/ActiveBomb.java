package game.entities.environment;

import engine.physics.RigidBody;
import game.entities.EntityFactory;
import game.entities.npcs.Hostile;
import game.enums.AnimationPath;
import game.enums.AnimationState;
import game.enums.EffectType;
import game.environment.Room;

// TODO: ABomb shouldn't be considered an item since it can't be picked back up
// This should be a neutral entity instead (or possibly a hostile entity if we
// want a mechanic where the player can shoot the bomb and explode it
public class ActiveBomb extends Obstacle {
    //private static final int BOMB_POWER = 10;
    private static final int FUSE_TIME = 3000;
    private static final int ABOUT_TO_BLOW = 500;
    private static final int FLICKER_SPEED = 500;
    private int fuseTime = FUSE_TIME;
    private int flickerTime = FLICKER_SPEED;
    private Hostile owner;
    private Room room;
    
    public ActiveBomb(RigidBody rb, Room room, Hostile source){
        super(AnimationPath.BOMB, rb);
        animState = AnimationState.ACTIVE;
        owner = source;
        moveable = true;
        destructable = true;
        this.room = room;
    }
    
    @Override
    public void update(long elapsedTime) {
        fuseTime -= elapsedTime;
        if(fuseTime < ABOUT_TO_BLOW) {
            animState = AnimationState.ACTIVE;
        } else {
            flickerTime += elapsedTime;
            if(flickerTime >= FLICKER_SPEED * (fuseTime / (float) FUSE_TIME)) {
                if(animState == AnimationState.IDLE) {
                    animState = AnimationState.ACTIVE;
                } else {
                    animState = AnimationState.IDLE;
                }
                flickerTime = 0;
            }
            
        }
        if(fuseTime <= 0) {
            explode();
        }
    }

    public void explode() {
        room.addLater(EntityFactory.createExplosion(this, owner));
        room.addLater(EntityFactory.createEffect(EffectType.EXPLOSION, this));
        //room.addExplosion(EntityFactory.createExplosion(this, owner));
        //room.addEffect(EntityFactory.createEffect(EffectType.EXPLOSION, this));
        disable();
    }
}