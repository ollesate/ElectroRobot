package olof.sjoholm;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import olof.sjoholm.Api.ScreenHandler;
import olof.sjoholm.Client.GameUiTest;
import olof.sjoholm.GameWorld.Assets.Textures;
import olof.sjoholm.Client.ClientController;
import olof.sjoholm.Net.Server.Server;
import olof.sjoholm.Net.ServerConstants;
import olof.sjoholm.Views.LoginScreen;
import olof.sjoholm.Utils.Logger;

public class MyGdxGame extends Game implements LoginScreen.LoginActions {
	public static final boolean isDebug = false;

	@Override
	public void create () {
		Textures.initialize();
		if (isDebug) {
			new GameUiTest(this);
		} else {
			setScreen(new LoginScreen(this));
		}
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
	}

	@Override
	public void dispose () {
		super.dispose();
		Textures.dispose();
	}

	@Override
	public void onStartServer() {
		Server server = new Server(ServerConstants.HOST_NAME, ServerConstants.CONNECTION_PORT);
		server.start();
        ScreenHandler screenHandler = new ScreenHandler(this, server);
        screenHandler.showLobbyScreen();
	}

	@Override
	public void onStartClient() {
		new ClientController(this);
	}

    @Override
    public void setScreen(Screen screen) {
		Logger.d("Set screen " + screen.getClass().getSimpleName());
		super.setScreen(screen);
    }
}
