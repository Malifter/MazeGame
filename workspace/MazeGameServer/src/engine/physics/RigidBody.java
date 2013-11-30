package engine.physics;

import engine.Vertex2;
import engine.Vertex2f;

public class RigidBody {
    private static final float MOVE_FACTOR = 10.0f;
    private Vertex2f max = new Vertex2f();
    private Vertex2f mid = new Vertex2f();
    private Vertex2f min = new Vertex2f();
    private Vertex2f location;
    private Vertex2f delta = new Vertex2f();
    private Vertex2f velocity = new Vertex2f();
    private Vertex2f offset = new Vertex2f(); // Offset the bounding box from center
    private float radius = 0;
    private float width; // has nothing to do with image width
    private float height; // has nothing to do with image height
    private boolean enabled = false;
    
    public RigidBody(Vertex2f location, float width, float height) {
        this.location = location;
        this.width = width;
        this.height = height;
        calculateBounds();
        float xd = this.mid.x - max.x;
        float yd = this.mid.y - max.y;
        radius = (float) Math.sqrt(xd*xd + yd*yd);
        enable();
    }
    
    public RigidBody(Vertex2 location, float width, float height) {
        this.location = new Vertex2f(location);
        this.width = width;
        this.height = height;
        calculateBounds();
        float xd = this.mid.x - max.x;
        float yd = this.mid.y - max.y;
        radius = (float) Math.sqrt(xd*xd + yd*yd);
        enable();
    }
    
    private void calculateBounds() {
        mid.x = location.x + offset.x;
        mid.y = location.y + offset.y;
        min.x = mid.x - (width/2.0f);
        min.y = mid.y - (height/2.0f);
        max.x = min.x + width;
        max.y = min.y + height;
    }
    
    public void move(float dx, float dy, long elapsedTime) {
        float step = elapsedTime / MOVE_FACTOR;
        delta.x = dx * step;
        delta.y = dy * step;
        location.x += delta.x;
        location.y += delta.y;
        calculateBounds();
    }
    
    /**
     * move: Based on velocity.
     * @param elapsedTime
     */
    public void move(long elapsedTime) {
        float step = elapsedTime / MOVE_FACTOR;
        delta.x = velocity.x * step;
        delta.y = velocity.y * step;
        location.x += delta.x;
        location.y += delta.y;
        calculateBounds();
    }
    
    /**
     * move: Moves immediately by a specific x, y.
     * setLocation: <add description>
     * @param location
     */
    public void move(float dx, float dy) {
        location.x += dx;
        location.y += dy;
        calculateBounds();
    }
    
    public void setLocation(Vertex2f location) {
        this.location.x = location.x;
        this.location.y = location.y;
        calculateBounds();
    }
    
    public void setLocation(Vertex2 location) {
        mid.x = (float) location.x;
        mid.y = (float) location.y;
        this.location.x = mid.x - offset.x;
        this.location.y = mid.y - offset.y;
        calculateBounds();
    }
    
    public void setLocation(float x, float y) {
        mid.put(x, y);
        this.location.x = mid.x - offset.x;
        this.location.y = mid.y - offset.y;
        calculateBounds();
    }
    
    public Vertex2f getLocation() {
        return location;
    }
    
    public Vertex2f getMax() {
        return max;
    }
    
    public Vertex2f getMid() {
        return mid;
    }
    
    public Vertex2f getMin() {
        return min;
    }
    
    public Vertex2f getDelta() {
        return delta;
    }
    
    public void setVelocity(Vertex2f velocity) {
        this.velocity = velocity;
    }
    
    public void setVelocity(float x, float y) {
        velocity.put(x, y);
    }
    
    public Vertex2f getVelocity() {
        return velocity;
    }
    
    public void changeVelocity(Vertex2f dV) {
        velocity.x += dV.x;
        velocity.y += dV.y;
    }
    
    public void changeVelocity(float x, float y) {
        velocity.x += x;
        velocity.y += y;
    }
    
    public void setOffset(Vertex2f offset) {
        this.offset = offset;
        calculateBounds();
    }
    
    public void setOffset(float x, float y) {
        offset.put(x, y);
        calculateBounds();
    }
    
    public Vertex2f getOffset() {
        return offset;
    }
    
    public float getWidth() {
        return width;
    }
    
    public float getHeight() {
        return height;
    }
    
    public float getRadius() {
        return radius;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void disable() {
        enabled = false;
    }
    
    public void enable() {
        enabled = true;
    }
    
    /**
     * useLowerBoundingBox: Use this if the width and height are the same as the image.
     * It will use the lower % as bounding box. This is so we can put one around feet instead
     * of the whole image.
     * @param percent
     */
    public static void useLowerBoundingBox(RigidBody rb, float percent) {
        rb.height = rb.height * percent;
        rb.offset.y += (rb.max.y - (rb.height/2.0f)) - rb.mid.y;
        rb.calculateBounds();
        float xd = rb.mid.x - rb.max.x;
        float yd = rb.mid.y - rb.max.y;
        rb.radius = (float) Math.sqrt(xd*xd + yd*yd);
    }
}
