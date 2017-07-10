package olof.sjoholm;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import olof.sjoholm.Client.GameUiTest;
import olof.sjoholm.GameWorld.Assets.Textures;
import olof.sjoholm.Client.ClientController;
import olof.sjoholm.Views.LoginScreen;
import olof.sjoholm.Utils.Robo;
import olof.sjoholm.GameLogic.ServerGameController;
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
		Robo.isServer = true;
		new ServerGameController(this);
	}

	@Override
	public void onStartClient() {
		Robo.isServer = false;
		new ClientController(this);
	}

    @Override
    public void setScreen(Screen screen) {
		Logger.d("Set screen " + screen.getClass().getSimpleName());
		super.setScreen(screen);
    }
}
