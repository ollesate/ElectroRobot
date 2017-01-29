package olof.sjoholm.Client;

import com.badlogic.gdx.Game;

import olof.sjoholm.GameWorld.Server.GameScreen;

public class GameUiTest {

    public GameUiTest(Game game) {
        GameScreen gameScreen = new GameScreen(null);
        gameScreen.showGameStage();
        game.setScreen(gameScreen);
    }
}
