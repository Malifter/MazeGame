package game.enums;

import java.util.Random;

public enum ObstacleType {
    SPIKES,
    PIT,
    ROCK,
    CELLDOOR,
    CHEST;

    private static final ObstacleType[] VALUES = values();
    private static final int SIZE = VALUES.length;
    private static final Random RANDOM = new Random();
    
    public static ObstacleType randomObstacle() {
        ObstacleType obstacle;
        do {
            obstacle = VALUES[RANDOM.nextInt(SIZE)];
        } while(obstacle.equals(CELLDOOR) || obstacle.equals(CHEST));
        return obstacle;
    }
}
