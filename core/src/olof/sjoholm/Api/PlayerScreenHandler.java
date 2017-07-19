package olof.sjoholm.Api;

import com.badlogic.gdx.Game;

public class PlayerScreenHandler extends ScreenHandler {
    public static final String GAME = "game";

    public PlayerScreenHandler(Game game) {
        super(game);
        addScreen(GAME, new PlayerGameScreen());
    }
}
