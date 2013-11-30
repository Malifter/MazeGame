package game.enums;

public enum Pressed {
    RIGHT(0),
    LEFT(1),
    UP(2),
    DOWN(3),
    FIRE(4),
    ESCAPE(5),
    PAUSE(6),
    START_GAME(7), 
    SELECT_FORWARD(8),
    SELECT_BACKWARD(9);
    
    public static final int SIZE = values().length;
    
    private final int value;
    
    private Pressed(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
}
