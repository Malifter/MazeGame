package engine.serializable;

import engine.Vector2f;
import game.entities.Entity;
import game.entities.environment.Obstacle;
import game.entities.npcs.Player;
import game.enums.GameState;
import game.enums.Sounds;
import game.environment.Exterior;
import game.environment.Interior;

public class SerializeFactory {
    
    private SerializeFactory() {}
    
    public static SerializedPlayer serialize(Player player) {
        return new SerializedPlayer(player.getUUID(), player.getAnimationPath(), player.getAnimationState(), 
                player.getFaceDirection(), new Vector2f(player.getRigidBody().getLocation()), null);
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
    
    public static SerializedGameState serialize(GameState state) {
        return new SerializedGameState(state);
    }
    
    public static SerializedEntity serialize(Entity entity) {
        return new SerializedEntity(entity.getUUID(), entity.getAnimationPath(), entity.getAnimationState(), 
                entity.getFaceDirection(), new Vector2f(entity.getRigidBody().getLocation()), !entity.isEnabled());
    }
    
    public static SerializedObstacle serialize(Obstacle obstacle) {
        return new SerializedObstacle(obstacle.getUUID(), obstacle.getAnimationPath(), obstacle.getAnimationState(),
                obstacle.getFaceDirection(), new Vector2f(obstacle.getRigidBody().getLocation()), !obstacle.isEnabled());
    }
}
