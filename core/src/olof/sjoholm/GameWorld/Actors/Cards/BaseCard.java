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

public abstract class BaseCard extends Group {
    private BaseActor priorityActor;
    private BaseActor actionActor;

    private ICard card;

    public BaseCard(ICard card) {
        this.card = card;
    }

    protected void build() {
        addActors();
    }

    private void addActors() {
        actionActor = new BaseActor(getActionTexture());
        actionActor.setY(25f);
        addActor(actionActor);

        priorityActor = new BaseActor(CardUtil.getNumberTexture(card.getPriority()));
        priorityActor.setY(-25f);
        addActor(priorityActor);
    }

    protected abstract Texture getActionTexture();

    public void select() {
        setColor(Color.BLUE);
    }

    public void unselect() {
        setColor(Color.WHITE);
    }

    @Override
    public void setColor(Color color) {
        super.setColor(color);
        priorityActor.setColor(color);
        actionActor.setColor(color);
    }
}
