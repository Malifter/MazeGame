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

public class Vector2i implements Serializable {
	private static final long serialVersionUID = 4768609106467658952L;
	public Integer x;
	public Integer y;

    public Vector2i() {
        super();
        this.x = 0;
        this.y = 0;
    }
	
    public Vector2i(int x, int y) {
    	super();
    	this.x = x;
    	this.y = y;
    }
    
    public Vector2i(Vector2i point) {
        super();
        this.x = point.x;
        this.y = point.y;
    }
    
    public Vector2i(Vector2f point) {
        super();
        this.x = point.x.intValue();
        this.y = point.y.intValue();
    }
    
    public Vector2i add(Vector2i op) {
        return new Vector2i(x + op.x, y + op.y);
    }
    
    public void addEq(Vector2i op) {
        x += op.x;
        y += op.y;
    }
    
    public Vector2i sub(Vector2i op) {
        return new Vector2i(x - op.x, y - op.y);
    }
    
    public void subEq(Vector2i op) {
        x -= op.x;
        y -= op.y;
    }
    
    public Vector2i mult(int op) {
        return new Vector2i(x *= op, y *= op);
    }
    
    public void multEq(int op) {
        x *= op;
        y *= op;
    }
    
    public Vector2i div(int op) {
        return new Vector2i(x /= op, y /= op);
    }
    
    public void divEq(int op) {
        x /= op;
        y /= op;
    }
    
    public float dot(Vector2i op) {
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
    	if (other instanceof Vector2i) {
    		Vector2i otherPosition = (Vector2i) other;
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
    
    public void put(int x, int y) {
    	this.x = x;
    	this.y = y;
    }
    
    public Vector2i reverse() {
        return new Vector2i(-x, -y);
    }
    
    public Vector2f norm() {
        float length = length();
        return new Vector2f(x/length, y/length);
    }
    
    public float length() {
        return (float) Math.sqrt((x*x) + (y*y));
    }
}
