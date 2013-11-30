package engine;

/*
* Classname:            Vertex2f.java
*
* Version information:  1.0
*
* Date:                 11/17/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

import java.io.Serializable;

public class Vertex2f implements Serializable {
	private static final long serialVersionUID = 4768609106467658952L;
	public Float x;
	public Float y;

	public Vertex2f() {
	    super();
	    this.x = 0.0f;
	    this.y = 0.0f;
	}
	
    public Vertex2f(float x, float y) {
    	super();
    	this.x = x;
    	this.y = y;
    }
    
    public Vertex2f(Vertex2f point) {
        super();
        this.x = point.x;
        this.y = point.y;
    }
    
    public Vertex2f(Vertex2 point) {
        super();
        this.x = point.x.floatValue();
        this.y = point.y.floatValue();
    }
    
    public Vertex2f add(Vertex2f op) {
        return new Vertex2f(x + op.x, y + op.y);
    }
    
    public void addEq(Vertex2f op) {
        x += op.x;
        y += op.y;
    }
    
    public Vertex2f sub(Vertex2f op) {
        return new Vertex2f(x - op.x, y - op.y);
    }
    
    public void subEq(Vertex2f op) {
        x -= op.x;
        y -= op.y;
    }
    
    public Vertex2f mult(Vertex2f op) {
        return new Vertex2f(x * op.x, y * op.y);
    }
    
    public void multEq(Vertex2f op) {
        x *= op.x;
        y *= op.y;
    }
    
    public Vertex2f div(Vertex2f op) {
        return new Vertex2f(x / op.x, y / op.y);
    }
    
    public void divEq(Vertex2f op) {
        x /= op.x;
        y /= op.y;
    }

    public int hashCode() {
    	int hashFirst = x != null ? x.hashCode() : 0;
    	int hashSecond = y != null ? y.hashCode() : 0;

    	return (hashFirst + hashSecond) * hashSecond + hashFirst;
    }

    public boolean equals(Object other) {
    	if (other instanceof Vertex2f) {
    		Vertex2f otherPosition = (Vertex2f) other;
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
    
    public void put(float x, float y) {
    	this.x = x;
    	this.y = y;
    }
}
