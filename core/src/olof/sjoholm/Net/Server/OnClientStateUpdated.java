package olof.sjoholm.Net.Server;

import olof.sjoholm.Net.Both.Client;

/**
 * Created by sjoholm on 09/10/16.
 */

public interface OnClientStateUpdated {

    void onClientConnected(Client client);

    void onClientDisconnected(Client client);

}
