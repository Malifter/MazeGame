package engine.serializable;

import engine.Vector2f;
import game.enums.Pressed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SerializedInputs implements Serializable {
    private static final long serialVersionUID = -6969684933081440833L;
    private List<Pressed> pressed = new ArrayList<Pressed>();
    private Vector2f mouse = new Vector2f();
    
    public SerializedInputs(List<Pressed> pressed, Vector2f mouse) {
        this.pressed = pressed;
        this.mouse = mouse;
    }
    
    public void setMouseLocation(Vector2f mouse) {
        this.mouse = mouse;
    }
    
    public Vector2f getMouseLocation() {
        return mouse;
    }
    
    public void setPressed(List<Pressed> pressed) {
        this.pressed = pressed;
    }
    
    public List<Pressed> getPressed() {
        return pressed;
    }
}
