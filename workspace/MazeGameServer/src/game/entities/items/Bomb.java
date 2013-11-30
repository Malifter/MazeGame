package game.entities.items;

import java.util.Timer;
import java.util.TimerTask;
import engine.physics.RigidBody;
import game.entities.npcs.Player;

public class Bomb extends NotConsumable {
    private int power = 0;
    private int time = 0;
    private Timer timer;
    TimerTask bombClock;
    private boolean isExploded = false;
    private static final int BOMB_POWER = 10;
    private static final int BOMB_TIME = 7;
    
    public Bomb(RigidBody rb) {
        super("items/bomb/bomb.gif/", rb);
       
        //bombClock = new BombTask();
        time = 7;
        power = BOMB_POWER;
        
    }
    
    public void startTimer(Player player) {
        timer = new Timer();
        timer.schedule(new BombTask(), time * 1000); 
        
 
    }
    
    class BombTask extends TimerTask  {
        @Override
        public void run() {
            explode();
            System.out.print("time is up");
            timer.cancel(); 
        }

    }

    public void explode() {
        isExploded =  true;
      
    }

    public int getPower() {
        return power;
    }
}


