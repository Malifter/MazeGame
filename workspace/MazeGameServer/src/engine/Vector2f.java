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

public class Vector2f implements Serializable {
	private static final long serialVersionUID = 4768609106467658952L;
	public Float x;
	public Float y;

	public Vector2f() {
	    super();
	    this.x = 0.0f;
	    this.y = 0.0f;
	}
	
    public Vector2f(float x, float y) {
    	super();
    	this.x = x;
    	this.y = y;
    }
    
    public Vector2f(Vector2f point) {
        super();
        this.x = point.x;
        this.y = point.y;
    }
    
    public Vector2f(Vector2i point) {
        super();
        this.x = point.x.floatValue();
        this.y = point.y.floatValue();
    }
    
    public Vector2f add(Vector2f op) {
        return new Vector2f(x + op.x, y + op.y);
    }
    
    public void addEq(Vector2f op) {
        x += op.x;
        y += op.y;
    }
    
    public Vector2f sub(Vector2f op) {
        return new Vector2f(x - op.x, y - op.y);
    }
    
    public void subEq(Vector2f op) {
        x -= op.x;
        y -= op.y;
    }
    
    public Vector2f mult(float op) {
        return new Vector2f(x *= op, y *= op);
    }
    
    public void multEq(float op) {
        x *= op;
        y *= op;
    }
    
    public Vector2f div(float op) {
        return new Vector2f(x /= op, y /= op);
    }
    
    public void divEq(float op) {
        x /= op;
        y /= op;
    }
    
    public float dot(Vector2f op) {
        Vector2f U = this.norm();
        Vector2f V = op.norm();
        return U.x*V.x + U.y*V.y;
    }

    public int hashCode() {
    	int hashFirst = x != null ? x.hashCode() : 0;
    	int hashSecond = y != null ? y.hashCode() : 0;

    	return (hashFirst + hashSecond) * hashSecond + hashFirst;
    }

    public boolean equals(Object other) {
    	if (other instanceof Vector2f) {
    		Vector2f otherPosition = (Vector2f) other;
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
    
    public Vector2f reverse() {
        return new Vector2f(-x, -y);
    }
    
    public Vector2f norm() {
        float length = length();
        return new Vector2f(x/length, y/length);
    }
    
    public float length() {
        return (float) Math.sqrt((x*x) + (y*y));
    }
}
