package engine;

/*
* Classname:            Vertex2.java
*
* Version information:  1.0
*
* Date:                 10/30/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

import java.io.Serializable;

public class Vertex2 implements Serializable {
	private static final long serialVersionUID = 4768609106467658952L;
	private Integer x;
    private Integer y;

    public Vertex2(int x, int y) {
    	super();
    	this.x = x;
    	this.y = y;
    }

    public int hashCode() {
    	int hashFirst = x != null ? x.hashCode() : 0;
    	int hashSecond = y != null ? y.hashCode() : 0;

    	return (hashFirst + hashSecond) * hashSecond + hashFirst;
    }

    public boolean equals(Object other) {
    	if (other instanceof Vertex2) {
    		@SuppressWarnings("unchecked")
			Vertex2 otherPosition = (Vertex2) other;
    		return 
    		((  this.x == otherPosition.x ||
    			( this.x != null && otherPosition.x != null &&
    			  this.x.equals(otherPosition.x))) &&
    		 (	this.y == otherPosition.y ||
    			( this.y != null && otherPosition.y != null &&
    			  this.y.equals(otherPosition.y))) );
    	}

    	return false;
    }

    public String toString()
    { 
           return "(" + x + ", " + y + ")"; 
    }

    public int getX() {
    	return x;
    }

    public void setX(int x) {
    	this.x = x;
    }

    public int getY() {
    	return y;
    }

    public void setY(int y) {
    	this.y = y;
    }
    
    public void put(int x, int y) {
    	this.x = x;
    	this.y = y;
    }
}
