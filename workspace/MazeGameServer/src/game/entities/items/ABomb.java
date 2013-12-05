package game.entities.items;

import java.util.Timer;
import java.util.TimerTask;

import engine.physics.RigidBody;
import game.entities.npcs.Player;
import game.enums.AnimationPath;
import game.enums.AnimationState;

public class ABomb extends NotConsumable {
    private Timer timer;
    //private static final int BOMB_POWER = 10;
    private static final int BOMB_TIME = 7;
    
    public ABomb(RigidBody rb){
        super(AnimationPath.BOMB, rb);
        startTimer();
    }
    
    public void startTimer() {
        animState = AnimationState.ACTIVE;
        timer = new Timer();
        timer.schedule(new BombTask(), BOMB_TIME * 1000); 
    }
    
    class BombTask extends TimerTask  {
        @Override
        public void run() {
            explode();
        }
    }

    public void explode() {
        disable();
        System.out.print("time is up");
        //things happens when it explodes
    }


    public void pickUp(Player player) {
        //get pushed around
    }
    
    public void use(Player p) {
        // do nothing
    }
}