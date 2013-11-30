package game.enums;

public enum Side {
    TOP("top", 0),
    LEFT("left", 1),
    RIGHT("right", 2),
    BOTTOM("bottom", 3);
    
    private static final Side[] VALUES = values();
    private static final int SIZE = VALUES.length;
    
    private final String value;
    private final int index;
    
    private Side(String value, int index) {
        this.value = value;
        this.index = index;
    }
    
    public static Side findByValue(String value) {
        for(int i = 0; i < SIZE; i++) {
            if(VALUES[i].getValue().equalsIgnoreCase(value)) return VALUES[i];
        }
        return null;
    }
    
    public Side opposite() {
        if(this.equals(TOP)) return BOTTOM;
        else if(this.equals(BOTTOM)) return TOP;
        else if(this.equals(RIGHT)) return LEFT;
        else if(this.equals(LEFT)) return RIGHT;
        return null;
    }
    
    public int getIndex() {
        return index;
    }
    
    public String getValue() {
        return value;
    }
}
