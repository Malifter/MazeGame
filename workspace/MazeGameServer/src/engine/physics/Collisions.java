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

import engine.Vector2f;

import game.entities.Entity;
import game.entities.environment.Tile;

public class Collisions {
    private static enum Position { NONE, RIGHT, LEFT, ABOVE, BELOW, AboveRIGHT, AboveLEFT, BelowRIGHT, BelowLEFT};
    private Collisions(){}
    
    /**
     * applyEnvironmentCollision: A simple class that detects collision between an entity and the environment. 
     * @param dynamicObj
     * @param staticObj
     * @return
     */
    public static void applyEnvironmentCorrections(Entity obj, ArrayList<Tile> environment) {
        ArrayList<PenetrationData<Position>> penetrationAdjust = new ArrayList<PenetrationData<Position>>();
        for(Entity env: environment) {
            if(env.getRigidBody().isEnabled() && detectCollision(obj, env)) {
                PenetrationData<Position> penetrationData = calculatePenetration(obj.getRigidBody(), env.getRigidBody());
                if(penetrationData != null) penetrationAdjust.add(penetrationData);
            }
        }
        if(penetrationAdjust.size() > 0) applyMultiplePenetrationCorrections(obj.getRigidBody(), penetrationAdjust);
    }
    
    public static void applySingleCorrection(Entity to, Entity from) {
        applySinglePenetrationCorrection(to.getRigidBody(), calculatePenetration(to.getRigidBody(), from.getRigidBody()));
    }
    
    public static void detectAndApplySingleCorrection(Entity to, Entity from) {
        if(detectCollision(to, from)){
            applySinglePenetrationCorrection(to.getRigidBody(), calculatePenetration(to.getRigidBody(), from.getRigidBody()));
        }
    }
    
    public static void applyEqualCorrection(Entity obj1, Entity obj2) {
        PenetrationData<Position> penAdjust = calculatePenetration(obj1.getRigidBody(), obj2.getRigidBody());
        penAdjust.setPenX(penAdjust.getPenX()/2.0f);
        penAdjust.setPenY(penAdjust.getPenY()/2.0f);
        applySinglePenetrationCorrection(obj1.getRigidBody(), penAdjust);
        penAdjust.setPenX(-penAdjust.getPenX());
        penAdjust.setPenY(-penAdjust.getPenY());
        applySinglePenetrationCorrection(obj2.getRigidBody(), penAdjust);
    }
    
    public static void detectAndApplyEqualCorrection(Entity obj1, Entity obj2) {
        if(detectCollision(obj1, obj2)) {
            PenetrationData<Position> penAdjust = calculatePenetration(obj1.getRigidBody(), obj2.getRigidBody());
            penAdjust.setPenX(penAdjust.getPenX()/2.0f);
            penAdjust.setPenY(penAdjust.getPenY()/2.0f);
            applySinglePenetrationCorrection(obj1.getRigidBody(), penAdjust);
            penAdjust.setPenX(-penAdjust.getPenX());
            penAdjust.setPenY(-penAdjust.getPenY());
            applySinglePenetrationCorrection(obj2.getRigidBody(), penAdjust);
        }
    }
    
    public static void applySingleRadialCorrection(Entity to, Entity from) {
        Vector2f direction = to.getRigidBody().getMid().sub(from.getRigidBody().getMid());
        direction.x = direction.x > 0.0f ? 1.0f : direction.x < 0.0f ? -1.0f : 0.0f;
        direction.y = direction.y > 0.0f ? 1.0f : direction.y < 0.0f ? -1.0f : 0.0f;
        to.getRigidBody().move(direction.x, direction.y);
    }
    
