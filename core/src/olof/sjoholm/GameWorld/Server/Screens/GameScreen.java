package olof.sjoholm.GameWorld.Server.Screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Locale;

import olof.sjoholm.GameLogic.GameManager;
import olof.sjoholm.GameWorld.Actors.GameBoard;
import olof.sjoholm.GameWorld.Levels;
import olof.sjoholm.GameWorld.Server.PlayerManager;
import olof.sjoholm.GameWorld.Utils.Constants;
import olof.sjoholm.GameWorld.Utils.ScreenAdapter;

/**
 * Created by sjoholm on 24/09/16.
 */

public class GameScreen extends ScreenAdapter {
    private Stage stage;
    private GameBoard gameBoard;
    private PlayerManager playerManager;
    private final Viewport viewport;
    private Table table;

    public GameScreen(PlayerManager playerManager) {
        this.playerManager = playerManager;
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
    }

    @Override
    public void show() {
        stage = new Stage(viewport);
        gameBoard = new GameBoard(stage);
        gameBoard.loadMap(Levels.Level1());

        stage.getRoot().setWidth(Constants.WORLD_WIDTH);
        stage.getRoot().setHeight(Constants.WORLD_HEIGHT);

        CountDownText countDownText = new CountDownText();

        table = new Table();
        table.top();
        table.setFillParent(true);
        table.add(gameBoard);
        table.row();
        table.addActor(countDownText);

        stage.addActor(table);

        new GameManager(gameBoard, playerManager, countDownText);
    }

    private static class CountDownText extends Table implements CountDownManager {
        private final TextField textField;
        private float countdown;
        private boolean isCounting;

        public CountDownText() {
            setWidth(Gdx.graphics.getWidth());
            setHeight(Gdx.graphics.getHeight());

            textField = new TextField("", new Skin(Gdx.files.internal("skin/uiskin.json")));
            setFillParent(true);
            top();
            add(textField).width(Gdx.graphics.getWidth() * 0.75f).center();
        }

        @Override
        public void act(float delta) {
            super.act(delta);

            textField.setVisible(countdown > 0);

            if (countdown > 0) {
                countdown -= delta;
                int seconds = (int) countdown;
                textField.setText(
                        String.format(Locale.ENGLISH, "Round starts in %s seconds.", seconds)
                );
            }
        }

        @Override
        public void startCountDown(long millis) {
            countdown = millis / 1000;
        }
    }

    public interface CountDownManager {

        void startCountDown(long millis);
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
