package game.enums;

import java.util.Random;

public enum Face {
    RIGHT,
    LEFT,
    UP,
    DOWN;
    
    private static final Face[] VALUES = values();
    private static final int SIZE = VALUES.length;
    private static final Random RANDOM = new Random();
    
    public static Face randomFace() {
        return VALUES[RANDOM.nextInt(SIZE)];
    }
}
