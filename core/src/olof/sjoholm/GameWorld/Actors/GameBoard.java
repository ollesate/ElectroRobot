package olof.sjoholm.GameWorld.Actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.HashMap;
import java.util.Map;

import olof.sjoholm.GameLogic.PlayerController;
import olof.sjoholm.GameWorld.Level;
import olof.sjoholm.Utils.Constants;
import olof.sjoholm.Utils.Logger;
import olof.sjoholm.Interfaces.IGameBoard;
import olof.sjoholm.Interfaces.MovableToken;

public class GameBoard extends Group implements IGameBoard {
    private int tileSize;
    private Map<PlayerController, MovableToken> playerTokens;
    private Level level;

    public GameBoard() {
        tileSize = (int) Constants.STEP_SIZE;
        playerTokens = new HashMap<PlayerController, MovableToken>();
    }

    public void loadMap(Level level) {
        this.level = level;
        reset();

        // Make sure to set up level the first thing we do to
        // Level.setUpWorldSize(level);
        // setWidth(Constants.STEP_SIZE * level.getWidth());
        // setHeight(Constants.STEP_SIZE * level.getHeight());

        for (int i = 0; i < level.getWidth(); i++) {
            for (int j = 0; j < level.getHeight(); j++) {
                addGameBoardActor(new Tile(i, j));
            }
        }
    }

    @Override
    protected void sizeChanged() {
        float width = getWidth();
        float height = getHeight();

        if (level != null) {
            float maxWidth = width / level.getWidth();
            float maxHeight = height / level.getHeight();

            float min = Math.min(maxWidth, maxHeight);

            tileSize = (int) min;
            Logger.d("New size: " + width + ", " + height + ". Tilesize is now set to " + tileSize);

            updateChildrenSizes();
            updateActualSize();
        }
    }

    private void updateActualSize() {
        setWidth(tileSize * level.getWidth());
        setHeight(tileSize * level.getHeight());
        Group parent = getParent();
        if (parent instanceof Table) ((Table) parent).invalidate();
    }

    private void updateChildrenSizes() {
        for (Actor actor : getChildren()) {
            if (actor instanceof GameBoardActor) {
                GameBoardActor gameBoardActor = ((GameBoardActor) actor);
                gameBoardActor.updateStepSize(tileSize);
            }
        }
    }

    private void addGameBoardActor(GameBoardActor actor) {
        actor.setWidth(tileSize);
        actor.setHeight(tileSize);
        actor.updateStepSize(tileSize);
        addActor(actor);
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

    public int getPossibleSteps(Vector2 direction, int x, int y) {
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
