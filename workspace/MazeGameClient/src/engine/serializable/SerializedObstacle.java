package engine.serializable;

import engine.Vector2f;
import game.enums.AnimationPath;
import game.enums.AnimationState;
import game.enums.Face;

public class SerializedObstacle extends SerializedEntity {
    private static final long serialVersionUID = -5584262842979598780L;
    private boolean moveable = false;

    public SerializedObstacle(String uniqueID, int animSpeed, AnimationPath animPath, AnimationState animState, Face face,
            Vector2f position, Vector2f min, Vector2f max, boolean delete, boolean moveable) {
        super(uniqueID, animSpeed, animPath, animState, face, position, min, max, delete);
        this.moveable = moveable;
    } 
    
    public boolean isMoveable() {
        return moveable;
    }
}
