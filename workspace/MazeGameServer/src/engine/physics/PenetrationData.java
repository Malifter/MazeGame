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
public class PenetrationData<K, X, Y> {

    private K info;
    private X penx;
    private Y peny;

    public static <K, X, Y> PenetrationData<K, X, Y> createPair(K info, X penx, Y peny) {
        return new PenetrationData<K, X, Y>(info, penx, peny);
    }

    public PenetrationData(K info, X penx, Y peny) {
        this.info = info;
        this.penx = penx;
        this.peny = peny;
    }

    public K getInfo() {
        return info;
    }

    public X getPenX() {
        return penx;
    }
    
    public Y getPenY() {
        return peny;
    }
    
    public void put(K info, X penx, Y peny) {
        this.info = info;
        this.penx = penx;
        this.peny = peny;
    }

    public void setInfo(K info) {
        this.info = info;
    }
    
    public void setPenX(X penx) {
        this.penx = penx;
    }
    
    public void setPenY(Y peny) {
        this.peny = peny;
    }
}
