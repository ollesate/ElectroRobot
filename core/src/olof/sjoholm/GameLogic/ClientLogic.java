package olof.sjoholm.GameLogic;

import olof.sjoholm.Net.Server.ServerApi;

/**
 * Created by sjoholm on 02/10/16.
 */

public class ClientLogic {
    private ServerApi serverApi;

    public ClientLogic(ServerApi serverApi) {
        this.serverApi = serverApi;
    }

    private void onIncomingMessage(String message) {
        if ("GiveCards".equals(message)) {
            serverApi.sendCards();
        }
    }

}
