package olof.sjoholm.Views;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;

import olof.sjoholm.Interfaces.IGameStage;
import olof.sjoholm.Views.GameStage;
import olof.sjoholm.Views.LobbyStage;

public class GameScreen extends ScreenAdapter {
    private Stage currentStage;
    private final LobbyStage lobbyStage;
    private final GameStage gameStage;

    public GameScreen(LobbyStage.OnStartGameListener onStartGameListener) {
        lobbyStage = new LobbyStage(onStartGameListener);
        gameStage = new GameStage();
        currentStage = lobbyStage;
    }

    @Override
    public void render(float delta) {
        if (currentStage != null) {
            currentStage.act(delta);
            currentStage.draw();
        }
    }

    public void showGameStage() {
        currentStage = gameStage;
    }

    public IGameStage getGameStage() {
        return gameStage;
    }
}
