package engine.physics;

/*
* Classname:            PenetrationData.java
*
* Version information:  1.0
*
* Date:                 10/30/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

/**
 * PenetrationData<k, x, y>
 */
public class PenetrationData<K> {

    private K info;
    private Float penx;
    private Float peny;

    public static <K, X, Y> PenetrationData<K> createPair(K info, float penx, float peny) {
        return new PenetrationData<K>(info, penx, peny);
    }

    public PenetrationData(K info, float penx, float peny) {
        this.info = info;
        this.penx = penx;
        this.peny = peny;
    }

    public K getInfo() {
        return info;
    }

    public float getPenX() {
        return penx;
    }
    
    public float getPenY() {
        return peny;
    }
    
    public void put(K info, float penx, float peny) {
        this.info = info;
        this.penx = penx;
        this.peny = peny;
    }

    public void setInfo(K info) {
        this.info = info;
    }
    
    public void setPenX(float penx) {
        this.penx = penx;
    }
    
    public void setPenY(float peny) {
        this.peny = peny;
    }
}
