package olof.sjoholm.Utils;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.Interfaces.OnMessageReceivedListener;
import olof.sjoholm.Net.Envelope;

public class Robo {
    public static boolean isServer;

    private static final List<OnMessageReceivedListener> subscribeList =
            new ArrayList<OnMessageReceivedListener>();

    public static void subscribeServerMessage(OnMessageReceivedListener onMessageReceivedListener) {
        synchronized (subscribeList) {
            subscribeList.add(onMessageReceivedListener);
        }
    }

    public static void broadcast(Envelope envelope, long clientId) {
        synchronized (subscribeList) {
            for (OnMessageReceivedListener onMessageReceivedListener : subscribeList) {
                onMessageReceivedListener.onMessage(envelope, clientId);
            }
        }
    }
}
