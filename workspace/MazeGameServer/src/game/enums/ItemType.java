package game.enums;

import java.util.Random;

public enum ItemType {
    BOMB("items/bomb/",0),
    GOLD("items/gold/",1),
    DKEY("items/dkey/",2),
    CKEY("items/ckey/",-1),
    TOOL("items/tool/",4),
    SHIELD("items/shield/",5),
    LIFE("items/elife/",-1),
    BOOSTER("items/booster/",-1),
    A_BOMB("items/bomb/",-1);

    private final String path;
    private final int index;
    
    private static final ItemType[] VALUES = values();
    private static final int SIZE = VALUES.length;
    private static final Random RANDOM = new Random();
    
    private ItemType(String path, int index) {
        this.path = path;
        this.index = index;
    }
    
    public String getPath() {
        return path;
    }
    
    public int getIndex(){
        return index;
    }
    
    public static int getSize(){
        return SIZE;
    }
    
    public static ItemType randomItem() {
        ItemType item;
        do{
            item = VALUES[RANDOM.nextInt(SIZE)];
        }while(item.equals(A_BOMB));
        return item;
    }
    
    public static ItemType getByIndex(int index){
        if(index>=0 && index<SIZE){
            return VALUES[index];
        }
        return null;
    }
}
