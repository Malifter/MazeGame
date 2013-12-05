package engine.render;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import engine.serializable.SerializedEntity;
import game.enums.AnimationState;
import game.enums.Face;

public class Animator {
    private static final String GIF = ".gif";
    private static final long ANIMATION_TIME = 250;
    private static Map<String, Integer> idToIndex = new HashMap<String, Integer>();
    private static ArrayList<String> animID = new ArrayList<String>();
    private static ArrayList<String> animPath = new ArrayList<String>();
    private static ArrayList<Face> animFace = new ArrayList<Face>();
    private static ArrayList<AnimationState> animState = new ArrayList<AnimationState>();
    private static ArrayList<Long> animTime = new ArrayList<Long>();
    private static ArrayList<Integer> animNum = new ArrayList<Integer>();
    private static ArrayList<Boolean> delete = new ArrayList<Boolean>();
    private static ArrayList<Integer> animSize = new ArrayList<Integer>();
    
    private Animator() {}
    
    public static void setEntities(ArrayList<SerializedEntity> entities) {
        for(SerializedEntity entity: entities) {
            Integer index = idToIndex.get(entity.getID());
            if(index == null) {
                index = idToIndex.size();
                idToIndex.put(entity.getID(), index);
                animID.add(entity.getID());
                animPath.add(entity.getAnimPath().getPath());
                animState.add(entity.getAnimState());
                animFace.add(entity.getFace());
                animNum.add(0);
                animTime.add(0l);
                delete.add(false);
                File file = new File("assets/" + animPath.get(index) + entity.getAnimState().getPath() + "total.txt");
                if(file.exists()) {
                    int size = 0;
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                        size = Integer.parseInt(bufferedReader.readLine());
                        bufferedReader.close();
                    } catch(Exception ignore) {};
                    animSize.add(size);
                } else {
                    animSize.add(1);
                }
            } else {
                if(!animState.get(index).equals(entity.getAnimState()) || !animFace.get(index).equals(entity.getFace())) {
                    animState.set(index, entity.getAnimState());
                    animFace.set(index, entity.getFace());
                    animNum.set(index, 0);
                    animTime.set(index, 0l);
                    File file = new File("assets/" + animPath.get(index) + entity.getAnimState().getPath() + "total.txt");
                    if(file.exists()) {
                        int size = 0;
                        try {
                            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                            size = Integer.parseInt(bufferedReader.readLine());
                            bufferedReader.close();
                        } catch(Exception ignore) {};
                        animSize.add(size);
                    } else {
                        animSize.add(1);
                    }
                }
            }
            if(entity.needsDelete()) {
                delete.set(index, true);
            }
        }
    }
    
    public static String getPath(String id) {
       Integer i = idToIndex.get(id);
       if(i != null) {
           return animPath.get(i) + animState.get(i).getPath() + animFace.get(i).getPath() + animNum.get(i) + GIF;
       }
       return null;
    }
    
    public static void clear() {
        idToIndex.clear();
        animID.clear();
        animPath.clear();
        animState.clear();
        animFace.clear();
        animTime.clear();
        animNum.clear();
        animSize.clear();
        delete.clear();
        
    }
    
    public static void update(long elapsedTime) {
        for(int i = 0; i < idToIndex.size(); i++) {
            animTime.set(i, animTime.get(i) + elapsedTime);
            if(animTime.get(i) > ANIMATION_TIME) {
                animNum.set(i, (animNum.get(i)+1) % animSize.get(i));
                animTime.set(i, 0l);
            }
        }
    }
}
