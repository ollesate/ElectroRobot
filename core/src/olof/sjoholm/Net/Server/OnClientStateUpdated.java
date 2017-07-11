package olof.sjoholm.Net.Server;

import olof.sjoholm.Net.Both.ConnectionMessageWorker;


public interface OnClientStateUpdated {

    void onClientConnected(ConnectionMessageWorker connectionMessageWorker);

    void onClientDisconnected(ConnectionMessageWorker connectionMessageWorker);

}
