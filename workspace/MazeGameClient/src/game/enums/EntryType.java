package game.enums;

public enum EntryType {
    NONE(0.6f), DOOR(0.3f), PORTAL(0.1f);
    
    private final float probability;
    
    private EntryType(float probability) {
        this.probability = probability;
    }

    public float getProbability() {
        return probability;
    }
}
