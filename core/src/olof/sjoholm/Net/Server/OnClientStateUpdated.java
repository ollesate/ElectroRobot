package olof.sjoholm.Net.Server;

import olof.sjoholm.Net.Both.Client;


public interface OnClientStateUpdated {

    void onClientConnected(Client client);

    void onClientDisconnected(Client client);

}
