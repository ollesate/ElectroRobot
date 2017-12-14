package olof.sjoholm.game.server.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.awt.Point;
import java.util.List;

import olof.sjoholm.assets.Textures;
import olof.sjoholm.game.server.logic.Direction;
import olof.sjoholm.game.shared.objects.PlayerToken;
import olof.sjoholm.utils.ui.TextureDrawable;

public class ConveyorBelt extends GameBoardActor {
    private static final float DURATION = 1f;
    private final Direction direction;
    private final int times;

    public ConveyorBelt(Direction direction, int times) {
        this.direction = direction;
        this.times = times;
        setDrawable(new TextureDrawable(Textures.BACKGROUND));
        setColor(Color.BROWN);
    }

    @Override
    public void onAddedToStage() {

    }

    public Action getAction() {
        System.out.println("Get action");
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
            if (beltAction == null) {
                beltAction = getInternalBeltAction();
            }

            if (beltAction.act(delta)) {
                currentTime--;
                if (currentTime == 0) {
                    // We are finished
                    return true;
                } else {
                    beltAction = getInternalBeltAction();
                }
            }
            return false;
        }

        private Action getInternalBeltAction() {
            GameBoard gameBoard = getStage().getGameBoard();
            Point boardPosition = gameBoard.getBoardPosition(ConveyorBelt.this);
            System.out.println("- Find tokens at " + boardPosition.x + ", " + boardPosition.y);
            List<PlayerToken> tokens = gameBoard.getActorsAt(boardPosition.x, boardPosition.y,
                    PlayerToken.class);

            if (tokens.size() > 0) {
                PlayerToken playerToken = tokens.get(0);
                System.out.println("---> Move token " + playerToken);
                System.out.println("------------");
                return playerToken.getMoveAction(direction, DURATION);
            }
            System.out.println("No one on it");
            System.out.println("------------");
            return Actions.delay(DURATION);
        }
    }
}
