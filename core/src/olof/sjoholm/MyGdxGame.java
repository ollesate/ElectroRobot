package olof.sjoholm;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import olof.sjoholm.Api.Fonts;
import olof.sjoholm.Api.PlayerScreenHandler;
import olof.sjoholm.Api.ServerScreenHandler;
import olof.sjoholm.GameWorld.Assets.Textures;
import olof.sjoholm.Utils.Logger;
import olof.sjoholm.Views.LoginScreen;

public class MyGdxGame extends Game implements LoginScreen.LoginActions {
	private static final boolean debug = false;

	@Override
	public void create () {
		Logger.d("Game create");
		Textures.initialize();
		Skins.initialize();
		Fonts.initialize();
		if (debug) {
			ServerScreenHandler screenHandler = new ServerScreenHandler(this);
			screenHandler.showScreen(ServerScreenHandler.GAME);
		} else {
			if (Gdx.app.getType() == ApplicationType.Desktop) {
				setScreen(new LoginScreen(this));
			} else if (GameConfig.isServer()) {
                ServerScreenHandler screenHandler = new ServerScreenHandler(this);
                screenHandler.showScreen(ServerScreenHandler.GAME);
            } else {
                PlayerScreenHandler screenHandler = new PlayerScreenHandler(this);
                screenHandler.showScreen(PlayerScreenHandler.LOBBY);
            }
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
		Skins.dispose();
	}

	@Override
	public void onStartServer() {
		Logger.d("onStartServer");
        ServerScreenHandler screenHandler = new ServerScreenHandler(this);
		screenHandler.showScreen(ServerScreenHandler.GAME);
	}

	@Override
	public void onStartClient() {
		Logger.d("onStartClient");
		PlayerScreenHandler screenHandler = new PlayerScreenHandler(this);
		screenHandler.showScreen(PlayerScreenHandler.LOBBY);
	}

    @Override
    public void setScreen(Screen screen) {
		super.setScreen(screen);
		Logger.d("Set screen " + screen.getClass().getSimpleName());
    }
}