    public static void detectAndApplySingleRadialCorrection(Entity to, Entity from) {
        if(detectCollisionSphere(to.getRigidBody(), from.getRigidBody())) {
            Vector2f direction = to.getRigidBody().getMid().sub(from.getRigidBody().getMid());
            direction.x = direction.x > 0.0f ? 1.0f : direction.x < 0.0f ? -1.0f : 0.0f;
            direction.y = direction.y > 0.0f ? 1.0f : direction.y < 0.0f ? -1.0f : 0.0f;
            to.getRigidBody().move(direction.x, direction.y);
        }
    }
    
    public static void applyEqualRadialCorrection(Entity obj1, Entity obj2) {
        Vector2f direction = obj1.getRigidBody().getMid().sub(obj2.getRigidBody().getMid());
        direction.x = direction.x > 0.0f ? 1.0f : direction.x < 0.0f ? -1.0f : 0.0f;
        direction.y = direction.y > 0.0f ? 1.0f : direction.y < 0.0f ? -1.0f : 0.0f;
        direction = direction.div(2.0f);
        obj1.getRigidBody().move(direction.x, direction.y);
        obj2.getRigidBody().move(-direction.x, -direction.y);
    }
    
    public static void detectAndApplyEqualRadialCorrection(Entity obj1, Entity obj2) {
        if(detectCollisionSphere(obj1.getRigidBody(), obj2.getRigidBody())) {
            Vector2f direction = obj1.getRigidBody().getMid().sub(obj2.getRigidBody().getMid());
            direction.x = direction.x > 0.0f ? 1.0f : direction.x < 0.0f ? -1.0f : 0.0f;
            direction.y = direction.y > 0.0f ? 1.0f : direction.y < 0.0f ? -1.0f : 0.0f;
            direction = direction.div(2.0f);
            obj1.getRigidBody().move(direction.x, direction.y);
            obj2.getRigidBody().move(-direction.x, -direction.y);
        }
    }
    
    /**
     * detectCollision: <add description>
     * @param ent1
     * @param ent2
     * @return
     */
    public static boolean detectCollision(Entity obj1, Entity obj2) {
        return detectCollisionSphere(obj1.getRigidBody(), obj2.getRigidBody()) ?
               detectCollisionBox(obj1.getRigidBody(), obj2.getRigidBody()) : false;
    }
    
    public static boolean detectCollisionSphere(RigidBody rb1, RigidBody rb2) {
        float dist = findDistance(rb1, rb2);
        float minDist = rb1.getRadius() + rb2.getRadius();
        return dist <= minDist;
    }
    
    public static boolean detectCollisionBox(RigidBody rb1, RigidBody rb2) {  
        if(rb1.getMax().x < rb2.getMin().x)
            return false;
        if(rb1.getMin().x >  rb2.getMax().x)
            return false;
        if(rb1.getMax().y <  rb2.getMin().y)
            return false;
        if(rb1.getMin().y >  rb2.getMax().y)
            return false;
        return true;
    }
    
