package olof.sjoholm.Api;

import com.badlogic.gdx.Game;

public class ServerScreenHandler extends ScreenHandler {
    public static final String GAME = "game";

    public ServerScreenHandler(Game game) {
        super(game);
        addScreen(GAME, new ServerGameScreen());
    }
}
