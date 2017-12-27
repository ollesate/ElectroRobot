package olof.sjoholm.game.server.objects;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.List;

import olof.sjoholm.configuration.Constants;

public class GameStage extends Stage {
    private GameBoard gameBoard;

    public GameStage(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        int width = Constants.WORLD_WIDTH;
        int height = Constants.WORLD_HEIGHT;
        OrthographicCamera camera = new OrthographicCamera(0, 0);
        Viewport viewport = new ScalingViewport(Scaling.none, width, height, camera);
        camera.position.set(width / 2, height / 2, 0);
        setViewport(viewport);
    }

    public void setSize(int width, int height) {
        getViewport().setWorldSize(width, height);
        getViewport().getCamera().position.set(width / 2, height / 2, 0);
    }

    public List<GameBoardActor> getActors(float x, float y, float width, float height) {
        return gameBoard.getActors(x, y, width, height);
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }
}
