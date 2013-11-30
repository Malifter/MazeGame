package game.enums;

import java.util.Random;

public enum ProjectileType {
    STRAIGHT("animations/projectiles/"),
    DIAGONAL("animations/projectiles/"),
    ARC("animations/projectiles/"),
    HOMING("animations/projectiles/");
    
    private final String path;
    
    private static final ProjectileType[] VALUES = values();
    private static final int SIZE = VALUES.length;
    private static final Random RANDOM = new Random();
    
    private ProjectileType(String path) {
        this.path = path;
    }
    
    public String getPath() {
        return path;
    }
    
    public static ProjectileType randomProjectile() {
        return VALUES[RANDOM.nextInt(SIZE)];
    }
}
