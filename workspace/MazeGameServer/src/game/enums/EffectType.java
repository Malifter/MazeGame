package game.enums;

public enum EffectType {
    EXPLOSION,
    PROJECTILE_1,
    PROJECTILE_2,
    DEATH;
    
    public AnimationPath getAnimPath() {
        switch(this) {
            case EXPLOSION:
                return AnimationPath.EXPLOSION;
            case PROJECTILE_1:
                return AnimationPath.PROJECTILE_1_HIT;
            case PROJECTILE_2:
                return AnimationPath.PROJECTILE_2_HIT;
            case DEATH:
                return AnimationPath.DEATH_EFFECT;
            default: return null;
        }
    }
}
