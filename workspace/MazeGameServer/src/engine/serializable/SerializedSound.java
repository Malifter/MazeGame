package engine.serializable;

/*
* Classname:            SerializedSound.java
*
* Version information:  1.0
*
* Date:                 11/6/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

public class SerializedSound extends SerializedObject {
    private static final long serialVersionUID = 5783455298083405273L;
    private int soundID;

    public SerializedSound(int soundID) {
        super(null);
        this.soundID = soundID;
    }

    public int getSound() {
        return soundID;
    }
    
    public void setSound(int soundID) {
        this.soundID = soundID;
    }
}
