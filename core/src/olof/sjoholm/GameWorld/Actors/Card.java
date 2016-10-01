package olof.sjoholm.GameWorld.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import olof.sjoholm.GameWorld.Utils.CardUtil;
import olof.sjoholm.GameWorld.Utils.Direction;
import olof.sjoholm.Interfaces.Callback;
import olof.sjoholm.Interfaces.MovableToken;

/**
 * Created by sjoholm on 24/09/16.
 */
public class Card extends Group implements olof.sjoholm.Interfaces.ICard {
    private olof.sjoholm.Interfaces.MovableToken movableToken;
    private int steps;
    private Direction direction;

    private final BaseActor valueActor;
    private final BaseActor directionActor;

    public Card(MovableToken movableToken) {
        this.movableToken = movableToken;
        steps = (int)(Math.random() * 4 + 1);
        direction = Direction.random();


        valueActor = new BaseActor(CardUtil.getDirectionTexture(direction));
        valueActor.setY(25f);
        addActor(valueActor);

        directionActor = new BaseActor(CardUtil.getNumberTexture(steps));
        directionActor.setY(-25f);
        addActor(directionActor);
    }

    @Override
    public void playCard(final olof.sjoholm.Interfaces.Callback callback) {
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
    public void select() {
        setColor(Color.BLUE);
    }

    @Override
    public void unselect() {
        setColor(Color.WHITE);
    }

    @Override
    public void setColor(Color color) {
        super.setColor(color);
        valueActor.setColor(color);
        directionActor.setColor(color);
    }

    @Override
    public void delete() {
        addAction(Actions.removeActor());
    }
}
