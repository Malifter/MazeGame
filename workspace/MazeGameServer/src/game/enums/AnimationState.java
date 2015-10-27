package game.enums;

public enum AnimationState {
    IDLE("idle/"),
    ACTIVE("active/"),
    ATTACK("attack/"),
    RUN("run/"),
    ATTACKRUN("attackrun/"),
    CORPSE("corpse/"),
    DEATH("death/");
    
    // TODO: Add a boolean that allows for this to know whether it is to play once or to loop
    private final String path;
    
    private AnimationState(String path) {
        this.path = path;
    }
    
    public String getPath() {
        return path;
    }
}
