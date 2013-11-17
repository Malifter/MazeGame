package game;

public class Shield extends Item {
    
    
    int strength;
    
    public Shield(Game g, String anImage, int x, int y, float w, float h,
            int itemId, String name) {
        super(g, anImage, x, y, w, h, itemId, name);
        // TODO Auto-generated constructor stub
    }
    
    public void getAttacked(int damage){

        strength = strength - damage;
        if (strength <= 0) 
            isDestroyed = true;

        }
    
    
}
