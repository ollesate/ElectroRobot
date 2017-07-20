package olof.sjoholm.GameWorld.Actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.List;

import olof.sjoholm.GameLogic.PlayerController;
import olof.sjoholm.GameWorld.Maps;
import olof.sjoholm.GameWorld.Maps.Map;
import olof.sjoholm.GameWorld.Maps.SpawnPoint;
import olof.sjoholm.Utils.Constants;
import olof.sjoholm.Utils.Logger;

public class GameBoard extends Group {
    private int tileSize;
    private Map map;

    public GameBoard(int tileSize) {
        this.tileSize = tileSize;
    }

    public void loadMap(Map map) {
        this.map = map;
        clearChildren(); // TODO: why?

        // TODO: can remove?
        // Make sure to set up level the first thing we do to
        // Level.setUpWorldSize(level);
        // setWidth(Constants.STEP_SIZE * level.getWidth());
        // setHeight(Constants.STEP_SIZE * level.getHeight());
        map.create(this, tileSize);
    }

    public List<SpawnPoint> getSpawnPoints() {
        return map.getSpawnPoints();
    }

    public void spawnToken(SpawnPoint spawnPoint, PlayerToken playerToken) {
        playerToken.setBoardX(spawnPoint.x);
        playerToken.setBoardY(spawnPoint.y);
        addActor(playerToken);
    }

    @Override
    protected void sizeChanged() {
        float width = getWidth();
        float height = getHeight();

        if (map != null) {
            float maxWidth = width / map.getWidth();
            float maxHeight = height / map.getHeight();

            float min = Math.min(maxWidth, maxHeight);

            tileSize = (int) min;
            Logger.d("New size: " + width + ", " + height + ". Tilesize is now set to " + tileSize);

            updateChildrenSizes();
            updateActualSize();
        }
    }

    private void updateActualSize() {
        setWidth(tileSize * map.getWidth());
        setHeight(tileSize * map.getHeight());
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

    @Deprecated
    public void spawnToken(PlayerController owner) {
//        PlayerToken playerToken = new PlayerToken(this);
//        if (playerTokens.size() == 0) {
//            playerToken.setBoardX(0);
//            playerToken.setBoardY(0);
//        } else {
//            playerToken.setBoardX(2);
//            playerToken.setBoardY(0);
//        }
//
//        playerTokens.put(owner, playerToken);
//        addActor(playerToken);
    }

    @Deprecated
    public int isTileAvailable(int x, int y) {
//        if (mapHandler.isWithinBounds(x, y)) {
//            return -1;
//        }
//        if (isOccupiedByPlayer(x, y)) {
//            return -1;
//        }
//        return mapHandler.get(x, y);
        return 1;
    }

    @Deprecated
    private boolean isOccupiedByPlayer(int x, int y) {
//        for (MovableToken token : playerTokens.values()) {
//            if (token.getBoardX() == x && token.getBoardY() == y) {
//                return true;
//            }
//        }
        return false;
    }

    @Deprecated
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
