package olof.sjoholm.GameWorld.Utils;

import com.badlogic.gdx.Gdx;


public class Logger {
    private static final String TAG = "Game";

    public static void d(String message) {
        Gdx.app.log(TAG, message);
    }
}
