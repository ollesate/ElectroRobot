package olof.sjoholm.Api;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;

public class LinearLayout extends Group {

    @Override
    public void addActor(Actor actor) {
        actor.setWidth(getWidth());
        setHeight(getHeight() + actor.getHeight());
        super.addActor(actor);
    }

    @Override
    public boolean removeActor(Actor actor) {
        setHeight(getHeight() - actor.getHeight());
        return super.removeActor(actor);
    }

    @Override
    protected void childrenChanged() {
        float y = getHeight();
        for (Actor actor : getChildren()) {
            actor.setPosition(0, y, Align.topLeft);
            y -= actor.getHeight();
        }
    }

    @Override
    protected void sizeChanged() {
        for (Actor actor : getChildren()) {
            actor.setWidth(getWidth());
        }
    }
}
