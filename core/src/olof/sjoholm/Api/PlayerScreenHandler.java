package olof.sjoholm.Api;

import com.badlogic.gdx.Game;

public class PlayerScreenHandler extends ScreenHandler {
    public static final String GAME = "game";
    public static final String LOBBY = "lobby";

    public PlayerScreenHandler(Game game) {
        super(game);
        addScreen(GAME, new PlayerGameScreen());
        addScreen(LOBBY, new PlayerLobbyScreen(this));
    }
}
