package engine.serializable;

/*
* Classname:            SerializedGameState.java
*
* Version information:  1.0
*
* Date:                 11/6/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

public class SerializedGameState extends SerializedObject {
    private static final long serialVersionUID = 4643754828530905782L;
    //private GameState state;

    public SerializedGameState(/*GameState state*/) {
        super(null);
        //this.state = state;
    }

    public /*GameState*/ void getGameState() {
        //return state;
    }
    
    public void setGameState(/*GameState state*/) {
        //this.state = state;
    }
}