    /**
     * 
     * calculatePenetration: <add description>
     * @param dynamicRB - The one penetrating
     * @param staticRB
     * @return
     */
    private static PenetrationData<Position> calculatePenetration(RigidBody to, RigidBody from) {
        //calculate x and y penetration levels
        float penx = 0;
        float peny = 0;
        Position pos = Position.NONE;
        Vector2f toMax = to.getMax();
        Vector2f toMin = to.getMin();
        Vector2f fromMax = from.getMax();
        Vector2f fromMin = from.getMin();
        
        // tile below - neg
        if(toMax.y < fromMax.y && toMin.y < fromMin.y) {
            peny = -(toMax.y - fromMin.y);
            if(peny == 0) {
                peny = -0.0000001f;
            }
            pos = Position.BELOW;
            //System.out.printf("below ");
        }
        // tile above - pos
        else if(fromMax.y < toMax.y && fromMin.y < toMin.y) {
            peny = (fromMax.y - toMin.y);
            if(peny == 0) {
                peny = 0.0000001f;
            }
            pos = Position.ABOVE;
            //System.out.printf("above ");
        }
        // tile right - neg
        if(toMax.x < fromMax.x && toMin.x < fromMin.x) {
            penx = -(toMax.x - fromMin.x);
            if(penx == 0) {
                penx = -0.0000001f;
            }
            pos = pos.compareTo(Position.NONE) == 0 ? Position.RIGHT : (pos.compareTo(Position.BELOW) == 0 ? Position.BelowRIGHT : Position.AboveRIGHT);
            //System.out.printf("right ");
        }
        // tile left - pos
        else if(fromMax.x < toMax.x && fromMin.x < toMin.x) {
            penx = (fromMax.x - toMin.x);
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

        return (penx != 0 || peny != 0) ? new PenetrationData<Position>(pos, penx, peny) : null;
    }
    
    private static void applySinglePenetrationCorrection(RigidBody rb, PenetrationData<Position> penetrationAdjust) {
        rb.move(penetrationAdjust.getPenX(), penetrationAdjust.getPenY());
    }
    
    private static void applyMultiplePenetrationCorrections(RigidBody rb, ArrayList<PenetrationData<Position>> penetrationAdjust) {
        if(penetrationAdjust.size() == 1) {
            rb.move(penetrationAdjust.get(0).getPenX(), penetrationAdjust.get(0).getPenY());
        }
        else {
            PenetrationData<Boolean> R = new PenetrationData<Boolean>(false, 0.0f, 0.0f);
            PenetrationData<Boolean> L = new PenetrationData<Boolean>(false, 0.0f, 0.0f);
            PenetrationData<Boolean> A = new PenetrationData<Boolean>(false, 0.0f, 0.0f);
            PenetrationData<Boolean> B = new PenetrationData<Boolean>(false, 0.0f, 0.0f);
            PenetrationData<Boolean> AR = new PenetrationData<Boolean>(false, 0.0f, 0.0f);
            PenetrationData<Boolean> AL = new PenetrationData<Boolean>(false, 0.0f, 0.0f);
            PenetrationData<Boolean> BR = new PenetrationData<Boolean>(false, 0.0f, 0.0f);
            PenetrationData<Boolean> BL = new PenetrationData<Boolean>(false, 0.0f, 0.0f);
            
            // count the number of penetrations on each side
            for(int i = 0; i < penetrationAdjust.size(); i++) {
                PenetrationData<Position> tmpPen = penetrationAdjust.get(i);
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
                rb.move(Math.max(L.getPenX(), Math.max(AL.getPenX(), BL.getPenX())), Math.min(B.getPenY(), Math.min(BR.getPenY(), BL.getPenY())));
                //System.out.print("BLCorner ");
            }
            // Above Left Corner
            else if(((L.getInfo() || A.getInfo() || AL.getInfo()) && (AR.getInfo() && BL.getInfo()))
                    || (L.getInfo() && A.getInfo())
                    || (A.getInfo() && BL.getInfo())
                    || (L.getInfo() && AR.getInfo())) {
                rb.move(Math.max(L.getPenX(), Math.max(AL.getPenX(), BL.getPenX())), Math.max(A.getPenY(), Math.max(AR.getPenY(), AL.getPenY())));
                //System.out.print("ALCorner ");
            }
            // Below Right Corner
            else if(((R.getInfo() || B.getInfo() || BR.getInfo()) && (AR.getInfo() && BL.getInfo()))
                    || (R.getInfo() && B.getInfo())
                    || (B.getInfo() && AR.getInfo())
                    || (R.getInfo() && BL.getInfo())) {
                rb.move(Math.min(R.getPenX(), Math.min(AR.getPenX(), BR.getPenX())), Math.min(B.getPenY(), Math.min(BR.getPenY(), BL.getPenY())));
                //System.out.print("BRCorner ");
            }
            // Above Right Corner
            else if(((R.getInfo() || A.getInfo() || AR.getInfo()) && (AL.getInfo() && BR.getInfo()))
                    || (R.getInfo() && A.getInfo())
                    || (A.getInfo() && BR.getInfo())
                    || (R.getInfo() && AL.getInfo())) {
                rb.move(Math.min(R.getPenX(), Math.min(AR.getPenX(), BR.getPenX())), Math.max(A.getPenY(), Math.max(AR.getPenY(), AL.getPenY())));
                //System.out.print("ARCorner ");
            }
            // Left in general
            else if(L.getInfo() || ((AL.getInfo() && !BR.getInfo()) && (BL.getInfo() && !AR.getInfo()))) {
                rb.move(Math.max(L.getPenX(), Math.max(AL.getPenX(), BL.getPenX())), 0);
                //System.out.print("LSide ");
            }
            // Right in general
            else if(R.getInfo() || ((AR.getInfo() && !BL.getInfo()) && (BR.getInfo() && !AL.getInfo()))) {
                rb.move(Math.min(R.getPenX(), Math.min(AR.getPenX(), BR.getPenX())), 0);
                //System.out.print("RSide ");
            }
            // Above in general
            else if(A.getInfo() || ((AR.getInfo() && !BL.getInfo()) && (AL.getInfo() && !BR.getInfo()))) {
                rb.move(0, Math.max(A.getPenY(), Math.max(AL.getPenY(), AR.getPenY())));
                //System.out.print("ASide ");
            }
            // Below in general
            else if(B.getInfo() || ((BR.getInfo() && !AL.getInfo()) && (BL.getInfo() && !AR.getInfo()))) {
                rb.move(0, Math.min(B.getPenY(), Math.min(BL.getPenY(), BR.getPenY())));
                //System.out.print("BSide ");
            }
            // Opposite corners AL & BR
            else if(AL.getInfo() && BR.getInfo()) {
                if(Math.abs(AL.getPenY()) > Math.abs(AL.getPenX()) && Math.abs(BR.getPenY()) < Math.abs(BR.getPenX())) {
                    rb.move(Math.min(R.getPenX(), Math.min(AR.getPenX(), BR.getPenX())), Math.max(A.getPenY(), Math.max(AR.getPenY(), AL.getPenY())));
                    //System.out.print("ARCorner_2 ");
                }
                else if(Math.abs(AL.getPenY()) < Math.abs(AL.getPenX()) && Math.abs(BR.getPenY()) > Math.abs(BR.getPenX())) {
                    rb.move(Math.max(L.getPenX(), Math.max(AL.getPenX(), BL.getPenX())), Math.min(B.getPenY(), Math.min(BR.getPenY(), BL.getPenY())));
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
                    rb.move(Math.max(L.getPenX(), Math.max(AL.getPenX(), BL.getPenX())), Math.max(A.getPenY(), Math.max(AR.getPenY(), AL.getPenY())));
                    //System.out.print("ALCorner_2 ");
                }
                else if(Math.abs(AR.getPenY()) < Math.abs(AR.getPenX()) && Math.abs(BL.getPenY()) > Math.abs(BL.getPenX())) {
                    rb.move(Math.min(R.getPenX(), Math.min(AR.getPenX(), BR.getPenX())), Math.min(B.getPenY(), Math.min(BR.getPenY(), BL.getPenY())));
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
    
    public static float findDistance(RigidBody rb1, RigidBody rb2) {
        float relX = rb1.getMid().x - rb2.getMid().x;
        float relY = rb1.getMid().y - rb2.getMid().y;
        float dist = relX*relX + relY*relY;
        return (float) Math.sqrt(dist);
    }
    
    public static float findDistance(RigidBody rb, Vector2f point) {
        float relX = rb.getMid().x - point.x;
        float relY = rb.getMid().y - point.y;
        float dist = relX*relX + relY*relY;
        return (float) Math.sqrt(dist);
    }
    
    public static float findDistance(Vector2f point1, Vector2f point2) {
        float relX = point1.x - point2.x;
        float relY = point1.y - point2.y;
        float dist = relX*relX + relY*relY;
        return (float) Math.sqrt(dist);
    }
}
