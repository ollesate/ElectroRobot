package olof.sjoholm.Views;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import olof.sjoholm.Client.CardViewParser;
import olof.sjoholm.Client.SelectableCard;
import olof.sjoholm.GameWorld.Actors.GameBoard;
import olof.sjoholm.GameWorld.Actors.CardView;
import olof.sjoholm.Interfaces.IGameStage;
import olof.sjoholm.GameWorld.Levels;
import olof.sjoholm.Utils.Constants;
import olof.sjoholm.Utils.Logger;
import olof.sjoholm.Interfaces.IGameBoard;
import olof.sjoholm.Interfaces.IPlayerHands;
import olof.sjoholm.Models.CardModel;


public class GameStage extends Stage implements IGameStage, IPlayerHands {
    private GameBoard gameBoard;
    private final Viewport viewport;
    private CountDownText countDownText;
    private PlayerHand playerHand1;
    private PlayerHand playerHand2;
    private Map<Integer, PlayerHand> playerHands = new HashMap<Integer, PlayerHand>();

    public GameStage() {
        Camera camera = new OrthographicCamera(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        camera.position.set(Constants.WORLD_WIDTH / 2, Constants.WORLD_HEIGHT / 2, 0);
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        init();
    }

    private void init() {
        setViewport(viewport);
        gameBoard = new GameBoard();
        gameBoard.loadMap(Levels.Level1());

        getRoot().setWidth(Constants.WORLD_WIDTH);
        getRoot().setHeight(Constants.WORLD_HEIGHT);

        countDownText = new CountDownText();

        initPlayerHands();

        Table table = new Table();
        table.setFillParent(true);
        table.top();
        table.row();
        table.add(getUpperTable()).fillX();
        table.row();
        table.add(gameBoard).grow().center();
        table.addActor(countDownText);

        addActor(table);
    }

    private void initPlayerHands() {
        playerHand1 = new PlayerHand();
        playerHand2 = new PlayerHand();
        playerHands.put(0, playerHand1);
        playerHands.put(1, playerHand2);
    }

    private Table getUpperTable() {
        Table table = new Table();
        table.center();
        table.left();
        table.add(playerHand1).expandX().padBottom(10f).padTop(10f).bottom();
        table.add().expandX();
        table.add(playerHand2).expandX().padBottom(10f).padTop(10f).bottom();
        return table;
    }

    @Override
    public IGameBoard getGameBoard() {
        return gameBoard;
    }

    @Override
    public IPlayerHands getPlayerHands() {
        return this;
    }

    @Override
    public void startCountdown(float timeSec) {
        countDownText.startCountDown(timeSec);
    }

    @Override
    public void setCards(int playerId, List<CardModel> cards) {
        playerHands.get(playerId).setCards(cards);
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

    private static class PlayerHand extends Table {
        private List<SelectableCard> cards = new ArrayList<SelectableCard>();
        private static final float height = 125f;

        public PlayerHand() {
            left();
            top();
        }

        public void setCards(final List<CardModel> cardModels) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    for (CardModel cardModel : cardModels) {
                        Logger.d("Card " + cardModel.getClass().getName());
                        CardView cardView = CardViewParser.getInstance().modelToView(cardModel);
                        SelectableCard selectableCard = new SelectableCard(cardView);
                        cards.add(selectableCard);
                        cardView.setPadding(0f);
                        cardView.setMinSize(70f, height, 1f);
                        add(cardView).space(10f).top();
                    }
                }
            });
        }

        public void select(int i) {

        }

        @Override
        public float getHeight() {
            return height;
        }

        @Override
        public float getPrefHeight() {
            return height;
        }

    }
}