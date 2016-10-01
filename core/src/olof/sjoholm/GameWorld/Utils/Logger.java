package olof.sjoholm.GameWorld.Utils;

import com.badlogic.gdx.Gdx;

/**
 * Created by sjoholm on 01/10/16.
 */

public class Logger {
    private static final String TAG = "Game";

    public static void d(String message) {
        Gdx.app.log(TAG, message);
    }
}
