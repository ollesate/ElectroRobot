package olof.sjoholm.GameWorld.GameManagers;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

import olof.sjoholm.GameLogic.GameManager;
import olof.sjoholm.GameLogic.ICardHand;
import olof.sjoholm.GameLogic.IGameBoard;
import olof.sjoholm.GameWorld.Actors.CardHand;
import olof.sjoholm.GameWorld.Actors.MovableToken;
import olof.sjoholm.GameWorld.PlayerToken;

/**
 * Created by sjoholm on 24/09/16.
 */

public class GameScreen implements IGameBoard {
    private static final float tokenSpawnOffset = 100f;
    private static final Vector2[] spawnSites = new Vector2[]{
        new Vector2(100, 50f),
        new Vector2(700, 50f)
    };

    private Stage stage;

    public GameScreen() {
        stage = new Stage();
        new GameManager(this);
    }

    public void draw() {
        stage.draw();
    }

    public void update(float delta) {
        stage.act(delta);
    }

    @Override
    public MovableToken spawnToken(int site) {
        PlayerToken playerToken = new PlayerToken();
        playerToken.setPosition(new Vector2(spawnSites[site]).add(0, tokenSpawnOffset));
        stage.addActor(playerToken);
        return playerToken;
    }

    @Override
    public ICardHand spawnCardHand(int site) {
        CardHand cardHand = new CardHand();
        cardHand.setPosition(spawnSites[site].x, spawnSites[site].y);
        stage.addActor(cardHand);
        return cardHand;
    }
}
