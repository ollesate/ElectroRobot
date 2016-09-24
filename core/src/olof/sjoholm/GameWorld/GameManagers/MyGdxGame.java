package olof.sjoholm.GameWorld.GameManagers;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import olof.sjoholm.GameWorld.Assets.Textures;

public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
    private GameScreen gameScreen;

	@Override
	public void create () {
        gameScreen = new GameScreen();
		batch = new SpriteBatch();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameScreen.update(Gdx.graphics.getDeltaTime());
        gameScreen.draw();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		Textures.dispose();
	}
}
