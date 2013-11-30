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
	public Integer x;
	public Integer y;

    public Vertex2() {
        super();
        this.x = 0;
        this.y = 0;
    }
	
    public Vertex2(int x, int y) {
    	super();
    	this.x = x;
    	this.y = y;
    }
    
    public Vertex2(Vertex2 point) {
        super();
        this.x = point.x;
        this.y = point.y;
    }
    
    public Vertex2(Vertex2f point) {
        super();
        this.x = point.x.intValue();
        this.y = point.y.intValue();
    }
    
    public Vertex2 add(Vertex2 op) {
        return new Vertex2(x + op.x, y + op.y);
    }
    
    public void addEq(Vertex2 op) {
        x += op.x;
        y += op.y;
    }
    
    public Vertex2 sub(Vertex2 op) {
        return new Vertex2(x - op.x, y - op.y);
    }
    
    public void subEq(Vertex2 op) {
        x -= op.x;
        y -= op.y;
    }
    
    public Vertex2 mult(Vertex2 op) {
        return new Vertex2(x * op.x, y * op.y);
    }
    
    public void multEq(Vertex2 op) {
        x *= op.x;
        y *= op.y;
    }
    
    public Vertex2 div(Vertex2 op) {
        return new Vertex2(x / op.x, y / op.y);
    }
    
    public void divEq(Vertex2 op) {
        x /= op.x;
        y /= op.y;
    }

    public int hashCode() {
    	int hashFirst = x != null ? x.hashCode() : 0;
    	int hashSecond = y != null ? y.hashCode() : 0;

    	return (hashFirst + hashSecond) * hashSecond + hashFirst;
    }

    public boolean equals(Object other) {
    	if (other instanceof Vertex2) {
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
    
    public void put(int x, int y) {
    	this.x = x;
    	this.y = y;
    }
}
