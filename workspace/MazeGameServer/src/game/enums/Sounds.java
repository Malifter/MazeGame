package game.enums;

public enum Sounds {
    HIT(0),
    SHOT(1),
    DEFLECT(2),
    SPAWN(3),
    DEAD(4),
    MUSIC(5);
    
    private final int value;
    
    private Sounds(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
}
