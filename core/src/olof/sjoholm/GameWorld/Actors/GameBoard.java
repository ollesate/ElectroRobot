package olof.sjoholm.GameWorld.Actors;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.HashMap;
import java.util.Map;

import olof.sjoholm.GameWorld.Game.PlayerController;
import olof.sjoholm.GameWorld.Level;
import olof.sjoholm.GameWorld.Server.Player;
import olof.sjoholm.GameWorld.Utils.Constants;
import olof.sjoholm.GameWorld.Utils.Direction;
import olof.sjoholm.GameWorld.Utils.Logger;
import olof.sjoholm.Interfaces.IGameBoard;
import olof.sjoholm.Interfaces.MovableToken;

public class GameBoard extends Group implements IGameBoard {
    public static final int size = 30;
    private Stage stage;
    private Map<PlayerController, MovableToken> playerTokens;
    private Level level;

    public GameBoard(Stage stage) {
        this.stage = stage;
        playerTokens = new HashMap<PlayerController, MovableToken>();
    }

    public void loadMap(Level level) {
        this.level = level;
        reset();

        // Make sure to set up level the first thing we do to
        Level.setUpWorldSize(level);
        setWidth(Constants.STEP_SIZE * level.getWidth());
        setHeight(Constants.STEP_SIZE * level.getHeight());

        if (stage != null) {
            for (int i = 0; i < level.getWidth(); i++) {
                for (int j = 0; j < level.getHeight(); j++) {
                    addActor(new Tile(i, j));
                }
            }
        } else {
            Logger.d("Could not creat map, stage is null");
        }
    }

    private void reset() {
        clearChildren();
    }

    @Override
    public void spawnToken(PlayerController owner) {
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
    public MovableToken getToken(PlayerController owner) {
        return playerTokens.get(owner);
    }

    @Override
    public int isTileAvailable(int x, int y) {
        if (level.isOutsideLevel(x, y)) {
            return -1;
        }
        if (isOccupiedByPlayer(x, y)) {
            return -1;
        }
        return level.get(x, y);
    }

    private boolean isOccupiedByPlayer(int x, int y) {
        for (MovableToken token : playerTokens.values()) {
            if (token.getBoardX() == x && token.getBoardY() == y) {
                return true;
            }
        }
        return false;
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
