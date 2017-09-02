package olof.sjoholm.game.server.objects;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.List;

import olof.sjoholm.game.server.logic.TileType;
import olof.sjoholm.configuration.Constants;
import olof.sjoholm.sfx.Explosion;


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

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                addActor(Explosion.create(x, y));
                return true;
            }
        });
    }

    public List<GameBoardActor> getActorsAt(int x, int y) {
        return gameBoard.getActorsAt(x, y);
    }

    public List<GameBoardActor> getActors(float x, float y, float width, float height) {
        return gameBoard.getActors(x, y, width, height);
    }

    public boolean isWithinBounds(int x, int y) {
        return gameBoard.getLevel().isWithinBounds(x, y);
    }

    public TileType getTile(int x, int y) {
        return gameBoard.getLevel().getTile(x, y);
    }
}
