package game.entities.environment;

import engine.physics.RigidBody;
import game.entities.Entity;
import game.entities.npcs.Player;
import game.enums.Side;
import game.environment.Room;

public class Entry extends Entity {
    protected Room room;
    protected Side side;
    
    public Entry(String img, RigidBody rb) {
        super(img, rb);
    }
    
    public Room getRoom() {
        return room;
    }
    
    public Side getSide() {
        return side;
    }
    
    public boolean transport(Player player) {
        return false;
    }
    
    protected boolean contains(Player player) {
        if(player.getRigidBody().getMax().x >= rBody.getMax().x)
            return false;
        if(player.getRigidBody().getMin().x <= rBody.getMin().x)
            return false;
        if(player.getRigidBody().getMax().y >= rBody.getMax().y)
            return false;
        if(player.getRigidBody().getMin().y <= rBody.getMin().y)
            return false;
        return true;
    }
}
