package olof.sjoholm.Utils;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.Interfaces.OnMessageReceivedListener;
import olof.sjoholm.Net.Both.Envelope;

public class Robo {
    public static boolean isServer;

    private static final List<OnMessageReceivedListener> subscribeList =
            new ArrayList<OnMessageReceivedListener>();

    public static void subscribeServerMessage(OnMessageReceivedListener onMessageReceivedListener) {
        synchronized (subscribeList) {
            subscribeList.add(onMessageReceivedListener);
        }
    }

    public static void broadcast(Envelope envelope, int clientId) {
        synchronized (subscribeList) {
            for (OnMessageReceivedListener onMessageReceivedListener : subscribeList) {
                onMessageReceivedListener.onMessage(envelope, clientId);
            }
        }
    }
}
