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
    ACTIVE_BOMB(-1);

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
        ItemType item = null;
        do {
            // TODO: Check probabilities here, a cell key can only be used on a cell
            // therefore we probably only want one to spawn per person and only have
            // a chance of spawning if a person who doesn't already have a key is in the room
            // maybe you can't even pick up a second one if you already have one. This will
            // help reduce the number of gold keys which are spawned. The silver keys are used
            // for the doors and chests. So basically they should spawn more abudnantly since
            // they will be used a lot more often.
            item = VALUES[RANDOM.nextInt(SIZE)];
        } while(item == ACTIVE_BOMB);
        return item;
    }
    
    public static ItemType getByIndex(int index) {
        if(index >= 0 && index < SIZE) {
            return VALUES[index];
        }
        return null;
    }
}
