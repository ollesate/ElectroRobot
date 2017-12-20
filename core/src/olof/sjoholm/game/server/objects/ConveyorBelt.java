package olof.sjoholm.game.server.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.utils.Align;

import java.awt.Point;
import java.util.List;

import olof.sjoholm.assets.Textures;
import olof.sjoholm.game.server.logic.Direction;
import olof.sjoholm.game.shared.logic.Rotation;
import olof.sjoholm.game.shared.objects.PlayerToken;
import olof.sjoholm.utils.PointUtils;
import olof.sjoholm.utils.ui.TextureDrawable;

public class ConveyorBelt extends GameBoardActor {
    private static final float DURATION = 1f;
    private final Direction direction;
    private final Rotation rotation;
    private final int times;

    public ConveyorBelt(Direction direction, int times) {
        this(direction, null, times);
    }

    public ConveyorBelt(Direction direction, Rotation rotation, int times) {
        this.direction = direction;
        this.rotation = rotation;
        this.times = times;
        setColor(Color.BROWN);
        initTexture();
        initRotation();
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        setOrigin(Align.center);
    }

    private void initTexture() {
        Texture texture;
        if (rotation == null) { // Not curved
            texture = Textures.CONVEYOR_BELT_UP;
        } else {
            switch (rotation) {
                case LEFT:
                    texture = Textures.CONVEYOR_BELT_CURVE_LEFT;
                    break;
                case RIGHT:
                    texture = Textures.CONVEYOR_BELT_CURVE_RIGHT;
                    break;
                default:
                    throw new IllegalStateException("Oops");
            }

        }
        setDrawable(new TextureDrawable(texture));
    }

    private void initRotation() {
        float rotationDegrees = direction.rotation - (rotation == null ? 90f : 0);
        setRotation(rotationDegrees);
    }

    public Action getAction() {
        return new BeltAction();
    }

    private final class BeltAction extends Action {
        private int currentTime;
        private Action beltAction;

        public BeltAction() {
            currentTime = times;
        }

        @Override
        public boolean act(float delta) {
            if (currentTime == 0) {
                return true;
            }

            if (beltAction == null) {
                beltAction = getInternalBeltAction();
            }

            if (beltAction.act(delta)) {
                beltAction = null;
                currentTime--;
                if (currentTime == 0) {
                    // We are finished
                    return true;
                }
            }
            return false;
        }

        private Action getInternalBeltAction() {
            GameBoard gameBoard = getStage().getGameBoard();
            Point boardPosition = gameBoard.getBoardPosition(ConveyorBelt.this);
            List<PlayerToken> tokens = gameBoard.getActorsAt(boardPosition.x, boardPosition.y,
                    PlayerToken.class);

            if (tokens.size() > 0) {
                System.out.println("Move player " + getGameboardPosition());
                PlayerToken playerToken = tokens.get(0);

                Point nextPos = PointUtils.add(boardPosition, direction.getPoint());

                Action move = playerToken.getMoveTileAction(direction, DURATION, false);
                List<ConveyorBelt> belts = gameBoard.getActorsAt(nextPos, ConveyorBelt.class);
                if (belts.size() > 0 && belts.get(0).rotation != null) {
                    System.out.println("Move and rotate");
                    Rotation rotation = belts.get(0).rotation;
                    ParallelAction parallel = Actions.parallel(move, playerToken.rotate(rotation));
                    parallel.setActor(playerToken);
                    return parallel;
                }

                return move;
            }
            return Actions.delay(DURATION);
        }
    }
}
