package olof.sjoholm.utils.objects;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;

public class LinearLayout extends Group {

    @Override
    public void addActor(Actor actor) {
        actor.setWidth(getWidth());
        super.addActor(actor);
    }

    @Override
    protected void childrenChanged() {
        float totalHeight = 0;
        for (Actor actor : getChildren()) {
            totalHeight += actor.getHeight();
        }
        float y = totalHeight;
        for (Actor actor : getChildren()) {
            actor.setPosition(0, y, Align.topLeft);
            y -= actor.getHeight();
        }
        setHeight(totalHeight);
    }

    @Override
    protected void sizeChanged() {
        for (Actor actor : getChildren()) {
            actor.setWidth(getWidth());
        }
    }
}
