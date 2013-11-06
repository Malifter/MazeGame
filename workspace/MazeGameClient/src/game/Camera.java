package game;
/*
* Classname:            Camera.java
*
* Version information:  1.0
*
* Date:                 10/30/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

import static org.lwjgl.util.glu.GLU.gluLookAt;

import org.lwjgl.opengl.GL11;

import engine.serializable.SerializedEntity;
import engine.serializable.SerializedObject;

/**
 * Camera 
 */
public class Camera {
    
    private static final int TILESIZE = 16;
    private static final int screenHeight = 240;
    private static final int levelWidth = 144;
    SerializedEntity focus;
    float posX, posY;
    float upX, upY;
    float offsetX, offsetY;
    MazeGameClient game;
    boolean mode = true;
    boolean switchMode = false;
    
    /**
     * Constructor
     * @param g
     * @param anImage
     * @param x
     * @param y
     */
    public Camera(Game g) {
        game = (MazeGameClient)g;
        offsetX = (screenHeight/2)+34; //focus' half width - window half width
        offsetY = -(levelWidth/2); //focus' half height - window half height
    }
    
    public void setFocusObject(SerializedEntity so) {
        focus = so;
    }
    
    public void setOrientation(float posX, float posY, float upX, float upY) {
        this.posX = posX;
        this.posY = posY;
        this.upX = upX;
        this.upY = upY;
    }
    
    public void setMode(boolean m) {
        mode = m;
    }
    
    public void update() {
        if(game.cameraMode.isDown() && !switchMode) {
            mode = !mode;
            switchMode = true;
        } else if (!game.cameraMode.isDown() && switchMode) {
            switchMode = false;
            
        }
        
        if(mode) {
            if(focus != null) {
                posX = focus.getPosition().getX()-offsetX;
                /*if(posX <= 0) {
                    posX = 0;
                } else if (posX >= levelWidth) {
                    posX = levelWidth;
                }*/
                posY = focus.getPosition().getY()+offsetY;//((float)(focus.getPosition().getY()/screenHeight)*screenHeight)+(TILESIZE/2.0f);//+offsetY;
            }
            GL11.glLoadIdentity();
            GL11.glScaled(4.2666666666666666666666666666667, 4.2666666666666666666666666666667, 0);
            GL11.glTranslatef(-posX, -posY, 0);
            // x = TILESIZE = move left
            // x = -TILESIZE = move right
            // y = TILESIZE = move up;
            // y = -TILESIZE = move down;
            // z = TILESIZE ??
        }
        else {
            posY = 0;
            posX = 0;
        }
        //gluLookAt(posX,posY,.1f,posX,posY,-.7f,upX,upY,0f);
    }
    
    public float getPosX() {
        return posX;
    }
    
    public float getPosY() {
        return posY;
    }

//    void lookAt(Vector, Vector, Vector);
//    void lookAt(Vector, Object*, Vector, bool follow);
//    void lookRelativeAt(Vector, Object*, Vector, bool follow);
//    void setEye(Vector);
//    void setCenter(Vector);
//    void setUp(Vector);
//    void setAnchor(Object*);
//    void setFollow(bool follow = true);
//
//    void setOrtho(double  left,  double  right,  double  bottom,  double  top,  double  nearVal,  double  farVal);
//    void setPerspective(double  fovy,  double  aspect,  double  zNear,  double  zFar);
    
}
