package olof.sjoholm.GameWorld.Utils;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

/**
 * Created by sjoholm on 02/10/16.
 */

public abstract class ScreenAdapter implements Screen {
    private static final Color COLOR_WHITE = new Color(1, 1, 1, 1);
    private Color backgroundColor;

    @Override
    public void hide() {
        // Do nothing
    }

    @Override
    public void pause() {
        // Do nothing
    }

    @Override
    public void resize(int width, int height) {
        // Do nothing
    }

    @Override
    public void resume() {
        // Do nothing
    }

    public void setBackgroundColor(Color color) {
        backgroundColor = color;
    }

    @Override
    public void render(float delta) {
        Color color = (backgroundColor != null) ? backgroundColor : COLOR_WHITE;
        Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}
