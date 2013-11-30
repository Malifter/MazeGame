package game.enums;

import java.util.Random;

public enum EnemyType {    
    WOODMAN("animations/woodman/"),
    SHIELD("animations/shieldguy/"),
    CANNON("animations/cannon/");
    
    private final String path;
    
    private static final EnemyType[] VALUES = values();
    private static final int SIZE = VALUES.length;
    private static final Random RANDOM = new Random();
    
    private EnemyType(String path) {
        this.path = path;
    }
    
    public String getPath() {
        return path;
    }
    
    public static EnemyType randomEnemy() {
        return VALUES[RANDOM.nextInt(SIZE)];
    }
}
