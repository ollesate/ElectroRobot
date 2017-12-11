package olof.sjoholm.game.server;

import com.badlogic.gdx.scenes.scene2d.Event;

public class TerminalEvent extends Event {

    public final String[] args;

    public TerminalEvent(String... args) {
        this.args = args;
    }

    public String getCommand() {
        return get(0);
    }

    public String get(int index) {
        if (args.length > index) {
            return args[index];
        }
        return null;
    }

    public int getInt(int index) throws TerminalException {
        if (args.length > index) {
            try {
                return Integer.valueOf(args[index]);
            } catch (NumberFormatException e) {
                throw new TerminalException(e.getMessage());
            }
        }
        throw new TerminalException("Index out of bounds");
    }

    public int getLength(int offset) {
        return Math.max(0, args.length - offset);
    }
}
