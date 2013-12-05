package game.enums;

public enum AnimationPath {
    DOOR("animations/entries/door/"),
    PORTAL("animations/entries/portal/"),
    BOMB("animations/items/bomb/"),
    BOOSTER("animations/items/booster/"),
    CKEY("animations/items/ckey/"),
    DKEY("animations/items/dkey/"),
    ELIFE("animations/items/elife/"),
    GOLD("animations/items/gold/"),
    SHIELD("animations/items/shield/"),
    TOOL("animations/items/tool/"),
    SPIDER_BOSS("animations/npcs/spider_boss/"),
    CHASER("animations/npcs/chaser/"),
    FLYBAG("animations/npcs/flyingbag/"),
    GATEKEEPER("animations/npcs/gatekeeper/"),
    HOSTAGE("animations/npcs/hostage/"),
    SPIDER("animations/npcs/spider/"),
    PLAYER_1("animations/npcs/player/"),
    PLAYER_2("animations/npcs/player/"),
    PLAYER_3("animations/npcs/player/"),
    PLAYER_4("animations/npcs/player/"),
    CELLDOOR("animations/obstacles/celldoor/"),
    CHEST("animations/obstacles/chest/"),
    PIT("animations/obstacles/pit/"),
    ROCK("animations/obstacles/rock/"),
    SPIKES("animations/obstacles/spikes/"),
    PROJECTILE_1("animations/projectiles/projectile_1/"),
    PROJECTILE_2("animations/projectiles/projectile_2/"),
    NONE("");
    
    private final String path;
    
    private AnimationPath(String path) {
        this.path = path;
    }
    
    public String getPath() {
        return path;
    }
}
