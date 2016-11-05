package olof.sjoholm.GameWorld.Actors;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.HashMap;
import java.util.Map;

import olof.sjoholm.GameWorld.Server.Player;
import olof.sjoholm.GameWorld.Utils.Constants;
import olof.sjoholm.GameWorld.Utils.Direction;
import olof.sjoholm.Interfaces.IGameBoard;
import olof.sjoholm.Interfaces.MovableToken;

/**
 * Created by sjoholm on 01/10/16.
 */
public class GameBoard extends Group implements IGameBoard {
    public static final int size = 30;
    private Stage stage;
    private Map<Player, MovableToken> playerTokens;

    private int[][] map = new int[][]{
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
    };

    public GameBoard(Stage stage) {
        reset();
        this.stage = stage;
        playerTokens = new HashMap<Player, MovableToken>();
        setPosition(200, 50);

        if (stage != null) {
            for (int i = 0; i < getMapWidth(); i++) {
                for (int j = 0; j < getMapHeight(); j++) {
                    addActor(new Tile(i, j));
                }
            }
        }
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
    public void spawnToken(Player owner) {
        PlayerToken playerToken = new PlayerToken(this);
        if (playerTokens.size() == 0) {
            playerToken.setBoardX(0);
            playerToken.setBoardY(0);
        } else {
            playerToken.setBoardX(2);
            playerToken.setBoardY(0);
        }

        playerTokens.put(owner, playerToken);
        addActor(playerToken);
    }

    @Override
    public MovableToken getToken(Player owner) {
        return playerTokens.get(owner);
    }

    @Override
    public int isTileAvailable(int x, int y) {
        if (outOfBorder(x, y)) {
            return -1;
        }
        if (playerInTile(x, y)) {
            return -1;
        }
        return map[y][x];
    }

    private boolean playerInTile(int x, int y) {
        for (MovableToken token : playerTokens.values()) {
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
        while (isTileAvailable(x, y) != Constants.IMMOBILE &&
                isTileAvailable(x, y) != Constants.OUT_OF_BOUNDS) {
            x += direction.x;
            y += direction.y;
            actualSteps++;
        }

        return actualSteps;
    }
}
