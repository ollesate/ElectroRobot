package olof.sjoholm.GameWorld.Server.Screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.List;
import java.util.Locale;

import olof.sjoholm.GameLogic.GameManager;
import olof.sjoholm.GameWorld.Actors.GameBoard;
import olof.sjoholm.GameWorld.Game.PlayerController;
import olof.sjoholm.GameWorld.IGameStage;
import olof.sjoholm.GameWorld.Levels;
import olof.sjoholm.GameWorld.Server.PlayerApi;
import olof.sjoholm.GameWorld.Server.PlayerManager;
import olof.sjoholm.GameWorld.Utils.Constants;
import olof.sjoholm.GameWorld.Utils.ScreenAdapter;
import olof.sjoholm.Interfaces.IGameBoard;


public class GameStage extends Stage implements IGameStage {
    private GameBoard gameBoard;
    private final Viewport viewport;
    private CountDownText countDownText;

    public GameStage() {
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        init();
    }

    private void init() {
        setViewport(viewport);
        gameBoard = new GameBoard(this);
        gameBoard.loadMap(Levels.Level1());

        getRoot().setWidth(Constants.WORLD_WIDTH);
        getRoot().setHeight(Constants.WORLD_HEIGHT);

        countDownText = new CountDownText();

        Table table = new Table();
        table.top();
        table.setFillParent(true);
        table.add(gameBoard);
        table.row();
        table.addActor(countDownText);

        addActor(table);
    }

    @Override
    public IGameBoard getGameBoard() {
        return gameBoard;
    }

    @Override
    public void startCountdown(float timeSec) {
        countDownText.startCountDown(timeSec);
    }

    private static class CountDownText extends Table {
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

        public void startCountDown(float seconds) {
            countdown = seconds;
        }
    }
}
