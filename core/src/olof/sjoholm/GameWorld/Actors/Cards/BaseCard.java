package olof.sjoholm.GameWorld.Actors.Cards;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;

import olof.sjoholm.GameWorld.Actors.BaseActor;
import olof.sjoholm.GameWorld.Utils.CardUtil;
import olof.sjoholm.Interfaces.ICard;

/**
 * Created by sjoholm on 02/10/16.
 */

public abstract class BaseCard extends Group implements ICard {
    private BaseActor priorityActor;
    private BaseActor actionActor;

    protected void addActors() {
        actionActor = new BaseActor(getActionTexture());
        actionActor.setY(25f);
        addActor(actionActor);

        priorityActor = new BaseActor(CardUtil.getNumberTexture(getCardPriority()));
        priorityActor.setY(-25f);
        addActor(priorityActor);
    }

    abstract Texture getActionTexture();

    protected void select() {
        setColor(Color.BLUE);
    }

    protected void unselect() {
        setColor(Color.WHITE);
    }

    @Override
    public void setColor(Color color) {
        super.setColor(color);
        priorityActor.setColor(color);
        actionActor.setColor(color);
    }
}
