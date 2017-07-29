package olof.sjoholm;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import olof.sjoholm.Api.PlayerGameScreen;
import olof.sjoholm.Api.PlayerScreenHandler;
import olof.sjoholm.Api.ScreenHandler;
import olof.sjoholm.Api.ServerGameScreen;
import olof.sjoholm.Api.ServerScreenHandler;
import olof.sjoholm.GameWorld.Assets.Textures;
import olof.sjoholm.Utils.Logger;
import olof.sjoholm.Views.LoginScreen;

public class MyGdxGame extends Game implements LoginScreen.LoginActions {
	private boolean debug = true;

	@Override
	public void create () {
		Logger.d("Game create");
		Textures.initialize();
		Skins.initialize();
		if (debug) {
			setScreen(new PlayerGameScreen());
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
		Skins.dispose();
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
		super.setScreen(screen);
		Logger.d("Set screen " + screen.getClass().getSimpleName());
    }
}
