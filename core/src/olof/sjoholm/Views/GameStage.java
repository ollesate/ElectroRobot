package olof.sjoholm.Views;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Collections;
import java.util.List;

import olof.sjoholm.GameWorld.Actors.GameBoard;
import olof.sjoholm.GameWorld.Actors.GameBoardActor;
import olof.sjoholm.GameWorld.Maps;
import olof.sjoholm.Utils.Constants;


public class GameStage extends Stage {
    private GameBoard gameBoard;

    public GameStage(GameBoard gameBoard) {
        this.gameBoard = gameBoard;

        Camera camera = new OrthographicCamera(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        camera.position.set(Constants.WORLD_WIDTH / 2, Constants.WORLD_HEIGHT / 2, 0);
        Viewport viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        setViewport(viewport);
        getRoot().setWidth(Constants.WORLD_WIDTH);
        getRoot().setHeight(Constants.WORLD_HEIGHT);
    }

    public List<GameBoardActor> getActorsAt(int x, int y) {
        return gameBoard.getActorsAt(x, y);
    }

    public boolean isWithinBounds(int x, int y) {
        return gameBoard.getMap().isWithinBounds(x, y);
    }
}
