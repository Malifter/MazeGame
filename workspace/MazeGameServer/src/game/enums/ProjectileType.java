package game.enums;

import java.util.Random;

public enum ProjectileType {
    STRAIGHT,
    DIAGONAL,
    ARC,
    HOMING;
    
    private static final ProjectileType[] VALUES = values();
    private static final int SIZE = VALUES.length;
    private static final Random RANDOM = new Random();
    
    public static ProjectileType randomProjectile() {
        return VALUES[RANDOM.nextInt(SIZE)];
    }
}
