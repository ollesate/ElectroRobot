package olof.sjoholm.GameWorld.Actors;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;

public class FireEventAction extends Action {
    private final Event event;

    public FireEventAction(Event event) {
        this.event = event;
    }

    @Override
    public boolean act(float delta) {
        Actor target = getTarget();
        target.fire(event);
        return true;
    }
}
