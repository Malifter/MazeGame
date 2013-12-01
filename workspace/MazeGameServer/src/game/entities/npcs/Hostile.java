package game.entities.npcs;

import engine.physics.RigidBody;
import game.environment.Room;

public class Hostile extends NPC {
    protected boolean isDead = false;
    protected int health;
    protected int damage;
    protected int range;
    protected int numProjectiles = 0;
    protected Room room;
    
    public Hostile(String img, RigidBody rb, Room room) {
        super(img, rb);
        this.room = room;
    }
    
    public void setHealthPoints(int hp) {
        if(hp < 0) {
            this.health = 0;
        }
        else {
            this.health = hp;
        }
    }

    public int getHealthPoints() {
        return health;
    }
    
    public void checkHealth() {
        if(health == 0) {
            disable();
        }
    }
    
    public void setDamage(int d) {
        damage = d;
    }
    
    public int getDamage() {
        return damage;
    }
    
    public int getRange() {
        return range;
    }
    
    public void takeDamage(int d) {
        setHealthPoints(health-d);
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
}
