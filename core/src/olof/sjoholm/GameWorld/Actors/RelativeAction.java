package olof.sjoholm.GameWorld.Actors;

import com.badlogic.gdx.scenes.scene2d.Action;

public abstract class RelativeAction extends Action {
    private boolean hasBegun;

    public abstract void begin();

    public abstract boolean update(float delta);

    @Override
    public boolean act(float delta) {
        if (!hasBegun) {
            begin();
            hasBegun = true;
        }
        return update(delta);
    }
}
