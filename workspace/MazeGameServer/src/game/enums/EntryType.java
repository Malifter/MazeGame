package game.enums;

public enum EntryType {
    NONE(0.6f, ""), DOOR(0.3f, "animations/door/"), PORTAL(0.1f, "animations/door/");
    
    private final float probability;
    private final String path;
    
    private EntryType(float probability, String path) {
        this.probability = probability;
        this.path = path;
    }
    
    public String getPath() {
        return path;
    }
    
    public float getProbability() {
        return probability;
    }
}
