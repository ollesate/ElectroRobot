package olof.sjoholm.utils.ui.objects;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;

import olof.sjoholm.utils.Logger;
import sun.rmi.runtime.Log;

public class LinearLayout extends Group {
    private int alignment;

    @Override
    public void addActor(Actor actor) {
        actor.setWidth(getWidth());
        super.addActor(actor);
    }

    @Override
    protected void childrenChanged() {
        float prevY = getY(alignment);

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

        setPosition(getX(alignment), prevY, alignment);
    }

    @Override
    public void setPosition(float x, float y, int alignment) {
        this.alignment = alignment;
        super.setPosition(x, y, alignment);
    }

    @Override
    protected void sizeChanged() {
        for (Actor actor : getChildren()) {
            actor.setWidth(getWidth());
        }
    }
}
