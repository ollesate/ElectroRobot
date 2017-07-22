package olof.sjoholm.GameWorld.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.Api.BoardAction;
import olof.sjoholm.GameLogic.PlayerController;
import olof.sjoholm.GameWorld.Actors.GameBoardActor.OnEndActionEvent;
import olof.sjoholm.GameWorld.Actors.GameBoardActor.OnStartActionEvent;
import olof.sjoholm.GameWorld.Maps.Map;
import olof.sjoholm.GameWorld.Maps.SpawnPoint;
import olof.sjoholm.Utils.Constants;
import olof.sjoholm.Utils.Logger;

import static com.badlogic.gdx.net.HttpRequestHeader.From;

public class GameBoard extends Group implements EventListener {
    private final List<GameBoardActor> movingActors = new ArrayList<GameBoardActor>();
    private int tileSize;
    private Map map;

    public GameBoard(int tileSize) {
        this.tileSize = tileSize;
        addListener(this);
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

    @Override
    public void act(float delta) {
        super.act(delta);
        for (GameBoardActor movingActor : movingActors) {
            // TODO: Fix really nice logic about pushing back actors if they, move over bounds.
            PlayerToken playerToken = (PlayerToken) movingActor;
            Vector2 dir = playerToken.getDirection();
            Vector2 pos = new Vector2(playerToken.getX(), playerToken.getY());

            if (Math.abs(dir.y) > 0) {
                boolean trespassing = Math.abs(pos.y % Constants.STEP_SIZE) > 0;
                if (trespassing) {
                    int fromTile;
                    if (dir.y > 0) {
                        if (pos.y > 0) {
                            fromTile = (int) (pos.y / Constants.STEP_SIZE);
                        } else {
                            fromTile = (int) (pos.y / Constants.STEP_SIZE) -1;
                        }
                    } else {
                        if (pos.y > 0) {
                            fromTile = (int) (pos.y / Constants.STEP_SIZE) + 1;
                        } else {
                            fromTile = (int) (pos.y / Constants.STEP_SIZE);
                        }
                    }
                    int toTile = dir.y > 0 ? fromTile + 1 : fromTile - 1;
                    if (!map.isWithinBounds(0, toTile)) {
                        playerToken.setY(fromTile * Constants.STEP_SIZE);
                        playerToken.setColor(Color.RED);
                    } else {
                        playerToken.setColor(Color.GREEN);
                    }
                } else {
                    playerToken.setColor(Color.WHITE);
                }
            } else if (Math.abs(dir.x) > 0) {
                boolean trespassing = Math.abs(pos.x % Constants.STEP_SIZE) > 0;
                if (trespassing) {
                    int fromTile;
                    if (dir.x > 0) {
                        if (pos.x > 0) {
                            fromTile = (int) (pos.x / Constants.STEP_SIZE);
                        } else {
                            fromTile = (int) (pos.x / Constants.STEP_SIZE) -1;
                        }
                    } else {
                        if (pos.x > 0) {
                            fromTile = (int) (pos.x / Constants.STEP_SIZE) + 1;
                        } else {
                            fromTile = (int) (pos.x / Constants.STEP_SIZE);
                        }
                    }
                    int toTile = dir.x > 0 ? fromTile + 1 : fromTile - 1;
                    if (!map.isWithinBounds(toTile, 0)) {
                        playerToken.setX(fromTile * Constants.STEP_SIZE);
                        playerToken.setColor(Color.RED);
                    } else {
                        playerToken.setColor(Color.GREEN);
                    }
                } else {
                    playerToken.setColor(Color.WHITE);
                }
            }
        }
    }

    private Point insideTile(PlayerToken playerToken) {
        Vector2 pos = new Vector2(playerToken.getX(), playerToken.getY());

        int tileY;
        if (pos.y > 0) {
            tileY = (int) (pos.y / Constants.STEP_SIZE);
        } else {
            tileY = (int) (pos.y / Constants.STEP_SIZE) -1;
        }

        int tileX;
        if (pos.x > 0) {
            tileX = (int) (pos.x / Constants.STEP_SIZE);
        } else {
            tileX = (int) (pos.x / Constants.STEP_SIZE) -1;
        }

        return new Point(tileX, tileY);
    }

    public List<SpawnPoint> getSpawnPoints() {
        return map.getSpawnPoints();
    }

    public void spawnToken(SpawnPoint spawnPoint, PlayerToken playerToken) {
        playerToken.setX(spawnPoint.x * Constants.STEP_SIZE);
        playerToken.setY(spawnPoint.y * Constants.STEP_SIZE);
        addActor(playerToken);
    }

    private ArrayDeque<PlayerAction> actionsQueue = new ArrayDeque<PlayerAction>();

    public static class PlayerAction {
        public final PlayerToken playerToken;
        public final BoardAction boardAction;

        public PlayerAction(PlayerToken playerToken, BoardAction boardAction) {
            this.playerToken = playerToken;
            this.boardAction = boardAction;
        }
    }

    private final Action playerFinishedTurn = new Action() {
        @Override
        public boolean act(float delta) {
            if (actionsQueue != null) {
                PlayerAction playerAction = actionsQueue.pop();
                PlayerToken playerToken = playerAction.playerToken;
                Action action = playerAction.boardAction.perform(playerToken);
                playerToken.addAction(Actions.sequence(action, playerFinishedTurn));
            }
            return true;
        }
    };

    public void performAction(PlayerToken playerToken, BoardAction boardAction) {
        if (actionsQueue.size() == 0) {
            Action action = boardAction.perform(playerToken);
            playerToken.addAction(Actions.sequence(action, playerFinishedTurn));
            boardAction.perform(playerToken);
        }
    }

    public void performActions(PlayerAction... playerActions) {
        SequenceAction sequence = new SequenceAction();
        for (PlayerAction action : playerActions) {
            sequence.addAction(new FireEventAction(new OnStartActionEvent(action.playerToken)));
            sequence.addAction(new ActionWrapper(action.playerToken, action.boardAction));
            sequence.addAction(new FireEventAction(new OnEndActionEvent(action.playerToken)));
        }
        addAction(sequence);
    }

    public static class FireEventAction extends Action {
        private final Event event;

        public FireEventAction(Event event) {
            this.event = event;
        }

        @Override
        public boolean act(float delta) {
            Actor target = getTarget();
            target.fire(event);
            return true;
        }
    }

    private class ActionWrapper extends Action {
        private final PlayerToken playerToken;
        private final BoardAction boardAction;
        private final Action perform;
        private boolean added;

        public ActionWrapper(PlayerToken playerToken, BoardAction boardAction) {
            // TODO: Rename target action or similiar
            this.playerToken = playerToken;
            this.boardAction = boardAction;
            perform = boardAction.perform(playerToken);
        }

        @Override
        public boolean act(float delta) {
            if (!added) {
                playerToken.addAction(perform);
                added = true;
            }
            return perform.act(delta);
        }
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

//            updateChildrenSizes();
//            updateActualSize();
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

    @Override
    public boolean handle(Event e) {
        Logger.d("Event " + e);
        if (e instanceof OnStartActionEvent) {
            // On start action
            OnStartActionEvent event = (OnStartActionEvent) e;
            movingActors.add(event.gameBoardActor);
        } else if (e instanceof OnEndActionEvent) {
            // On end action
            OnEndActionEvent event = (OnEndActionEvent) e;
            movingActors.remove(event.gameBoardActor);
        }
        return false;
    }
}
