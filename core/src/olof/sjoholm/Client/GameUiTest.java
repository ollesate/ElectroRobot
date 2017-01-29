package olof.sjoholm.Client;

import com.badlogic.gdx.Game;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.GameLogic.GameManager;
import olof.sjoholm.GameWorld.Game.PlayerController;
import olof.sjoholm.GameWorld.Server.GameApi;
import olof.sjoholm.GameWorld.Server.GameScreen;
import olof.sjoholm.GameWorld.Server.IPlayerApi;
import olof.sjoholm.GameWorld.Server.OnCardsReceivedListener;
import olof.sjoholm.GameWorld.Utils.Direction;
import olof.sjoholm.GameWorld.Utils.Logger;
import olof.sjoholm.common.CardModel;
import olof.sjoholm.common.MoveModel;

public class GameUiTest {
    private final GameScreen gameScreen;

    public GameUiTest(Game game) {
        gameScreen = new GameScreen(null);
        gameScreen.showGameStage();
        game.setScreen(gameScreen);
        startGame();
    }

    private void startGame() {
        List<PlayerController> controllers = new ArrayList<PlayerController>();
        int playerId = 0;
        for (IPlayerApi playerApi : getMockPlayers()) {
            controllers.add(new PlayerController(playerId++, playerApi, new CardHandModel()));
        }
        gameScreen.showGameStage();
        GameManager gameManager = new GameManager(gameScreen.getGameStage(), new GameApi(null){
            @Override
            public void startGame() {
                // Do nothing
            }
        });
        gameManager.startGame(controllers);
    }

    private List<IPlayerApi> getMockPlayers() {
        List<IPlayerApi> mockPlayers = new ArrayList<IPlayerApi>(){{
            add(new IPlayerApi() {
                @Override
                public void sendCards(List<CardModel> cards) {
                    // Do nothing
                }

                @Override
                public void getCards(OnCardsReceivedListener onCardsReceivedListener) {
                    List<CardModel> cards = new ArrayList<CardModel>(){{
                        add(new MoveModel(Direction.UP, 1));
                        add(new MoveModel(Direction.UP, 2));
                        add(new MoveModel(Direction.UP, 3));
                    }};
                    onCardsReceivedListener.onCardsReceived(cards);
                }
            });
        }};
        return mockPlayers;
    }
}
