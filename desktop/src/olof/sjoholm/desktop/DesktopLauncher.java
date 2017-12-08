package olof.sjoholm.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import olof.sjoholm.MyGdxGame;
import olof.sjoholm.configuration.Constants;
import olof.sjoholm.game.player.PlayerGame;
import olof.sjoholm.game.server.ServerGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Constants.WORLD_WIDTH;
		config.height = Constants.WORLD_HEIGHT;

		String what = null;
		if (arg.length > 0) {
			what = arg[0];
		}

		if (what == null || what.equals("server")) {
			new LwjglApplication(new ServerGame(), config);
		} else if (what.equals("player")) {
			new LwjglApplication(new PlayerGame(), config);
		}
	}
}
