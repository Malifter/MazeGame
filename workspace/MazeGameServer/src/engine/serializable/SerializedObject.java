package engine.serializable;

/*
* Classname:            SerializedObject.java
*
* Version information:  1.0
*
* Date:                 10/30/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

import java.io.Serializable;


public abstract class SerializedObject implements Serializable {
	private static final long serialVersionUID = -4988010332278134153L;
    protected String uniqueID;

    public SerializedObject(String uniqueID) {
    	super();
    	this.uniqueID = uniqueID;
    }
    
    public boolean equals(Object other) {
    	if (other instanceof SerializedObject) {
    	    SerializedObject otherPair = (SerializedObject) other;
    		return 
    		((  this.uniqueID == otherPair.uniqueID ||
    			( this.uniqueID != null && otherPair.uniqueID != null &&
    			  this.uniqueID.equals(otherPair.uniqueID))) );
    	}

    	return false;
    }

    public String getID() {
    	return uniqueID;
    }
}
