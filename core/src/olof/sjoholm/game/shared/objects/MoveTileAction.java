package olof.sjoholm.game.shared.objects;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

import java.awt.Point;

import olof.sjoholm.configuration.Constants;
import olof.sjoholm.game.server.logic.Direction;

public class MoveTileAction extends Action {
    private final PlayerToken token;
    private final Direction direction;
    private final boolean animate;
    private final float duration;

    private Action action;

    public MoveTileAction(PlayerToken actor, Direction direction, boolean animate, float duration) {
        this.token = actor;
        this.direction = direction;
        this.animate = animate;
        this.duration = duration;
    }

    @Override
    public boolean act(float delta) {
        if (action == null) {
            action = getInternalAction();
            action.setActor(token);
        }
        if (action.act(delta)) {
            return true;
        }
        return false;
    }

    @Override
    public void restart() {
        action = null;
    }

    @Override
    public void reset() {
        action = null;
    }

    private Action getInternalAction() {
        if (token.getStage() == null) { // Already died
            return Actions.delay(0f);
        }

        if (animate) {
            return Actions.parallel(new MoveAction(), new AnimateAction());
        }

        return new MoveAction();
    }

    private class MoveAction extends Action {
        private Action action;

        @Override
        public boolean act(float delta) {
            if (action == null) {
                action = getInternalAction();
                action.setActor(token);
            }
            return action.act(delta);
        }

        private Action getInternalAction() {
            Point offset = direction.getPoint();
            Point nextPos = token.getGameBoardPosition(offset);

            boolean walkablePath = token.getStage().getGameBoard().isPassableTerrain(nextPos.x, nextPos.y);
            if (!walkablePath) {
                return Actions.delay(duration + 1f);
            }

            boolean isPit = token.getStage().getGameBoard().isPit(nextPos.x, nextPos.y);
            Action move = getMovementAction();
            Action wait = Actions.delay(1f);
            if (isPit) {
                return Actions.sequence(move, getFallToDeathAction());
            }
            return Actions.sequence(move, wait);
        }

        private Action getMovementAction() {
            MoveByAction moveByAction = new MoveByAction() {

                @Override
                protected void end() {
                    super.end();
                    token.setX(Math.round(token.getX()));
                    token.setY(Math.round(token.getY()));
                }
            };
            moveByAction.setAmountX(direction.dirX * Constants.STEP_SIZE);
            moveByAction.setAmountY(direction.dirY * Constants.STEP_SIZE);
            moveByAction.setDuration(duration);
            moveByAction.setTarget(token);
            return moveByAction;
        }

        private Action getFallToDeathAction() {
            return Actions.sequence(Actions.scaleTo(0, 0, 1f), Actions.removeActor());
        }
    }

    private class AnimateAction extends TemporalAction {

        public AnimateAction() {
            setDuration(duration);
        }

        @Override
        protected void update(float percent) {}

        @Override
        protected void begin() {
            token.startAnimation();
        }

        @Override
        protected void end() {
            token.stopAnimation();
        }
    }
}
