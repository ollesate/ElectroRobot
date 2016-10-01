package olof.sjoholm.GameWorld.GameManagers;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import olof.sjoholm.GameLogic.GameManager;
import olof.sjoholm.GameWorld.Utils.Constants;

/**
 * Created by sjoholm on 24/09/16.
 */

public class GameScreen {
    public static final Vector2[] handSites = new Vector2[]{
            new Vector2(50, 60),
            new Vector2(600, 60)
    };

    private Stage stage;
    private olof.sjoholm.GameWorld.Actors.GameBoard gameBoard;

    public GameScreen() {
        Viewport viewport = new FitViewport(Constants.WORLD_WIDTH * 0.75f,
                Constants.WORLD_HEIGHT * 0.75f);
        stage = new Stage(viewport);
        gameBoard = new olof.sjoholm.GameWorld.Actors.GameBoard(stage);
        stage.addActor(gameBoard);
        new GameManager(gameBoard);
    }

    public void draw() {
        stage.draw();
    }

    public void update(float delta) {
        stage.act(delta);
    }

}
