package game.enums;

import java.util.Random;

public enum ItemType {
    BOMB(0),
    GOLD(1),
    DKEY(2),
    TOOL(3),
    SHIELD(4),
    CKEY(-1),
    LIFE(-1),
    BOOSTER(-1),
    A_BOMB(-1);

    private final int index;
    
    private static final ItemType[] VALUES = values();
    private static final int SIZE = VALUES.length;
    private static final Random RANDOM = new Random();
    
    private ItemType(int index) {
        this.index = index;
    }
    
    public int getIndex(){
        return index;
    }
    
    public static int getSize(){
        return SIZE;
    }
    
    public static ItemType randomItem() {
        ItemType item;
        do {
            item = VALUES[RANDOM.nextInt(SIZE)];
        } while(item.equals(A_BOMB));
        return item;
    }
    
    public static ItemType getByIndex(int index) {
        if(index >= 0 && index < SIZE) {
            return VALUES[index];
        }
        return null;
    }
}
