package olof.sjoholm.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import olof.sjoholm.GameWorld.GameManagers.MyGdxGame;
import olof.sjoholm.GameWorld.Utils.Constants;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Constants.ViewportWidth;
		config.height = Constants.ViewportHeight;

		new LwjglApplication(new MyGdxGame(), config);
	}
}
