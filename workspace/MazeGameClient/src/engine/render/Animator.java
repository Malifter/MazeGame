package engine.render;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import engine.serializable.SerializedEntity;
import engine.serializable.SerializedObject;
import game.Game;
import game.enums.AnimationState;
import game.enums.Face;
import game.enums.GameState;

public class Animator {
    private static final String GIF = ".gif";
    private static final long DEFAULT_ANIMATION_SPEED = 175;
    private static final FilenameFilter fileFilter = new FilenameFilter() {
        public boolean accept(File dir, String name) {
            String lowercaseName = name.toLowerCase();
            if (lowercaseName.endsWith(".db")) {
                return false;
            } else {
                return true;
            }
        }
    };
    private static Map<String, AnimationInfo> animations = new HashMap<String, AnimationInfo>();
    
    private Animator() {}
    
    public static void setEntities(ArrayList<SerializedEntity> entities) {
        for(SerializedEntity entity: entities) {
            AnimationInfo animInfo = animations.get(entity.getID());
            if(animInfo == null) {
                animations.put(entity.getID(), new AnimationInfo(entity));
            } else {
                animInfo.update(entity);
            }
        }
    }
    
    public static int getSize() {
        return animations.size();
    }
    
    public static ArrayList<Entry<String, AnimationInfo>> getAnimations() {
        return new ArrayList<Entry<String, AnimationInfo>>(animations.entrySet());
    }
    
    public static String getPath(String id) {
       AnimationInfo animInfo = animations.get(id);
       if(animInfo != null && (!animInfo.delete || animInfo.completeOnce)) {
           return animInfo.filePath + animInfo.state.getPath() + animInfo.face.getPath() + animInfo.frame + GIF;
       }
       return null;
    }
    
    public static String getPath(AnimationInfo animInfo) {
        if(animInfo != null && (!animInfo.delete || animInfo.completeOnce)) {
            return animInfo.filePath + animInfo.state.getPath() + animInfo.face.getPath() + animInfo.frame + GIF;
        }
        return null;
     }
    
    public static void clear() {
        for(AnimationInfo animInfo: animations.values()) {
            animInfo.delete();
            animInfo = null;
        }
        animations.clear();
    }
    
    public static void update(long elapsedTime, List<SerializedObject> updatedObjects) {
        if(updatedObjects != null && Game.state == GameState.PLAYING) {
            List<String> uniqueIDs = new ArrayList<String>();
            for(SerializedObject so: updatedObjects) {
                uniqueIDs.add(so.getID());
            }
            for(Entry<String, AnimationInfo> animation: animations.entrySet()) {
                if(!uniqueIDs.contains(animation.getKey())) {
                    animation.getValue().delete = true;
                }
            }
        }
        
        Iterator<Entry<String, AnimationInfo>> itr = animations.entrySet().iterator();
        // TODO: NEED TO FIX THIS. This is on the server side most likely, not all
        // deletes are being sent this way. So sometimes we get things that stay 
        // even after they were supposed to disappear and delete
        // TODO: Because I removed the "updatedObjects" and replaced it with the
        // Animator, now all assets will be there until we make sure they all
        // send a delete this way.
        // TODO: To make this work correctly, we'll have to add a finalize to
        // objects on the serverside so that after they get deleted, they cannot
        // finalize until they have received an acknowledgement from all players
        // that it has been deleted
        // TODO: The other solution is once we realize that the server is no
        // longer sending us information about a specific ID, meaning when we
        // call get path on updatedObjects, we set needs delete to true.
        // This will at least cover lingering objects for now.
        //System.out.println(Arrays.deepToString(animations.entrySet().toArray()));
        while(itr.hasNext()) {
            AnimationInfo animInfo = itr.next().getValue();
            if(animInfo.delete && !animInfo.completeOnce) {
                animInfo.delete();
                itr.remove();
            } else {
                animInfo.time += elapsedTime;
                // TODO: Instead of everything being stuck at a default animation speed, this
                // can easily be changed so that each serialized object has it's own animation
                // speed. This way some things can be faster or slower than others. Whatever makes
                // sense.
                if(animInfo.time > animInfo.frameRate) {
                    animInfo.frame = ++animInfo.frame % animInfo.maxFrame;
                    animInfo.time = 0l;
                    if(animInfo.completeOnce && animInfo.frame == 0) {
                        animInfo.completeOnce = false;   
                    }
                }
            }
        }
    }
    
    public static class AnimationInfo {
        private String filePath;
        private Face face;
        private AnimationState state;
        private long frameRate;
        private long time;
        private int frame;
        private boolean delete;
        private int maxFrame;
        private boolean completeOnce = false;
        private SerializedEntity entity;
        
        public AnimationInfo(SerializedEntity entity) {
            filePath = entity.getAnimPath().getPath();
            state = entity.getAnimState();
            face = entity.getFace();
            frameRate = entity.getAnimSpeed();
            if(frameRate == -1) frameRate = DEFAULT_ANIMATION_SPEED;
            frame = 0;
            time = 0l;
            delete = entity.needsDelete();
            maxFrame = new File("assets/" + filePath + state.getPath() + face.getPath()).list(fileFilter).length;
            if(state == AnimationState.DEATH) {
                completeOnce = true;
            }
            this.entity = entity;
        }
        
        public void update(SerializedEntity entity) {
            if(state != entity.getAnimState() || face != entity.getFace()) {
                state = entity.getAnimState();
                face = entity.getFace();
                frame = 0;
                time = 0l;
                delete = entity.needsDelete();
                maxFrame = new File("assets/" + filePath + state.getPath() + face.getPath()).list(fileFilter).length;
                if(state == AnimationState.DEATH) {
                    completeOnce = true;
                }
            }
            this.entity = entity;
        }
        
        public SerializedEntity getEntity() {
            return entity;
        }
        
        public void delete() {
            filePath = null;
            state = null;
            face = null;
            frame = 0;
            time = 0l;
            delete = false;
            entity = null;
        }
    }
}
