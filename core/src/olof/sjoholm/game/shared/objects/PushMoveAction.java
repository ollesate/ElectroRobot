package olof.sjoholm.game.shared.objects;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.awt.Point;
import java.util.List;

import olof.sjoholm.game.server.logic.Direction;
import olof.sjoholm.game.server.objects.GameBoard;
import olof.sjoholm.game.shared.logic.Movement;

public class PushMoveAction extends Action {
    private final PlayerToken token;
    private final Movement movement;
    private final float duration;

    private Action action;

    public PushMoveAction(PlayerToken token, Movement movement, float duration) {
        this.token = token;
        this.movement = movement;
        this.duration = duration;
    }

    @Override
    public boolean act(float delta) {
        if (action == null) {
            action = getInternalAction();
            action.setActor(token);
        }
        return action.act(delta);
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

        Direction direction = Direction.fromRotation(token.getRotation()).translate(movement);
        Point offset = direction.getPoint();
        Point nextPos = token.getGameBoardPosition(offset);
        GameBoard gameBoard = token.getStage().getGameBoard();
        List<PlayerToken> actors = gameBoard.getActorsAt(nextPos.x, nextPos.y, PlayerToken.class);

        PlayerToken other;
        if (actors.isEmpty()) {
            System.out.println("No actors here: move");
            return new MoveTileAction(token, direction, true, duration);
        } else if ((other = actors.get(0)).canMove(direction)) {
            System.out.println("Move other player");
            return Actions.parallel(
                    new MoveTileAction(token, direction, true, duration),
                    new MoveTileAction(other, direction, false, duration)
            );
        } else {
            // We can't move pawn into un available space!
            System.out.println("We cant move 2 pawns!");
            return token.getFakeMoveAction(duration);
        }
    }
}
