package game.enums;

import java.util.Random;

public enum ObstacleType {
    SPIKES,
    PIT,
    ROCK,
    CELLDOOR,
    CHEST,
    ACTIVE_BOMB,
    CORPSE;

    private static final ObstacleType[] VALUES = values();
    private static final int SIZE = VALUES.length;
    private static final Random RANDOM = new Random();
    
    public static ObstacleType randomObstacle() {
        ObstacleType obstacle;
        do {
            obstacle = VALUES[RANDOM.nextInt(SIZE)];
            if(obstacle.equals(ACTIVE_BOMB) && Math.random() <= 0.01) break; // low chance of bomb room
            else if(obstacle.equals(CORPSE) && Math.random() <= 0.01) break; // low chance of corpse room
        } while(obstacle.equals(CELLDOOR) || obstacle.equals(CHEST) || obstacle.equals(ACTIVE_BOMB) || obstacle.equals(CORPSE));
        return obstacle;
    }
}
