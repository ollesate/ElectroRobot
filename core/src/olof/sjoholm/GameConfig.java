package olof.sjoholm;

public class GameConfig {
    private static boolean isServer = false;

    public static boolean isServer() {
        return isServer;
    }

    public static void setServer(boolean isServer) {
        GameConfig.isServer = isServer;
    }
}
