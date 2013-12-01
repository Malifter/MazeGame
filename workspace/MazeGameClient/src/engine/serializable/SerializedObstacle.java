package engine.serializable;

import engine.Vector2f;

public class SerializedObstacle extends SerializedEntity {
    private static final long serialVersionUID = -5584262842979598780L;

    public SerializedObstacle(String uniqueID, String image, Vector2f position, boolean delete) {
        super(uniqueID, image, position, delete);
    } 
}
