package game.enums;

import java.util.Random;

public enum ItemType {
    BOMB("items/bomb/"),
    GOLD("items/gold/"),
    DKEY("items/dkey/"),
    CKEY("items/ckey/"),
    TOOL("items/tool/"),
    LIFE("items/elife/"),
    BOOSTER("items/booster/"),
    SHIELD("items/shield/");
    
    private final String path;
    
    private static final ItemType[] VALUES = values();
    private static final int SIZE = VALUES.length;
    private static final Random RANDOM = new Random();
    
    private ItemType(String path) {
        this.path = path;
    }
    
    public String getPath() {
        return path;
    }
    
    public static ItemType randomItem() {
        return VALUES[RANDOM.nextInt(SIZE)];
    }
}
