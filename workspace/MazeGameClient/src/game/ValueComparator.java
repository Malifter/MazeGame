package game;

import java.util.Comparator;
import java.util.Map;

import engine.render.Animator.AnimationInfo;

class ValueComparator implements Comparator<AnimationInfo> {

    Map<AnimationInfo, Float> base;
    public ValueComparator(Map<AnimationInfo, Float> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(AnimationInfo a, AnimationInfo b) {
        if (base.get(a) >= base.get(b)) {
            return 1;
        } else {
            return -1;
        } // returning 0 would merge keys
    }
}