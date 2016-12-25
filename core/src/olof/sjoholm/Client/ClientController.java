package olof.sjoholm.Client;

import com.badlogic.gdx.Game;

import olof.sjoholm.Net.ServerConstants;


public class ClientController {
    private ServerConnection serverConnection;

    public ClientController(Game game) {
        serverConnection = new ServerConnection(
                ServerConstants.HOST_NAME, ServerConstants.CONNECTION_PORT
        );

        PlayerGame playerGame = new PlayerGame();
        game.setScreen(playerGame);

        MessageDispatcher messageDispatcher = new MessageDispatcher(playerGame);

        new MessageHandler(serverConnection, messageDispatcher);
    }
}
