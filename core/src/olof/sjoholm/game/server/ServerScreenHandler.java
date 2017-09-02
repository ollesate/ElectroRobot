package olof.sjoholm.game.server;

import com.badlogic.gdx.Game;

import olof.sjoholm.game.shared.ScreenHandler;

public class ServerScreenHandler extends ScreenHandler {
    public static final String GAME = "game";

    public ServerScreenHandler(Game game) {
        super(game);
        addScreen(GAME, new ServerGameScreen());
    }
}
