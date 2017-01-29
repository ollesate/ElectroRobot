package olof.sjoholm.GameWorld.Server;

import com.badlogic.gdx.Game;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.Client.CardHandModel;
import olof.sjoholm.GameLogic.GameManager;
import olof.sjoholm.GameWorld.Game.PlayerController;
import olof.sjoholm.GameWorld.Server.Screens.LobbyStage;
import olof.sjoholm.GameWorld.Utils.Logger;
import olof.sjoholm.Net.Server.Server;
import olof.sjoholm.Net.ServerConstants;

public class ServerGameController {
    private Server server;
    private GameServer gameServer;
    private final GameScreen gameScreen;
    private final GameManager gameManager;

    public ServerGameController(Game game) {
        initServer();

        gameScreen = new GameScreen(new LobbyStage.OnStartGameListener() {
            @Override
            public void onStartClicked() {
                startGame();
            }
        });
        game.setScreen(gameScreen);

        gameManager = new GameManager(gameScreen.getGameStage(), new GameApi(gameServer));
    }

    private void initServer() {
        Logger.d("Starting game server");
        server = new Server(ServerConstants.HOST_NAME, ServerConstants.CONNECTION_PORT);
        gameServer = new GameServer(server);
        server.start();
    }

    private void startGame() {
        List<PlayerController> controllers = new ArrayList<PlayerController>();
        for (PlayerApi playerApi : gameServer.getConnectedPlayers()) {
            controllers.add(new PlayerController(playerApi, new CardHandModel()));
        }
        gameScreen.showGameStage();
        gameManager.startGame(controllers);
    }
}
