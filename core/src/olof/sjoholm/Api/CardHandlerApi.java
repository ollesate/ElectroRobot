package olof.sjoholm.Api;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

public class CardHandlerApi implements EventListener {

    public interface CardHandler {

    }

    @Override
    public boolean handle(Event event) {
        return false;
    }
}
