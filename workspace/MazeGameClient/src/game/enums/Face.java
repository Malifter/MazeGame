package game.enums;

import java.util.Random;

public enum Face {
    RIGHT("right/"),
    LEFT("left/"),
    UP("up/"),
    DOWN("down/"),
    NONE("");
    
    private final String path;
    
    private static final Face[] VALUES = values();
    private static final int SIZE = VALUES.length;
    private static final Random RANDOM = new Random();
    
    private Face(String path) {
        this.path = path;
    }
    
    public String getPath() {
        return path;
    }
    
    public static Face randomFace() {
        Face randomFace;
        do {
            randomFace = VALUES[RANDOM.nextInt(SIZE)];
        } while(randomFace.equals(NONE));
        return randomFace;
    }
    
    public static Face randomAnyFace() {
        return VALUES[RANDOM.nextInt(SIZE)];
    }
}
