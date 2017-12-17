package olof.sjoholm.game.shared.objects;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.awt.Point;
import java.util.List;

import olof.sjoholm.game.server.logic.Direction;
import olof.sjoholm.game.server.objects.GameBoard;

public class PushMoveAction extends Action {
    private final PlayerToken token;
    private final Direction direction;
    private final boolean animate;
    private final float duration;

    private Action action;

    public PushMoveAction(PlayerToken token, Direction direction, boolean animate, float duration) {
        this.token = token;
        this.direction = direction;
        this.animate = animate;
        this.duration = duration;
    }

    @Override
    public boolean act(float delta) {
        System.out.println("Push move act");
        if (action == null) {
            action = getInternalAction();
            action.setActor(token);
        }
        boolean finished = action.act(delta);
        if (finished) {
            System.out.println("Push move finished");
        }
        return finished;
    }

    @Override
    public void reset() {
        action = null;
    }

    @Override
    public void restart() {
        action = null;
    }

    private Action getInternalAction() {
        if (token.getStage() == null) {
            System.out.println("Token is dead");
            return Actions.delay(0f);
        }

        Point offset = direction.getPoint();
        Point nextPos = token.getGameBoardPosition(offset);
        Point nextNextPos = new Point(nextPos.x + offset.x, nextPos.y + offset.y);
        GameBoard gameBoard = token.getStage().getGameBoard();
        List<PlayerToken> actors = gameBoard.getActorsAt(nextPos.x, nextPos.y, PlayerToken.class);
        if (actors.isEmpty()) {
            System.out.println("No actors here: move");
            return new MoveTileAction(token, direction, animate, duration);
        } else if (gameBoard.isAvailableSpace(nextNextPos.x, nextNextPos.y)) {
            System.out.println("Move other player");
            PlayerToken other = actors.get(0);
            return Actions.parallel(
                    new MoveTileAction(token, direction, animate, duration),
                    new MoveTileAction(other, direction, false, duration)
            );
        } else {
            // We can't move pawn into un available space!
            System.out.println("We cant move 2 pawns!");
            return token.getFakeMoveAction(duration);
        }
    }
}
