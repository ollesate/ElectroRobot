package olof.sjoholm.Utils;

import com.badlogic.gdx.Gdx;


public class Logger {
    private static final String TAG = "Game";

    public static void d(String message) {
        Gdx.app.log(TAG, message);
    }

    public static void e(String message) {
        Gdx.app.error(TAG, message);
    }
}
