package olof.sjoholm.GameLogic;

import com.badlogic.gdx.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import olof.sjoholm.Client.CardHandModel;
import olof.sjoholm.Interfaces.IPlayerApi;
import olof.sjoholm.Net.Both.Envelope;
import olof.sjoholm.Utils.Logger;
import olof.sjoholm.Views.GameScreen;
import olof.sjoholm.Views.LobbyStage;

public class ServerGameController  {
    private final Map<Integer, IPlayerApi> players = new ConcurrentHashMap<Integer, IPlayerApi>();
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

        gameManager = new GameManager(gameScreen.getGameStage());
    }

    private void initServer() {
        Logger.d("Starting game server");
    }

    private void startGame() {
        List<PlayerController> controllers = new ArrayList<PlayerController>();
        int playerId = 0;
        for (IPlayerApi playerApi : players.values()) {
            controllers.add(new PlayerController(playerId++, playerApi, new CardHandModel()));
        }
        gameScreen.showGameStage();
        gameManager.startGame(controllers);
//        server.broadcast(new Envelope.StartGame());
    }

    public void onMessageReceived(Envelope envelope) {
        if (envelope instanceof Envelope.ClientConnection) {
//            PlayerApi playerApi = new PlayerApi(envelope.getContents(NetClient.class));
//            players.put(envelope.getOwnerId(), playerApi);
        } else if (envelope instanceof Envelope.ClientDisconnection) {
            players.remove(envelope.getOwnerId());
        }
    }
}
