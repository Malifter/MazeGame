package engine.inputhandler;
import java.io.Serializable;

/*
* Classname:            Button.java
*
* Version information:  1.0
*
* Date:                 10/30/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

/**
 * 
 * Button
 */
public class Button extends Input implements Serializable {
    private static final long serialVersionUID = 8076775535697065560L;
    
    private boolean down;
    
    public Button(PhysicalInput[] p) {
        super(p);
        down = false;
    }
    
    public boolean isDown() {
        return down;
    }
    
    public void setDown(boolean down) {
        this.down = down;
    }
    
}
