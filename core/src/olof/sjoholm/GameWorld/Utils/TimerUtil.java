package olof.sjoholm.GameWorld.Utils;

import com.badlogic.gdx.utils.Timer;

/**
 * Created by sjoholm on 25/09/16.
 */

public class TimerUtil {

    public static void startDelayed(Timer.Task task, float secondsDelay) {
        Timer timer = new Timer();
        timer.scheduleTask(task, secondsDelay);
    }

    public static void startRepating(Timer.Task task, float secondsDelay, int repeats) {
        Timer timer = new Timer();
        timer.scheduleTask(task, secondsDelay, secondsDelay, repeats);
    }

}
