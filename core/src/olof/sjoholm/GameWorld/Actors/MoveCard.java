package olof.sjoholm.GameWorld.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import olof.sjoholm.GameWorld.Utils.CardUtil;
import olof.sjoholm.GameWorld.Utils.Direction;
import olof.sjoholm.Interfaces.Callback;
import olof.sjoholm.Interfaces.ICard;
import olof.sjoholm.Interfaces.MovableToken;

/**
 * Created by sjoholm on 24/09/16.
 */
public class MoveCard extends BaseCard implements ICard {
    private MovableToken movableToken;
    private int steps;
    private Direction direction;

    public MoveCard(MovableToken movableToken) {
        this.movableToken = movableToken;
        steps = (int)(Math.random() * 4 + 1);
        direction = Direction.random();
        addActors();
    }

    @Override
    public void playCard(final Callback callback) {
        select();
        movableToken.move(steps, direction, new Callback() {
            @Override
            public void callback() {
                unselect();
                callback.callback();
            }
        });
    }

    @Override
    public int getCardPriority() {
        return steps;
    }

    @Override
    Texture getActionTexture() {
        return CardUtil.getDirectionTexture(direction);
    }

    @Override
    public void delete() {
        addAction(Actions.removeActor());
    }
}
