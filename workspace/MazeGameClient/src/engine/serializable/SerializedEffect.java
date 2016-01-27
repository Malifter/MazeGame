package engine.serializable;

import engine.Vector2f;
import game.enums.AnimationPath;
import game.enums.AnimationState;
import game.enums.Face;

public class SerializedEffect extends SerializedEntity {
    private static final long serialVersionUID = 145609059064380929L;
    private boolean drawOnTop = false;

    public SerializedEffect(String uniqueID, int animSpeed, AnimationPath animPath, AnimationState animState, Face face,
            Vector2f position, Vector2f min, Vector2f max, boolean delete, boolean drawOnTop) {
        super(uniqueID, animSpeed, animPath, animState, face, position, min, max, delete);
        this.drawOnTop = drawOnTop;
    }
    
    public boolean drawOnTop() {
        return drawOnTop;
    }
}
