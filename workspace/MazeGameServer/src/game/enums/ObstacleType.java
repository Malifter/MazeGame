package game.enums;

import java.util.Random;

public enum ObstacleType {
    SPIKES("animations/spikes/"),
    PIT("animations/pit/"),
    ROCK("animations/rock/");
    
    private final String path;
    
    private static final ObstacleType[] VALUES = values();
    private static final int SIZE = VALUES.length;
    private static final Random RANDOM = new Random();
    
    private ObstacleType(String path) {
        this.path = path;
    }
    
    public String getPath() {
        return path;
    }
    
    public static ObstacleType randomObstacle() {
        return VALUES[RANDOM.nextInt(SIZE)];
    }
}