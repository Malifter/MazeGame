package game.enums;

import engine.serializable.SerializedSound;

public enum Sound {
    HIT(0, "sounds/effects/hit.wav"),
    SHOT(1, "sounds/effects/shot.wav");
    //DEFLECT,
    //SPAWN,
    //DEAD,
    //MUSIC;
    
    private final int soundID;
    private final String filePath;
    
    private Sound(int soundID, String filePath) {
        this.soundID = soundID;
        this.filePath = filePath;
    }
    
    public int getID() {
        return soundID;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public SerializedSound serialize() {
        return new SerializedSound(soundID);
    }
}
