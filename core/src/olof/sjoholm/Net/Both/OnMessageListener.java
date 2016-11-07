package olof.sjoholm.Net.Both;

import olof.sjoholm.Net.Envelope;

public interface OnMessageListener {

    void onMessage(Client client, Envelope envelope);

}
