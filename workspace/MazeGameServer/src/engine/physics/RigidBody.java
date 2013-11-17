package engine.physics;

import engine.Vertex2;
import engine.Vertex2f;

public class RigidBody {
    private Vertex2f max;
    private Vertex2f center;
    private Vertex2f min;
    private Vertex2f delta;
    private Vertex2f velocity;
    protected float radius;
    protected float width;
    protected float height;
    
    public RigidBody() {
        
    }
}
