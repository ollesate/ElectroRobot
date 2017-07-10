package olof.sjoholm.GameLogic;

import com.badlogic.gdx.Game;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.Client.CardHandModel;
import olof.sjoholm.Interfaces.IPlayerApi;
import olof.sjoholm.MyGdxGame;
import olof.sjoholm.Net.Game.GameApi;
import olof.sjoholm.Views.GameScreen;
import olof.sjoholm.Net.Game.GameServer;
import olof.sjoholm.Views.LobbyStage;
import olof.sjoholm.Interfaces.OnCardsReceivedListener;
import olof.sjoholm.Utils.Direction;
import olof.sjoholm.Utils.Logger;
import olof.sjoholm.Net.Server.Server;
import olof.sjoholm.Net.ServerConstants;
import olof.sjoholm.Models.CardModel;
import olof.sjoholm.Models.MoveModel;

public class ServerGameController {
    private boolean isDebug = MyGdxGame.isDebug;
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
        int playerId = 0;
        for (IPlayerApi playerApi : getPlayers()) {
            controllers.add(new PlayerController(playerId++, playerApi, new CardHandModel()));
        }
        gameScreen.showGameStage();
        gameManager.startGame(controllers);
    }

    private List<IPlayerApi> getPlayers() {
        if (isDebug) {
            return getMockPlayers();
        } else {
            return gameServer.getConnectedPlayers();
        }
    }

    private List<olof.sjoholm.Interfaces.IPlayerApi> getMockPlayers() {
        List<IPlayerApi> mockPlayers = new ArrayList<IPlayerApi>(){{
           add(new olof.sjoholm.Interfaces.IPlayerApi() {
               @Override
               public void sendCards(List<CardModel> cards) {
                   // Do nothing
               }

               @Override
               public void getCards(OnCardsReceivedListener onCardsReceivedListener) {
                    onCardsReceivedListener.onCardsReceived(
                            new ArrayList<CardModel>(){{
                                new MoveModel(Direction.RIGHT, 1);
                                new MoveModel(Direction.RIGHT, 2);
                                new MoveModel(Direction.RIGHT, 3);
                            }}
                    );
               }
           });
        }};
        return mockPlayers;
    }
}
