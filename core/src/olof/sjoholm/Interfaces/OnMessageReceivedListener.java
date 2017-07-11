package olof.sjoholm.Interfaces;

import olof.sjoholm.Net.Both.Envelope;

public interface OnMessageReceivedListener {

    void onMessage(Envelope envelope, int clientId);
}
