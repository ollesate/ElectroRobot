package olof.sjoholm.GameWorld.GameManagers;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import olof.sjoholm.GameWorld.Assets.Textures;
import olof.sjoholm.Interfaces.ConnectActions;
import olof.sjoholm.Net.Client.ClientApi;
import olof.sjoholm.Net.Server.Client;
import olof.sjoholm.Net.Server.Server;
import olof.sjoholm.Net.ServerConstants;

public class MyGdxGame extends ApplicationAdapter implements ConnectActions {
	private SpriteBatch batch;
    private GameScreen gameScreen;
	private Server server;
	private ClientApi client;
	private LoginScreen loginScreen;

	@Override
	public void create () {
		Textures.initialize();
        gameScreen = new GameScreen();
		batch = new SpriteBatch();
		server = new Server();
		loginScreen = new LoginScreen(this);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		loginScreen.update(Gdx.graphics.getDeltaTime());
		loginScreen.draw();

//        gameScreen.update(Gdx.graphics.getDeltaTime());
//        gameScreen.draw();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		Textures.dispose();
		server.stop();
	}

	@Override
	public void onConnectClicked() {
		loginScreen.disableButton();
		client = new ClientApi();
	}

	@Override
	public void onBroadcastClicked() {
		server.broadcast("a simple broadcast message");
	}
}
