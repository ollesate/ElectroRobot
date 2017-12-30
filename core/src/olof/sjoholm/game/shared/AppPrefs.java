package olof.sjoholm.game.shared;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class AppPrefs {
    private static final String LAST_IP_ADDRESS = "LAST_IP_ADDRESS";

    public static void setLastIpAddress(String lastIpAddress) {
        System.out.println("Set last ip " + lastIpAddress);
        getPrefs().putString(LAST_IP_ADDRESS, lastIpAddress);
    }

    public static String getLastIpAddress() {
        return getPrefs().getString(LAST_IP_ADDRESS, null);
    }

    public static void save() {
        getPrefs().flush();
    }

    private static Preferences getPrefs() {
        return Gdx.app.getPreferences("AppPrefs");
    }
}
