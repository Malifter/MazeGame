package game.enums;

import java.util.Random;

public enum EnemyType {    
    FLYBAG,
    CHASER,
    SPIDER,
    SPIDER_BOSS;
    
    private static final EnemyType[] VALUES = values();
    private static final int SIZE = VALUES.length;
    private static final Random RANDOM = new Random();
    
    public static EnemyType randomWeakEnemy() {
        EnemyType weakEnemy;
        do {
            weakEnemy = VALUES[RANDOM.nextInt(SIZE)];
        } while(weakEnemy.equals(SPIDER_BOSS));
        return weakEnemy;
    }
}
