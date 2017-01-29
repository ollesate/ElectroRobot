package olof.sjoholm.Interfaces;

import olof.sjoholm.Net.Envelope;

public interface OnMessageReceivedListener {

    void onMessage(Envelope envelope, Long clientId);

}
