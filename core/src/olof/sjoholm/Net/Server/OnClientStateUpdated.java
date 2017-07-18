package olof.sjoholm.Net.Server;

import olof.sjoholm.Net.Both.NetClient;


public interface OnClientStateUpdated {

    void onClientConnected(NetClient netClient);

    void onClientDisconnected(NetClient netClient);

}
