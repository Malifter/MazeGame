package game.entities.npcs;

import engine.physics.RigidBody;
import game.entities.Entity;
import game.entities.EntityFactory;
import game.enums.AnimationPath;
import game.enums.EffectType;
import game.enums.Face;
import game.environment.Room;

public abstract class Hostile extends NPC {
    protected boolean dead = false;
    protected boolean flying = false;
    protected int health;
    protected int damage;
    protected int attackRange;
    protected int aggroRange;
    protected int numProjectiles = 0;
    protected Room room;
    
    public Hostile(AnimationPath ap, RigidBody rb, Room room, Face f) {
        super(ap, rb, f);
        this.room = room;
    }
    
    @Override
    public void disable() {
        if(dead && isEnabled()) {
            // create death effect
            room.addEffect(EntityFactory.createEffect(EffectType.DEATH, this));
            // create corpse
            room.addObstacle(EntityFactory.createCorpse(this));
        }
        super.disable();
    }
    
    public void setHealth(int hp) {
        if(hp < 0) {
            health = 0;
            dead = true;
            //animState = AnimationState.DEAD;
        }
        else {
            health = hp;
        }
    }

    public int getHealth() {
        return health;
    }
    
    public int getDamage() {
        return damage;
    }
    
    public int getAttackRange() {
        return attackRange;
    }
    
    public void takeDamage(Entity source, int d) {
        //GameEngine.playSound(((MazeGameServer)game).sound_hit);
        // TODO: Currently most things don't care about damage source, so this is for extension only.
        // Perhaps this needs to be changed so that we don't have to deal with passing in something
        // that isn't used. this is a bad implementation.
        setHealth(health-d);
    }
    
    public void setRoom(Room room) {
        this.room = room;
    }
    
    public Room getRoom() {
        return room;
    }
    
    public void addProjectile() {
        numProjectiles++;
    }
    
    public void removeProjectile() {
        numProjectiles--;
    }
    
    public void attack(Hostile other) {
        if(!dead) {
            other.takeDamage(this, damage);
        }
    }
    
    public boolean isDead() {
        return dead;
    }
    
    public boolean isFlying() {
        return flying;
    }
}
