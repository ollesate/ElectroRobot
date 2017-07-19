package olof.sjoholm;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import olof.sjoholm.Api.PlayerScreenHandler;
import olof.sjoholm.Api.ServerScreenHandler;
import olof.sjoholm.Client.GameUiTest;
import olof.sjoholm.GameWorld.Assets.Textures;
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
        ServerScreenHandler screenHandler = new ServerScreenHandler(this);
		screenHandler.showScreen(ServerScreenHandler.LOBBY);
	}

	@Override
	public void onStartClient() {
		PlayerScreenHandler screenHandler = new PlayerScreenHandler(this);
		screenHandler.showScreen(PlayerScreenHandler.GAME);
	}

    @Override
    public void setScreen(Screen screen) {
		Logger.d("Set screen " + screen.getClass().getSimpleName());
		super.setScreen(screen);
    }
}
