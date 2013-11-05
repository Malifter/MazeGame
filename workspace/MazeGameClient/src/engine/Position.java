package engine;
import java.io.Serializable;

public class Position<X, Y> implements Serializable {
	private static final long serialVersionUID = 4768609106467658952L;
	private X x;
    private Y y;

    public Position(X x, Y y) {
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
    	if (other instanceof Position) {
    		@SuppressWarnings("unchecked")
			Position<X, Y> otherPosition = (Position<X, Y>) other;
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

    public X getX() {
    	return x;
    }

    public void setX(X x) {
    	this.x = x;
    }

    public Y getY() {
    	return y;
    }

    public void setY(Y y) {
    	this.y = y;
    }
    
    public void put(X x, Y y) {
    	this.x = x;
    	this.y = y;
    }
}
