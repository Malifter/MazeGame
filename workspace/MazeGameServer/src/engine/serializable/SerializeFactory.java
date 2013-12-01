package engine.serializable;

import engine.Vector2f;
import game.entities.Entity;
import game.entities.npcs.Player;
import game.enums.Sounds;
import game.environment.Exterior;
import game.environment.Interior;

public class SerializeFactory {
    
    private SerializeFactory() {}
    
    public static SerializedPlayer serialize(Player player) {
        return new SerializedPlayer(player.getUUID(), player.getImage(), new Vector2f(player.getRigidBody().getLocation()));
    }
    
    public static SerializedSound serialize(Sounds sound) {
        return new SerializedSound(sound.getValue());
    }
    
    public static SerializedRoom serialize(Interior room) {
        return new SerializedRoom(room.getLocation(), room.getRoomID());
    }
    
    public static SerializedRoom serialize(Exterior room) {
        return new SerializedRoom(null, room.getRoomID());
    }
    
    public static SerializedGameState serialize(/*GameState state*/) {
        return new SerializedGameState(/*state*/);
    }
    
    public static SerializedEntity serialize(Entity entity) {
        return new SerializedEntity(entity.getUUID(), entity.getImage(), new Vector2f(entity.getRigidBody().getLocation()), !entity.isEnabled());
    }
}
