package olof.sjoholm.game.player;

import com.badlogic.gdx.Game;

import olof.sjoholm.game.player.game.PlayerGameScreen;
import olof.sjoholm.game.player.lobby.PlayerLobbyScreen;

public class PlayerScreenHandler extends olof.sjoholm.game.shared.ScreenHandler {
    public static final String GAME = "game";
    public static final String LOBBY = "lobby";

    public PlayerScreenHandler(Game game) {
        super(game);
        addScreen(GAME, new PlayerGameScreen());
        addScreen(LOBBY, new PlayerLobbyScreen(this));
    }
}
