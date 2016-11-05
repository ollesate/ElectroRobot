package olof.sjoholm.GameWorld.Server;

import com.badlogic.gdx.Game;

import olof.sjoholm.GameWorld.Server.Screens.GameScreen;
import olof.sjoholm.GameWorld.Server.Screens.LobbyScreen;
import olof.sjoholm.GameWorld.Utils.Logger;
import olof.sjoholm.Net.Server.Server;
import olof.sjoholm.Net.ServerConstants;

public class ServerGame {
    private Game game;
    private LobbyScreen lobbyScreen;
    private GameScreen gameScreen;

    public ServerGame(Game game) {
        this.game = game;

        Logger.d("Starting game server");
        Server server = new Server(ServerConstants.HOST_NAME, ServerConstants.CONNECTION_PORT);
        server.start();

        Robo.server = server;
        GameServer gameServer = new GameServer(server);

        initScreens(gameServer);
        game.setScreen(lobbyScreen);
    }

    private void initScreens(PlayerManager playerManager) {
        lobbyScreen = new LobbyScreen(new LobbyScreen.OnStartGameListener() {
            @Override
            public void onStartGame() {
                startGame();
            }
        });
        gameScreen = new GameScreen(playerManager);
    }

    private void startGame() {
        game.setScreen(gameScreen);
    }
}
