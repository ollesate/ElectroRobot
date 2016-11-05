package olof.sjoholm.GameWorld.Server.Screens;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import olof.sjoholm.GameLogic.GameManager;
import olof.sjoholm.GameWorld.Actors.GameBoard;
import olof.sjoholm.GameWorld.Server.PlayerManager;
import olof.sjoholm.GameWorld.Utils.Constants;
import olof.sjoholm.GameWorld.Utils.ScreenAdapter;

/**
 * Created by sjoholm on 24/09/16.
 */

public class GameScreen extends ScreenAdapter {
    public static final Vector2[] handSites = new Vector2[]{
            new Vector2(50, 60),
            new Vector2(600, 60)
    };

    private Stage stage;
    private GameBoard gameBoard;

    public GameScreen(PlayerManager playerManager) {
        Viewport viewport = new FitViewport(
                Constants.WORLD_WIDTH * 0.75f,
                Constants.WORLD_HEIGHT * 0.75f
        );

        stage = new Stage(viewport);

        gameBoard = new GameBoard(stage);
        stage.addActor(gameBoard);
        new GameManager(gameBoard, playerManager);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
