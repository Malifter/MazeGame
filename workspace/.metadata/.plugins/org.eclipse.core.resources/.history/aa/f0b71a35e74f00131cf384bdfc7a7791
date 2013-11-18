package engine.physics;

/*
* Classname:            Collisions.java
*
* Version information:  1.0
*
* Date:                 10/30/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

import java.util.ArrayList;

import game.Entity;

public class Collisions {
    private static enum Position { NONE, RIGHT, LEFT, ABOVE, BELOW, AboveRIGHT, AboveLEFT, BelowRIGHT, BelowLEFT};

    /**
     * applyEnvironmentCollision: A simple class that detects collision between an entity and the environment. 
     * @param dynamicObj
     * @param staticObj
     * @return
     */
    public static void applyEnvironmentCollision(Entity entity, ArrayList<Entity> environment) {
        ArrayList<PenetrationData<Position, Float, Float>> penetrationAdjust = new ArrayList<PenetrationData<Position, Float, Float>>();
        for(Entity env: environment) {
            if(detectCollisionSphere(entity, env) ? detectCollisionBox(entity, env) : false) {
                PenetrationData<Position, Float, Float> penetrationData = calculatePenetration(entity, env);
                if(penetrationData != null) penetrationAdjust.add(penetrationData);
            }
        }
        if(penetrationAdjust.size() > 0) applyPenetrationCorrections(entity, penetrationAdjust);
    }
    
    /**
     * detectCollision: <add description>
     * @param ent1
     * @param ent2
     * @return
     */
    public static boolean detectCollision(Entity obj1, Entity obj2) {
        return detectCollisionSphere(obj1, obj2) ? detectCollisionBox(obj1, obj2) : false;
    }
    
    public static boolean detectCollisionSphere(Entity obj1, Entity obj2) {
        float relX = obj1.getMidX() - obj2.getMidX();
        float relY = obj1.getMidY() - obj2.getMidY();
        float dist = relX*relX + relY*relY;
        float minDist = obj1.getRadius() + obj2.getRadius();
        return dist <= minDist*minDist;
    }
    
    public static boolean detectCollisionBox(Entity obj1, Entity obj2) {  
        if(obj1.getMaxX() < obj2.getMinX())
            return false;
        if(obj1.getMinX() >  obj2.getMaxX())
            return false;
        if(obj1.getMaxY() <  obj2.getMinY())
            return false;
        if(obj1.getMinY() >  obj2.getMaxY())
            return false;
        return true;
    }
    
    public static PenetrationData<Position, Float, Float> calculatePenetration(Entity entity, Entity environment) {
        //calculate x and y penetration levels
        float penx = 0;
        float peny = 0;
        Position pos = Position.NONE;
        
        // tile below - neg
        if(entity.getMaxY() < environment.getMaxY() && entity.getMinY() < environment.getMinY()) {
            peny = -(entity.getMaxY() - environment.getMinY());
            if(peny == 0) {
                peny = -0.0000001f;
            }
            pos = Position.BELOW;
            //System.out.printf("below ");
        }
        // tile above - pos
        else if(environment.getMaxY() < entity.getMaxY() && environment.getMinY() < entity.getMinY()) {
            peny = (environment.getMaxY() - entity.getMinY());
            if(peny == 0) {
                peny = 0.0000001f;
            }
            pos = Position.ABOVE;
            //System.out.printf("above ");
        }
        // tile right - neg
        if(entity.getMaxX() < environment.getMaxX() && entity.getMinX() < environment.getMinX()) {
            penx = -(entity.getMaxX() - environment.getMinX());
            if(penx == 0) {
                penx = -0.0000001f;
            }
            pos = pos.compareTo(Position.NONE) == 0 ? Position.RIGHT : (pos.compareTo(Position.BELOW) == 0 ? Position.BelowRIGHT : Position.AboveRIGHT);
            //System.out.printf("right ");
        }
        // tile left - pos
        else if(environment.getMaxX() < entity.getMaxX() && environment.getMinX() < entity.getMinX()) {
            penx = (environment.getMaxX() - entity.getMinX());
            if(penx == 0) {
                penx = 0.0000001f;
            }
            pos = pos.compareTo(Position.NONE) == 0 ? Position.LEFT : (pos.compareTo(Position.BELOW) == 0 ? Position.BelowLEFT : Position.AboveLEFT);
            //System.out.printf("left ");
        }
        //System.out.printf("\n");
        
        if(Math.abs(penx) > Math.abs(peny) && peny != 0) {
            penx = 0;
        }
        else if(Math.abs(penx) < Math.abs(peny) && penx != 0) {
            peny = 0;
        }

        return (penx != 0 || peny != 0) ? new PenetrationData<Position, Float, Float>(pos, penx, peny) : null;
    }
    
    public static void applyPenetrationCorrections(Entity entity, ArrayList<PenetrationData<Position, Float, Float>> penetrationAdjust) {
        PenetrationData<Boolean, Float, Float> R = new PenetrationData<Boolean, Float, Float>(false, 0.0f, 0.0f);
        PenetrationData<Boolean, Float, Float> L = new PenetrationData<Boolean, Float, Float>(false, 0.0f, 0.0f);
        PenetrationData<Boolean, Float, Float> A = new PenetrationData<Boolean, Float, Float>(false, 0.0f, 0.0f);
        PenetrationData<Boolean, Float, Float> B = new PenetrationData<Boolean, Float, Float>(false, 0.0f, 0.0f);
        PenetrationData<Boolean, Float, Float> AR = new PenetrationData<Boolean, Float, Float>(false, 0.0f, 0.0f);
        PenetrationData<Boolean, Float, Float> AL = new PenetrationData<Boolean, Float, Float>(false, 0.0f, 0.0f);
        PenetrationData<Boolean, Float, Float> BR = new PenetrationData<Boolean, Float, Float>(false, 0.0f, 0.0f);
        PenetrationData<Boolean, Float, Float> BL = new PenetrationData<Boolean, Float, Float>(false, 0.0f, 0.0f);
        if(penetrationAdjust.size() == 1) {
            entity.setMinX(entity.getMinX() + penetrationAdjust.get(0).getPenX());
            entity.setMinY(entity.getMinY() + penetrationAdjust.get(0).getPenY());
        }
        else {
            // count the number of penetrations on each side
            for(int i = 0; i < penetrationAdjust.size(); i++) {
                PenetrationData<Position, Float, Float> tmpPen = penetrationAdjust.get(i);
                if(tmpPen.getInfo().compareTo(Position.RIGHT) == 0) {
                    R.setInfo(true);
                    if(Math.abs(tmpPen.getPenX()) > Math.abs(R.getPenX())) R.setPenX(tmpPen.getPenX());
                    if(Math.abs(tmpPen.getPenY()) > Math.abs(R.getPenY())) R.setPenY(tmpPen.getPenY());
                    //System.out.print("R ");
                }
                if(tmpPen.getInfo().compareTo(Position.LEFT) == 0) {
                    L.setInfo(true);
                    if(Math.abs(tmpPen.getPenX()) > Math.abs(L.getPenX())) L.setPenX(tmpPen.getPenX());
                    if(Math.abs(tmpPen.getPenY()) > Math.abs(L.getPenY())) L.setPenY(tmpPen.getPenY());
                    //System.out.print("L ");
                }
                if(tmpPen.getInfo().compareTo(Position.ABOVE) == 0) {
                    A.setInfo(true);
                    if(Math.abs(tmpPen.getPenX()) > Math.abs(A.getPenX())) A.setPenX(tmpPen.getPenX());
                    if(Math.abs(tmpPen.getPenY()) > Math.abs(A.getPenY())) A.setPenY(tmpPen.getPenY());
                    //System.out.print("A ");
                }
                if(tmpPen.getInfo().compareTo(Position.BELOW) == 0) {
                    B.setInfo(true);
                    if(Math.abs(tmpPen.getPenX()) > Math.abs(B.getPenX())) B.setPenX(tmpPen.getPenX());
                    if(Math.abs(tmpPen.getPenY()) > Math.abs(B.getPenY())) B.setPenY(tmpPen.getPenY());
                    //System.out.print("B ");
                }
                if(tmpPen.getInfo().compareTo(Position.AboveRIGHT) == 0) {
                    AR.setInfo(true);
                    if(Math.abs(tmpPen.getPenX()) > Math.abs(AR.getPenX())) AR.setPenX(tmpPen.getPenX());
                    if(Math.abs(tmpPen.getPenY()) > Math.abs(AR.getPenY())) AR.setPenY(tmpPen.getPenY());
                    //System.out.print("AR ");
                }
                if(tmpPen.getInfo().compareTo(Position.AboveLEFT) == 0) {
                    AL.setInfo(true);
                    if(Math.abs(tmpPen.getPenX()) > Math.abs(AL.getPenX())) AL.setPenX(tmpPen.getPenX());
                    if(Math.abs(tmpPen.getPenY()) > Math.abs(AL.getPenY())) AL.setPenY(tmpPen.getPenY());
                    //System.out.print("AL ");
                }
                if(tmpPen.getInfo().compareTo(Position.BelowRIGHT) == 0) {
                    BR.setInfo(true);
                    if(Math.abs(tmpPen.getPenX()) > Math.abs(BR.getPenX())) BR.setPenX(tmpPen.getPenX());
                    if(Math.abs(tmpPen.getPenY()) > Math.abs(BR.getPenY())) BR.setPenY(tmpPen.getPenY());
                    //System.out.print("BR ");
                }
                if(tmpPen.getInfo().compareTo(Position.BelowLEFT) == 0) {
                    BL.setInfo(true);
                    if(Math.abs(tmpPen.getPenX()) > Math.abs(BL.getPenX())) BL.setPenX(tmpPen.getPenX());
                    if(Math.abs(tmpPen.getPenY()) > Math.abs(BL.getPenY())) BL.setPenY(tmpPen.getPenY());
                    //System.out.print("BL ");
                }
            }
            // Below Left Corner
            if(((L.getInfo() || B.getInfo() || BL.getInfo()) && (AL.getInfo() && BR.getInfo()))
                    || (L.getInfo() && B.getInfo())
                    || (B.getInfo() && AL.getInfo())
                    || (L.getInfo() && BR.getInfo())) {
                entity.setMinX(entity.getMinX() + Math.max(L.getPenX(), Math.max(AL.getPenX(), BL.getPenX())));
                entity.setMinY(entity.getMinY() + Math.min(B.getPenY(), Math.min(BR.getPenY(), BL.getPenY())));
                //System.out.print("BLCorner ");
            }
            // Above Left Corner
            else if(((L.getInfo() || A.getInfo() || AL.getInfo()) && (AR.getInfo() && BL.getInfo()))
                    || (L.getInfo() && A.getInfo())
                    || (A.getInfo() && BL.getInfo())
                    || (L.getInfo() && AR.getInfo())) {
                entity.setMinX(entity.getMinX() + Math.max(L.getPenX(), Math.max(AL.getPenX(), BL.getPenX())));
                entity.setMinY(entity.getMinY() + Math.max(A.getPenY(), Math.max(AR.getPenY(), AL.getPenY())));
                //System.out.print("ALCorner ");
            }
            // Below Right Corner
            else if(((R.getInfo() || B.getInfo() || BR.getInfo()) && (AR.getInfo() && BL.getInfo()))
                    || (R.getInfo() && B.getInfo())
                    || (B.getInfo() && AR.getInfo())
                    || (R.getInfo() && BL.getInfo())) {
                entity.setMinX(entity.getMinX() + Math.min(R.getPenX(), Math.min(AR.getPenX(), BR.getPenX())));
                entity.setMinY(entity.getMinY() + Math.min(B.getPenY(), Math.min(BR.getPenY(), BL.getPenY())));
                //System.out.print("BRCorner ");
            }
            // Above Right Corner
            else if(((R.getInfo() || A.getInfo() || AR.getInfo()) && (AL.getInfo() && BR.getInfo()))
                    || (R.getInfo() && A.getInfo())
                    || (A.getInfo() && BR.getInfo())
                    || (R.getInfo() && AL.getInfo())) {
                entity.setMinX(entity.getMinX() + Math.min(R.getPenX(), Math.min(AR.getPenX(), BR.getPenX())));
                entity.setMinY(entity.getMinY() + Math.max(A.getPenY(), Math.max(AR.getPenY(), AL.getPenY())));
                //System.out.print("ARCorner ");
            }
            // Left in general
            else if(L.getInfo() || ((AL.getInfo() && !BR.getInfo()) && (BL.getInfo() && !AR.getInfo()))) {
                entity.setMinX(entity.getMinX() + Math.max(L.getPenX(), Math.max(AL.getPenX(), BL.getPenX())));
                //System.out.print("LSide ");
            }
            // Right in general
            else if(R.getInfo() || ((AR.getInfo() && !BL.getInfo()) && (BR.getInfo() && !AL.getInfo()))) {
                entity.setMinX(entity.getMinX() + Math.min(R.getPenX(), Math.min(AR.getPenX(), BR.getPenX())));
                //System.out.print("RSide ");
            }
            // Above in general
            else if(A.getInfo() || ((AR.getInfo() && !BL.getInfo()) && (AL.getInfo() && !BR.getInfo()))) {
                entity.setMinY(entity.getMinY() + Math.max(A.getPenY(), Math.max(AL.getPenY(), AR.getPenY())));
                //System.out.print("ASide ");
            }
            // Below in general
            else if(B.getInfo() || ((BR.getInfo() && !AL.getInfo()) && (BL.getInfo() && !AR.getInfo()))) {
                entity.setMinY(entity.getMinY() + Math.min(B.getPenY(), Math.min(BL.getPenY(), BR.getPenY())));
                //System.out.print("BSide ");
            }
            // Opposite corners AL & BR
            else if(AL.getInfo() && BR.getInfo()) {
                if(Math.abs(AL.getPenY()) > Math.abs(AL.getPenX()) && Math.abs(BR.getPenY()) < Math.abs(BR.getPenX())) {
                    entity.setMinX(entity.getMinX() + Math.min(R.getPenX(), Math.min(AR.getPenX(), BR.getPenX())));
                    entity.setMinY(entity.getMinY() + Math.max(A.getPenY(), Math.max(AR.getPenY(), AL.getPenY())));
                    //System.out.print("ARCorner_2 ");
                }
                else if(Math.abs(AL.getPenY()) < Math.abs(AL.getPenX()) && Math.abs(BR.getPenY()) > Math.abs(BR.getPenX())) {
                    entity.setMinX(entity.getMinX() + Math.max(L.getPenX(), Math.max(AL.getPenX(), BL.getPenX())));
                    entity.setMinY(entity.getMinY() + Math.min(B.getPenY(), Math.min(BR.getPenY(), BL.getPenY())));
                    //System.out.print("BLCorner_2 ");
                }
                else {
                    // FIND SOLUTION TO THIS CASE
                    //System.out.print("NOTCOVERED 1");
                }
            }
            // Opposite corners AR & BL
            else if(AR.getInfo() && BL.getInfo()) {
                if(Math.abs(AR.getPenY()) > Math.abs(AR.getPenX()) && Math.abs(BL.getPenY()) < Math.abs(BL.getPenX())) {
                    entity.setMinX(entity.getMinX() + Math.max(L.getPenX(), Math.max(AL.getPenX(), BL.getPenX())));
                    entity.setMinY(entity.getMinY() + Math.max(A.getPenY(), Math.max(AR.getPenY(), AL.getPenY())));
                    //System.out.print("ALCorner_2 ");
                }
                else if(Math.abs(AR.getPenY()) < Math.abs(AR.getPenX()) && Math.abs(BL.getPenY()) > Math.abs(BL.getPenX())) {
                    entity.setMinX(entity.getMinX() + Math.min(R.getPenX(), Math.min(AR.getPenX(), BR.getPenX())));
                    entity.setMinY(entity.getMinY() + Math.min(B.getPenY(), Math.min(BR.getPenY(), BL.getPenY())));
                    //System.out.print("BRCorner_2 ");
                }
                else {
                    // FIND SOLUTION TO THIS CASE
                    //System.out.print("NOTCOVERED 2");
                }
            }
            else {
                // FIND SOLUTION TO THIS CASE
                //System.out.print("NOTCOVERED 3");
            }
            //System.out.print("\n");
        }
    }
    
    public static float findDistance(Entity obj1, Entity obj2) {
        float relX = obj1.getMidX() - obj2.getMidX();
        float relY = obj1.getMidY() - obj2.getMidY();
        float dist = relX*relX + relY*relY;
        return dist;
    }
}
