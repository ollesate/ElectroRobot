package olof.sjoholm.game.server;

import com.badlogic.gdx.scenes.scene2d.Event;

public class TerminalEvent extends Event {
    public final String[] args;

    public TerminalEvent(String... args) {
        this.args = args;
    }
}
