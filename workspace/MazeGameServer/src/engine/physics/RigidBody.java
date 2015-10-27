package engine.physics;

import engine.Vector2i;
import engine.Vector2f;

public class RigidBody {
    private static final float MOVE_FACTOR = 10.0f;
    private Vector2f max = new Vector2f();
    private Vector2f mid = new Vector2f();
    private Vector2f min = new Vector2f();
    private Vector2f location;
    private Vector2f delta = new Vector2f();
    private Vector2f velocity = new Vector2f();
    private Vector2f offset = new Vector2f(); // Offset the bounding box from center
    private float radius = 0;
    private float width; // has nothing to do with image width
    private float height; // has nothing to do with image height
    private boolean enabled = false;
    
    public RigidBody(Vector2f location, float width, float height) {
        this.location = location;
        this.width = width;
        this.height = height;
        calculateBounds();
        radius = Collisions.findDistance(mid, max);
        enable();
    }
    
    public RigidBody(Vector2i location, float width, float height) {
        this.location = new Vector2f(location);
        this.width = width;
        this.height = height;
        calculateBounds();
        radius = Collisions.findDistance(mid, max);
        enable();
    }
    
    public RigidBody(RigidBody rb, float width, float height) {
        this.location = new Vector2f(rb.getLocation());
        this.width = width;
        this.height = height;
        calculateBounds();
        radius = Collisions.findDistance(mid, max);
        enable();
    }
    
    public RigidBody(RigidBody rb) {
        this.location = new Vector2f(rb.getLocation());
        this.width = rb.getWidth();
        this.height = rb.getHeight();
        calculateBounds();
        radius = Collisions.findDistance(mid, max);
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
    
    /**
     * move: Set's velocity, then moves based on velocity.
     * @param velX
     * @param velY
     * @param elapsedTime
     */
    public void move(float velX, float velY, long elapsedTime) {
        velocity.x = velX;
        velocity.y = velY;
        move(elapsedTime);
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
    public void move(float newX, float newY) {
        location.x += newX;
        location.y += newY;
        calculateBounds();
    }
    
    public void setLocation(Vector2f location) {
        mid.x = location.x;
        mid.y = location.y;
        this.location.x = mid.x - offset.x;
        this.location.y = mid.y - offset.y;
        calculateBounds();
    }
    
    public void setLocation(Vector2i location) {
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
    
    public Vector2f getLocation() {
        return location;
    }
    
    public Vector2f getMax() {
        return max;
    }
    
    public Vector2f getMid() {
        return mid;
    }
    
    public Vector2f getMin() {
        return min;
    }
    
    public Vector2f getDelta() {
        return delta;
    }
    
    public void setVelocity(Vector2f velocity) {
        this.velocity = velocity;
    }
    
    public void setVelocity(float x, float y) {
        velocity.put(x, y);
    }
    
    public Vector2f getVelocity() {
        return velocity;
    }
    
    public void changeVelocity(Vector2f dV) {
        velocity.x += dV.x;
        velocity.y += dV.y;
    }
    
    public void changeVelocity(float x, float y) {
        velocity.x += x;
        velocity.y += y;
    }
    
    public void setOffset(Vector2f offset) {
        this.offset = offset;
        calculateBounds();
    }
    
    public void setOffset(float x, float y) {
        offset.put(x, y);
        calculateBounds();
    }
    
    public Vector2f getOffset() {
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
    
    public void setRadius(float radius) {
        this.radius = radius;
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
        rb.radius = Collisions.findDistance(rb.mid, rb.max);
    }
}
