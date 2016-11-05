package olof.sjoholm.GameWorld.Game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import olof.sjoholm.GameWorld.Assets.Textures;
import olof.sjoholm.GameWorld.Client.Screens.ClientScreenManager;
import olof.sjoholm.GameWorld.Client.ClientGameHandler;
import olof.sjoholm.GameWorld.Server.Screens.ServerScreenManager;
import olof.sjoholm.GameWorld.Server.ServerGame;
import olof.sjoholm.GameWorld.Utils.Logger;

public class MyGdxGame extends Game implements LoginScreen.LoginActions {

	@Override
	public void create () {
		Textures.initialize();
        setScreen(new LoginScreen(this));
	}
	
	@Override
	public void dispose () {
		super.dispose();
		Textures.dispose();
	}

	@Override
	public void render() {
		clearWhite();
		super.render();
	}

	private void clearWhite() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	@Override
	public void onStartServer() {
		new ServerGame(new ServerScreenManager(this));
	}

	@Override
	public void onStartClient() {
		new ClientGameHandler(new ClientScreenManager(this));
	}

    @Override
    public void setScreen(Screen screen) {
        super.setScreen(screen);
        Logger.d("Set screen " + screen.getClass().getSimpleName());
    }
}
