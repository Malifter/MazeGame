package engine.physics;

import engine.Position;

public class RigidBody {
    private Position<Float, Float> max;
    private Position<Float, Float> location;
    private Position<Float, Float> min;
    protected float radius;
    protected float width;
    protected float height;
    protected float velY;
    protected float velX;
    protected float deltaX;
    protected float deltaY;
    
    public RigidBody() {
        
    }
}
