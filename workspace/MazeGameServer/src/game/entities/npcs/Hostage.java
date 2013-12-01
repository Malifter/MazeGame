package game.entities.npcs;

import engine.physics.RigidBody;

public class Hostage extends Neutral {

    public Hostage(String img, RigidBody rb) {
        super(img, rb);
    }
    
    @Override
    public void update(long elapsedTime) {
        
    }
    
    @Override
    public void interact(Player player) {
        
    }
}
