package olof.sjoholm;

public class AndroidConfig {
    private static boolean isServer;

    static void instantiate(boolean isServer) {
        AndroidConfig.isServer = isServer;
    }

    public static boolean isServer() {
        return AndroidConfig.isServer;
    }
}
