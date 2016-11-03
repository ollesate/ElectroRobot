package olof.sjoholm.Net.Both;

import olof.sjoholm.Net.Envelope;

/**
 * Created by sjoholm on 09/10/16.
 */
public interface OnMessageListener {

    void onMessage(Client client, Envelope envelope);

}
