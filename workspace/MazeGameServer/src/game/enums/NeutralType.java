package game.enums;

public enum NeutralType {
    HOSTAGE("animations/hostage/"),
    GATEKEEPER("animations/alien/");
    
    private final String path;
    
    private NeutralType(String path) {
        this.path = path;
    }
    
    public String getPath() {
        return path;
    }
}
