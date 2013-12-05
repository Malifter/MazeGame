package engine.serializable;

import engine.Vector2f;
import game.enums.AnimationPath;
import game.enums.AnimationState;
import game.enums.Face;

public class SerializedObstacle extends SerializedEntity {
    private static final long serialVersionUID = -5584262842979598780L;

    public SerializedObstacle(String uniqueID, AnimationPath animPath, AnimationState animState, Face face, Vector2f position, boolean delete) {
        super(uniqueID, animPath, animState, face, position, delete);
    } 
}
