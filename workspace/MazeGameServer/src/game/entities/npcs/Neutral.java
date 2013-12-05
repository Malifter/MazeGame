package game.entities.npcs;

import engine.physics.RigidBody;
import game.enums.AnimationPath;
import game.enums.Face;

public abstract class Neutral extends NPC {

    public Neutral(AnimationPath ap, RigidBody rb, Face f) {
        super(ap, rb, f);
    }
    
    public abstract void interact(Player player);
}
