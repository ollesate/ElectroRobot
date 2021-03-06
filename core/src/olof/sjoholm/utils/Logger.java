package olof.sjoholm.utils;

import com.badlogic.gdx.Gdx;


public class Logger {
    private static final String TAG = "LibgdxGame";

    public static void d(String message) {
        Gdx.app.log(TAG, message);
    }

    public static void e(String message) {
        Gdx.app.error(TAG, message);
    }
}
