package olof.sjoholm.GameWorld.Actors;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.GameWorld.Server.Screens.GameScreen;
import olof.sjoholm.GameWorld.Utils.Constants;
import olof.sjoholm.GameWorld.Utils.Direction;
import olof.sjoholm.Interfaces.ICardHand;
import olof.sjoholm.Interfaces.IGameBoard;
import olof.sjoholm.Interfaces.MovableToken;

/**
 * Created by sjoholm on 01/10/16.
 */
public class GameBoard extends Group implements IGameBoard {
    public static final int size = 30;
    private List<PlayerToken> tokens;
    private Stage stage;

    private int[][] map = new int[][]{
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
    };

    public GameBoard(Stage stage) {
        reset();
        this.stage = stage;
        tokens = new ArrayList<PlayerToken>();
        setPosition(200, 50);

        if (stage != null) {
            for (int i = 0; i < getMapWidth(); i++) {
                for (int j = 0; j < getMapHeight(); j++) {
                    addActor(new Tile(i, j));
                }
            }
        }
    }

    public void addPlayerToken(PlayerToken playerToken) {
        tokens.add(playerToken);
        addActor(playerToken);
    }


    public int getMapWidth() {
        return map[0].length;
    }

    public int getMapHeight() {
        return map.length;
    }

    private void reset() {
        clearChildren();
    }

    @Override
    public MovableToken spawnToken(int site) {
        PlayerToken playerToken = new PlayerToken(this);
        if (site == 0) {
            playerToken.setBoardX(0);
            playerToken.setBoardY(0);
        } else {
            playerToken.setBoardX(2);
            playerToken.setBoardY(0);
        }

        addPlayerToken(playerToken);
        return playerToken;
    }

    @Override
    public ICardHand spawnCardHand(int site) {
        CardHand cardHand = new CardHand();
        cardHand.setPosition(GameScreen.handSites[site].x, GameScreen.handSites[site].y);
        stage.addActor(cardHand);
        return cardHand;
    }

    @Override
    public int getMapTile(int x, int y) {
        if (outOfBorder(x, y)) {
            return -1;
        }
        if (playerInTile(x, y)) {
            return -1;
        }
        return map[y][x];
    }

    private boolean playerInTile(int x, int y) {
        for (MovableToken token : tokens) {
            if (token.getBoardX() == x && token.getBoardY() == y) {
                return true;
            }
        }
        return false;
    }

    private boolean outOfBorder(int x, int y) {
        return x < 0 ||
                x >= getMapWidth() ||
                y < 0 ||
                y >= getMapHeight();
    }

    public void setMap(int[][] map) {
        this.map = map;
    }

    public int getPossibleSteps(Direction direction, int x, int y) {
        int actualSteps = 0;

        x += direction.x;
        y += direction.y;
        while (getMapTile(x, y) != Constants.IMMOBILE &&
                getMapTile(x, y) != Constants.OUT_OF_BOUNDS) {
            x += direction.x;
            y += direction.y;
            actualSteps++;
        }

        return actualSteps;
    }
}
