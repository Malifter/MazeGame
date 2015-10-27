package game.enums;

public enum AnimationState {
    IDLE("idle/"),
    ACTIVE("active/"),
    ATTACK("attack/"),
    RUN("run/"),
    ATTACKRUN("attackrun/"),
    CORPSE("corpse/"),
    DEATH("death/");
    
    private final String path;
    
    private AnimationState(String path) {
        this.path = path;
    }
    
    public String getPath() {
        return path;
    }
}
