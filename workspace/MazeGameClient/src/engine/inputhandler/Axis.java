package engine.inputhandler;


/*
* Classname:            Axis.java
*
* Version information:  1.0
*
* Date:                 10/30/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

/**
 * 
 * Axis: 
 */
public class Axis extends Input {
    private static final long serialVersionUID = -7624245331859910619L;
    
    private float value; // should be between -1 and +1, or whatever
	
	public Axis (PhysicalInput[] p) {
		super(p);
		value = 0;
	}
	
	public void setValue(float value) {
		this.value = value;
	}
	
	public float getValue() {
		return this.value;
	}
}
