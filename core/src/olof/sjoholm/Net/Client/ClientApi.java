package olof.sjoholm.Net.Client;

import olof.sjoholm.Net.Server.Client;
import olof.sjoholm.Net.ServerConstants;

/**
 * Created by sjoholm on 02/10/16.
 */

public class ClientApi {

    public ClientApi() {
        Client client = new Client(ServerConstants.HOST_NAME, ServerConstants.CONNECTION_PORT);
    }
}
