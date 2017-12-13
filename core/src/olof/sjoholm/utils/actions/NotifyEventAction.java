package olof.sjoholm.utils.actions;


import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;

import olof.sjoholm.utils.Logger;

public class NotifyEventAction extends Action {
    private final Event event;

    public NotifyEventAction(Event event) {
        this.event = event;
    }

    @Override
    public boolean act(float delta) {
        Actor target = getTarget();
        if (target != null) {
            this.target.notify(event, false);
        } else {
            Logger.e("No target to notify " + getClass().getSimpleName());
        }
        return true;
    }
}
