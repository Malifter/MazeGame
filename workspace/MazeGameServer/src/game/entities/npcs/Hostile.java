package game.entities.npcs;

import java.util.ArrayList;

import engine.physics.RigidBody;
import game.entities.Entity;
import game.entities.projectiles.Projectile;

public class Hostile extends NPC {
    protected boolean isDead = false;
    protected int health;
    protected ArrayList<Projectile> shots = new ArrayList<Projectile>();
    protected int damage;
    protected int range;
    
    public Hostile(String img, RigidBody rb) {
        super(img, rb);
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
    
    public void setShots(ArrayList<Projectile> shots) {
        this.shots = shots;
    }

    public ArrayList<Projectile> getShots() {
        return shots;
    }
}
