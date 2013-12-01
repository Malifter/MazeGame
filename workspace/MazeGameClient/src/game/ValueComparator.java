package game;

import java.util.Comparator;
import java.util.Map;
import engine.serializable.SerializedObject;

class ValueComparator implements Comparator<SerializedObject> {

    Map<SerializedObject, Float> base;
    public ValueComparator(Map<SerializedObject, Float> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(SerializedObject a, SerializedObject b) {
        if (base.get(a) >= base.get(b)) {
            return 1;
        } else {
            return -1;
        } // returning 0 would merge keys
    }
}