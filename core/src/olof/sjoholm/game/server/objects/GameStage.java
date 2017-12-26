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

        OrthographicCamera camera = new OrthographicCamera(0, 0);
        camera.position.set(700, 380, 0);
        Viewport viewport = new ScalingViewport(Scaling.none, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        setViewport(viewport);
    }

    public List<GameBoardActor> getActors(float x, float y, float width, float height) {
        return gameBoard.getActors(x, y, width, height);
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }
}
