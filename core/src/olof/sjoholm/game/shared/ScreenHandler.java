package olof.sjoholm.game.shared;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import java.util.HashMap;
import java.util.Map;

public class ScreenHandler {
    private final Map<String, Screen> screens = new HashMap<String, Screen>();
    private final Game game;

    public ScreenHandler(Game game) {
        this.game = game;
    }

    public void addScreen(String name, Screen screen) {
        screens.put(name, screen);
    }

    public void showScreen(String name) {
        Screen screen = screens.get(name);
        if (screen == null) {
            throw new IllegalStateException("No screen named " + name);
        }
        game.setScreen(screen);
    }
}
