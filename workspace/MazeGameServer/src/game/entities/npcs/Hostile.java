package game.entities.npcs;

import engine.physics.RigidBody;
import game.enums.AnimationPath;
import game.enums.AnimationState;
import game.enums.Face;
import game.environment.Room;

public abstract class Hostile extends NPC {
    protected boolean dead = false;
    protected boolean flying = false;
    protected int health;
    protected int damage;
    protected int range;
    protected int numProjectiles = 0;
    protected Room room;
    
    public Hostile(AnimationPath ap, RigidBody rb, Room room, Face f) {
        super(ap, rb, f);
        this.room = room;
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
    
    public int getRange() {
        return range;
    }
    
    public void takeDamage(int d) {
        //GameEngine.playSound(((MazeGameServer)game).sound_hit);
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
        other.takeDamage(damage);
    }
    
    public boolean isDead() {
        return dead;
    }
    
    public boolean isFlying() {
        return flying;
    }
}
